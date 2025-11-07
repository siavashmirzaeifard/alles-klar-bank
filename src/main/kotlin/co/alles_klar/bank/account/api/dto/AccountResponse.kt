package co.alles_klar.bank.account.api.dto

import co.alles_klar.bank.utility.constant.AccountId
import co.alles_klar.bank.utility.constant.AccountTypes

data class AccountResponse(
    val id: AccountId,
    val accountName: String,
    val balance: Double,
    val accountType: AccountTypes
)
