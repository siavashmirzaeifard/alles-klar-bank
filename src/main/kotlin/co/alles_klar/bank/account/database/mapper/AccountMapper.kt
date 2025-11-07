package co.alles_klar.bank.account.database.mapper

import co.alles_klar.bank.account.database.entity.AccountEntity
import co.alles_klar.bank.account.domain.model.Account
import co.alles_klar.bank.user.database.mapper.toDomain

fun AccountEntity.toDomain(): Account {
    return Account(
        id = this.id!!,
        accountName = this.accountName,
        accountType = accountType,
        balance = this.balance,
        user = this.user.toDomain()
    )
}