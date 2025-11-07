package co.alles_klar.bank.transaction.api.dto

import co.alles_klar.bank.utility.constant.AccountId
import co.alles_klar.bank.utility.constant.TransactionId
import co.alles_klar.bank.utility.constant.TransactionTypes

data class WithdrawResponse(

    val id: TransactionId,
    val fromAccountId: AccountId,
    val transactionType: TransactionTypes,
    val amount: Double

)
