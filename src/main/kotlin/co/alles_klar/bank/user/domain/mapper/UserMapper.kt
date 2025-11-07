package co.alles_klar.bank.user.domain.mapper

import co.alles_klar.bank.user.api.dto.LoginResponse
import co.alles_klar.bank.user.api.dto.RegisterResponse
import co.alles_klar.bank.user.domain.model.AuthenticatedUser
import co.alles_klar.bank.user.domain.model.User

fun User.toResponse(): RegisterResponse {
    return RegisterResponse(
        id = this.id,
        name = this.fullName,
        email = this.email,
        username = this.username,
        age = this.age,
        createdAt = this.createdAt
    )
}

fun AuthenticatedUser.toResponse(): LoginResponse {
    return LoginResponse(
        user = this.user,
        accessToken = this.accessToken,
        refreshToken = this.refreshToken
    )
}