package co.alles_klar.bank.user.api.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import org.hibernate.validator.constraints.Length

data class RegisterRequest(

    @field:NotEmpty(message = "First name can not be empty")
    @field:NotNull(message = "First name is required")
    val firstName: String,

    @field:NotEmpty(message = "Last name can not be empty")
    @field:NotNull(message = "Last name is required")
    val lastName: String,

    @field:NotEmpty(message = "Email address can not be empty")
    @field:NotNull(message = "Email is required")
    @field:Email(message = "Invalid email format")
    val email: String,

    @field:Length(
        min = 4,
        message = "Username must be at least 4 characters"
    )
    val username: String,

    @field:Min(
        value = 16,
        message = "Minimum age to register an account is 16"
    )
    val age: Int,

    @field:Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{9,}\$",
        message = "Password must be at least 9 characters long and contain at least one digit, uppercase and lowercase character."
    )
    val password: String

)