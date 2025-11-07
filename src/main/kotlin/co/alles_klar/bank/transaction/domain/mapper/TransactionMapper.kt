package co.alles_klar.bank.transaction.domain.mapper

import co.alles_klar.bank.transaction.api.dto.DepositResponse
import co.alles_klar.bank.transaction.api.dto.TransferResponse
import co.alles_klar.bank.transaction.api.dto.WithdrawResponse
import co.alles_klar.bank.transaction.domain.model.Transaction

fun Transaction.toTransferResponse(): TransferResponse {
    return TransferResponse(
        id = this.id,
        fromAccountId = this.fromAccountId!!,
        toAccountId = this.toAccountId!!,
        transactionType = this.transactionType,
        amount = this.amount
    )
}

fun Transaction.toDepositResponse(): DepositResponse {
    return DepositResponse(
        id = this.id,
        toAccountId = this.toAccountId!!,
        transactionType = this.transactionType,
        amount = this.amount
    )
}

fun Transaction.toWithdrawResponse(): WithdrawResponse {
    return WithdrawResponse(
        id = this.id,
        fromAccountId = this.fromAccountId!!,
        transactionType = this.transactionType,
        amount = this.amount
    )
}