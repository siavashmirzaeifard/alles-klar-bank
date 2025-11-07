package co.alles_klar.bank.user.api.dto

import co.alles_klar.bank.user.domain.model.User

data class LoginResponse(
    val user: User,
    val accessToken: String,
    val refreshToken: String
)
