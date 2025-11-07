package co.alles_klar.bank.account.domain.model

import co.alles_klar.bank.user.domain.model.User
import co.alles_klar.bank.utility.constant.AccountId
import co.alles_klar.bank.utility.constant.AccountTypes

data class Account(

    val id: AccountId,
    val accountName: String,
    val accountType: AccountTypes,
    val balance: Double,
    val user: User

)
