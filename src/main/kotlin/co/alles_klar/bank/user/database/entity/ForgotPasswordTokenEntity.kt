package co.alles_klar.bank.user.database.entity

import co.alles_klar.bank.utility.constant.UserId
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import java.time.Instant

@Entity
@Table(
    name = "forgot_password_tokens",
    schema = "user_service",
    indexes = [
        Index(
            name = "idx_forgot_password_tokens_token",
            columnList = "reset_token"
        ),
        Index(
            name = "idx_forgot_password_tokens_user_token",
            columnList = "user_id,reset_token"
        ),
    ]
)
class ForgotPasswordTokenEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(nullable = false)
    var userId: UserId,

    @Column(
        nullable = false,
        unique = true
    )
    var resetToken: String,

    @CreationTimestamp
    var createdAt: Instant = Instant.now(),

    @Column(nullable = false)
    var expiresAt: Instant,

    @Column(nullable = true)
    var usedAt: Instant? = null

)