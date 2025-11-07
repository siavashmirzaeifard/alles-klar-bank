package co.alles_klar.bank.account.database.entity

import co.alles_klar.bank.user.database.entity.UserEntity
import co.alles_klar.bank.utility.constant.AccountId
import co.alles_klar.bank.utility.constant.AccountTypes
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant

@Entity
@Table(
    name = "accounts",
    schema = "account_service",
    indexes = [
        Index(
            name = "idx_accounts_user_account",
            columnList = "user_id,id"
        )
    ]
)
class AccountEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: AccountId? = null,

    @Column(
        nullable = false,
        unique = true
    )
    var accountName: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "user_id",
        referencedColumnName = "id",
        nullable = false
    )
    var user: UserEntity,

    @Column(nullable = false)
    var balance: Double,

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    var accountType: AccountTypes,

    @CreationTimestamp
    var createdAt: Instant = Instant.now(),

    @UpdateTimestamp
    var updatedAt: Instant? = null

)