package co.alles_klar.bank.user.database.repository

import co.alles_klar.bank.user.database.entity.RefreshTokenEntity
import co.alles_klar.bank.utility.constant.UserId
import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant

interface RefreshTokenRepository: JpaRepository<RefreshTokenEntity, Long> {

    fun deleteByUserId(userId: UserId)
    fun deleteByUserIdAndHashedToken(userId: UserId, hashedToken: String)
    fun deleteByExpiresAtLessThan(now: Instant)

}