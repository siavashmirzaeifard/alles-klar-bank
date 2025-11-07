package co.alles_klar.bank.user.api.dto

import co.alles_klar.bank.utility.constant.UserId
import java.time.Instant

data class RegisterResponse(

    val id: UserId,
    val name: String,
    val email: String,
    val username: String,
    val age: Int,
    val createdAt: Instant

)