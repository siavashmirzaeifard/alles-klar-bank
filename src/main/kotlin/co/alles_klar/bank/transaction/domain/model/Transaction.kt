package co.alles_klar.bank.transaction.domain.model

import co.alles_klar.bank.utility.constant.AccountId
import co.alles_klar.bank.utility.constant.TransactionId
import co.alles_klar.bank.utility.constant.TransactionTypes
import java.time.Instant

data class Transaction(

    val id: TransactionId,
    val fromAccountId: AccountId?,
    val toAccountId: AccountId?,
    val amount: Double,
    val transactionType: TransactionTypes,
    val createdAt: Instant

)
