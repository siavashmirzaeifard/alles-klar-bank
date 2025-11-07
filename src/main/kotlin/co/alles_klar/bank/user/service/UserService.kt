package co.alles_klar.bank.user.service

import co.alles_klar.bank.user.database.entity.ForgotPasswordTokenEntity
import co.alles_klar.bank.user.database.entity.UserEntity
import co.alles_klar.bank.user.database.repository.ForgotPasswordTokenRepository
import co.alles_klar.bank.user.database.repository.RefreshTokenRepository
import co.alles_klar.bank.user.database.repository.UserRepository
import co.alles_klar.bank.user.domain.model.User
import co.alles_klar.bank.utility.constant.UserId
import co.alles_klar.bank.utility.encoder.PasswordEncoder
import co.alles_klar.bank.utility.encoder.SecureTokenGenerator
import co.alles_klar.bank.utility.exception.InvalidPasswordException
import co.alles_klar.bank.utility.exception.InvalidTokenException
import co.alles_klar.bank.utility.exception.SamePasswordException
import co.alles_klar.bank.utility.exception.UserNotFoundException
import jakarta.persistence.LockModeType
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.jpa.repository.Lock
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.Optional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val forgotPasswordTokenRepository: ForgotPasswordTokenRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val passwordEncoder: PasswordEncoder,
    private val secureTokenGenerator: SecureTokenGenerator,
    @param:Value(value = "\${reset-password.validity-minutes}") private val resetTokenValidity: Long
    ) {

    @Transactional(rollbackFor = [Exception::class])
    fun forgetPassword(username: String) {
        val user = userRepository.findByUsername(username = username)
            ?: return

        val resetToken = secureTokenGenerator.generate()
        val validityInMs = resetTokenValidity * 60 * 1000

        user.id?.let {
            forgotPasswordTokenRepository.saveAndFlush(
                ForgotPasswordTokenEntity(
                    userId = it,
                    resetToken = resetToken,
                    expiresAt = Instant.now().plusMillis(validityInMs)
                )
            )
        }
    }

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Transactional(rollbackFor = [Exception::class])
    fun resetPassword(
        resetPasswordToken: String,
        newPassword: String
    ) {
        val tokenEntity = forgotPasswordTokenRepository.findByResetToken(resetToken = resetPasswordToken)
            ?: throw InvalidTokenException()

        if (tokenEntity.usedAt != null && tokenEntity.expiresAt < Instant.now()) {
            throw InvalidTokenException()
        }

        val user = userRepository.findById(tokenEntity.userId).orElseThrow { UserNotFoundException() }

        if (passwordEncoder.matches(
                rawPassword = newPassword,
                encodedPassword = user.hashedPassword
            )) {
            throw SamePasswordException()
        }

        user.id?.let {
            forgotPasswordTokenRepository.saveAndFlush(
                tokenEntity.apply {
                    this.usedAt = Instant.now()
                }
            )

            userRepository.saveAndFlush(
                user.apply {
                    hashedPassword = passwordEncoder.encode(rawPassword = newPassword)
                }
            )

            refreshTokenRepository.deleteByUserId(userId = it)
        } ?: throw UserNotFoundException()
    }

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Transactional(rollbackFor = [Exception::class])
    fun changePassword(
        userId: UserId,
        oldPassword: String,
        newPassword: String
    ) {
        val user = userRepository.findById(userId).orElseThrow { InvalidTokenException() }

        if (newPassword == oldPassword) {
            throw SamePasswordException()
        }

        if (!passwordEncoder.matches(
            rawPassword = oldPassword,
            encodedPassword = user.hashedPassword
        )) {
            throw InvalidPasswordException()
        }

        refreshTokenRepository.deleteByUserId(userId = userId)

        userRepository.saveAndFlush(
            user.apply {
                this.hashedPassword = passwordEncoder.encode(rawPassword = newPassword)
            }
        )
    }

    fun getUser(userId: UserId): Optional<UserEntity> {
        return userRepository.findById(userId)
    }

    @Scheduled(cron = "0 0 4 * * *")
    private fun cleanupExpiredTokens() {
        forgotPasswordTokenRepository.deleteByExpiresAtLessThan(now = Instant.now())
    }

}