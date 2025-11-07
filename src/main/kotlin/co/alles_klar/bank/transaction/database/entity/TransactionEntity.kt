package co.alles_klar.bank.transaction.database.entity

import co.alles_klar.bank.utility.constant.AccountId
import co.alles_klar.bank.utility.constant.TransactionId
import co.alles_klar.bank.utility.constant.TransactionTypes
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import java.time.Instant

@Entity
@Table(
    name = "transactions",
    schema = "transaction_service",
    indexes = [
        Index(
            name = "idx_transactions_user_transactions",
            columnList = "from_account_id"
        )
    ]
)
class TransactionEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: TransactionId? = null,

    @Column(nullable = true)
    var fromAccountId: AccountId? = null,

    @Column(nullable = true)
    var toAccountId: AccountId? = null,

    @Column(nullable = false)
    var amount: Double,

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    var transactionType: TransactionTypes,

    @CreationTimestamp
    var createdAt: Instant = Instant.now()

)