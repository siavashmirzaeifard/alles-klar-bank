package co.alles_klar.bank.user.domain.model

import co.alles_klar.bank.utility.constant.AccountId
import co.alles_klar.bank.utility.constant.UserId
import java.time.Instant

data class User(

    val id: UserId,
    val fullName: String,
    val username: String,
    val email: String,
    val age: Int,
//    val defaultAccountId: AccountId,
    val createdAt: Instant

)
