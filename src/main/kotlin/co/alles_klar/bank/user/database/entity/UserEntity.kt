package co.alles_klar.bank.user.database.entity

import co.alles_klar.bank.account.database.entity.AccountEntity
import co.alles_klar.bank.utility.constant.AccountId
import co.alles_klar.bank.utility.constant.UserId
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.time.Instant

@Entity
@Table(
    name = "users",
    schema = "user_service",
    indexes = [
        Index(
            name = "idx_users_username",
            columnList = "username"
        ),
        Index(
            name = "idx_users_email",
            columnList = "email"
        )
    ]
)
class UserEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UserId? = null,

    @Column(nullable = false)
    var firstName: String,

    @Column(nullable = false)
    var lastName: String,

    @Column(
        nullable = false,
        unique = true
    )
    var email: String,

    @Column(
        nullable = false,
        unique = true
    )
    var username: String,

    @Column(nullable = false)
    var age: Int,

    @Column(nullable = false)
    var hashedPassword: String,

    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "user",
        targetEntity = AccountEntity::class
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    var accounts: Set<AccountEntity> = emptySet(),

    @Column(nullable = true)
    var defaultAccountId: AccountId? = null,

    @CreationTimestamp
    var createdAt: Instant = Instant.now(),

    @Column(nullable = true)
    var updatedAt: Instant? = null

)