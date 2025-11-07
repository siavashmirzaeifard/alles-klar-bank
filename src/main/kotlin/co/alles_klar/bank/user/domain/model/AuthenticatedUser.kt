package co.alles_klar.bank.user.domain.model

data class AuthenticatedUser(

    val user: User,
    val accessToken: String,
    val refreshToken: String

)