package co.alles_klar.bank.transaction.database.mapper

import co.alles_klar.bank.transaction.database.entity.TransactionEntity
import co.alles_klar.bank.transaction.domain.model.Transaction

fun TransactionEntity.toDomain(): Transaction {
    return Transaction(
        id = this.id!!,
        fromAccountId = this.fromAccountId,
        toAccountId = this.toAccountId,
        amount = this.amount,
        transactionType = this.transactionType,
        createdAt = this.createdAt
    )
}