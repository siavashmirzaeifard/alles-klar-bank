package co.alles_klar.bank.user.api.dto

import jakarta.validation.constraints.Pattern

data class ChangePasswordRequest(
    val oldPassword: String,

    @field:Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{9,}\$",
        message = "Password must be at least 9 characters long and contain at least one digit, uppercase and lowercase character."
    )
    val newPassword: String
)
