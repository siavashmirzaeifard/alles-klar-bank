package co.alles_klar.bank.account.domain.mapper

import co.alles_klar.bank.account.api.dto.AccountResponse
import co.alles_klar.bank.account.domain.model.Account

fun Account.toResponse(): AccountResponse {
    return AccountResponse(
        id = this.id,
        accountName = this.accountName,
        accountType = this.accountType,
        balance = this.balance
    )
}