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
    name = "refresh_tokens",
    schema = "user_service",
    indexes = [
        Index(
            name = "idx_refresh_tokens_hashed_token",
            columnList = "hashed_token"
        )
    ]
)
class RefreshTokenEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(nullable = false)
    var userId: UserId,

    @Column(
        nullable = false,
        unique = true
    )
    var hashedToken: String,

    @CreationTimestamp
    var createdAt: Instant = Instant.now(),

    @Column(nullable = false)
    var expiresAt: Instant

)