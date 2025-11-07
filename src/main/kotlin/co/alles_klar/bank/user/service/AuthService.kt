package co.alles_klar.bank.user.service

import co.alles_klar.bank.account.service.AccountService
import co.alles_klar.bank.user.database.entity.RefreshTokenEntity
import co.alles_klar.bank.user.database.entity.UserEntity
import co.alles_klar.bank.user.database.mapper.toDomain
import co.alles_klar.bank.user.database.repository.RefreshTokenRepository
import co.alles_klar.bank.user.database.repository.UserRepository
import co.alles_klar.bank.user.domain.model.AuthenticatedUser
import co.alles_klar.bank.user.domain.model.User
import co.alles_klar.bank.utility.constant.AccountTypes
import co.alles_klar.bank.utility.encoder.PasswordEncoder
import co.alles_klar.bank.utility.encoder.TokenEncoder
import co.alles_klar.bank.utility.exception.InvalidCredentialsException
import co.alles_klar.bank.utility.exception.InvalidTokenException
import co.alles_klar.bank.utility.exception.UserAlreadyExistsException
import co.alles_klar.bank.utility.exception.UserNotFoundException
import co.alles_klar.bank.utility.jwt.JwtService
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.Lock
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.UUID

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val accountService: AccountService,
    private val jwtService: JwtService,
    private val passwordEncoder: PasswordEncoder,
    private val tokenEncoder: TokenEncoder
) {

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Transactional(
        isolation = Isolation.SERIALIZABLE,
        rollbackFor = [Exception::class]
    )
    fun register(
        firstName: String,
        lastName: String,
        username: String,
        email: String,
        age: Int,
        password: String
    ): User {
        val user = userRepository.findByUsernameOrEmail(
            username = username,
            email = email
        )

        if (user != null) {
            throw UserAlreadyExistsException()
        }

        val userEntity = userRepository.saveAndFlush(
            UserEntity(
                firstName = firstName,
                lastName = lastName,
                email = email,
                username = username,
                age = age,
                hashedPassword = passwordEncoder.encode(rawPassword = password)
            )
        )

        val account = accountService.create(
            accountName = "${firstName}-${lastName}-${userEntity.id}" + UUID.randomUUID(),
            user = userEntity,
            balance = 0.0,
            accountType = AccountTypes.CHECKING
        )

        return userRepository.saveAndFlush(
            userEntity.apply {
                defaultAccountId = account.id
            }
        ).toDomain()
    }

    @Transactional
    fun login(
        username: String,
        password: String
    ): AuthenticatedUser {
        val user = userRepository.findByUsername(username = username)
            ?: throw InvalidCredentialsException()

        if (!passwordEncoder.matches(
            rawPassword = password,
            encodedPassword = user.hashedPassword
        )) {
            throw InvalidCredentialsException()
        }

        return user.id?.let {
            val accessToken = jwtService.generateAccessToken(userId = it)
            val refreshToken = jwtService.generateRefreshToken(userId = it)

            refreshTokenRepository.saveAndFlush(
                RefreshTokenEntity(
                    userId = it,
                    hashedToken = tokenEncoder.encode(rawToken = refreshToken),
                    expiresAt = Instant.now().plusMillis(jwtService.refreshTokenValidityMs)
                )
            )

            AuthenticatedUser(
                user = user.toDomain(),
                accessToken = accessToken,
                refreshToken = refreshToken
            )
        } ?: throw UserNotFoundException()
    }

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Transactional(rollbackFor = [Exception::class])
    fun refresh(refreshToken: String): AuthenticatedUser {
        val userId = jwtService.getUserIdFromToken(token = refreshToken)
        val user = userRepository.findById(userId).orElseThrow { UserNotFoundException() }

        return user.id?.let {
            val accessToken = jwtService.generateAccessToken(userId = it)
            val refreshToken = jwtService.generateRefreshToken(userId = it)

            refreshTokenRepository.deleteByUserId(userId = it)

            refreshTokenRepository.saveAndFlush(
                RefreshTokenEntity(
                    userId = it,
                    hashedToken = tokenEncoder.encode(rawToken = refreshToken),
                    expiresAt = Instant.now().plusMillis(jwtService.refreshTokenValidityMs)
                )
            )

            AuthenticatedUser(
                user = user.toDomain(),
                accessToken = accessToken,
                refreshToken = refreshToken
            )
        } ?: throw UserNotFoundException()
    }

    @Transactional(rollbackFor = [Exception::class])
    fun logout(refreshToken: String) {
        if (!jwtService.validateToken(
            token = refreshToken,
            type = "refresh"
        )) {
            throw InvalidTokenException()
        }

        val userId = jwtService.getUserIdFromToken(token = refreshToken)

        refreshTokenRepository.deleteByUserIdAndHashedToken(
            userId = userId,
            hashedToken = tokenEncoder.encode(rawToken = refreshToken)
        )
    }

    @Scheduled(cron = "0 0 3 * * *")
    private fun cleanupExpiredTokens() {
        refreshTokenRepository.deleteByExpiresAtLessThan(now = Instant.now())
    }

}