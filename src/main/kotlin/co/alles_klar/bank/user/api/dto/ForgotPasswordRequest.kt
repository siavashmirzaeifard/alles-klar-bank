package co.alles_klar.bank.user.api.dto

import org.hibernate.validator.constraints.Length

data class ForgotPasswordRequest(
    @field:Length(
        min = 4,
        message = "Username must be at least 4 characters"
    )
    val username: String
)