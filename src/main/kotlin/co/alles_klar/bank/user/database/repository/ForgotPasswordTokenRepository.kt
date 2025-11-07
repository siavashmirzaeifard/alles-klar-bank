package co.alles_klar.bank.user.database.repository

import co.alles_klar.bank.user.database.entity.ForgotPasswordTokenEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant

interface ForgotPasswordTokenRepository: JpaRepository<ForgotPasswordTokenEntity, Long> {

    fun findByResetToken(resetToken: String): ForgotPasswordTokenEntity?
    fun deleteByExpiresAtLessThan(now: Instant)

}