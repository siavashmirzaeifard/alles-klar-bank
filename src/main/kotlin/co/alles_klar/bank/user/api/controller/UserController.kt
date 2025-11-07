package co.alles_klar.bank.user.api.controller

import co.alles_klar.bank.user.api.dto.ChangePasswordRequest
import co.alles_klar.bank.user.api.dto.ForgotPasswordRequest
import co.alles_klar.bank.user.api.dto.LoginRequest
import co.alles_klar.bank.user.api.dto.LoginResponse
import co.alles_klar.bank.user.api.dto.LogoutRequest
import co.alles_klar.bank.user.api.dto.RefreshRequest
import co.alles_klar.bank.user.api.dto.RegisterRequest
import co.alles_klar.bank.user.api.dto.RegisterResponse
import co.alles_klar.bank.user.api.dto.ResetPasswordRequest
import co.alles_klar.bank.user.domain.mapper.toResponse
import co.alles_klar.bank.user.service.AuthService
import co.alles_klar.bank.user.service.UserService
import co.alles_klar.bank.utility.constant.UserId
import co.alles_klar.bank.utility.exception.InvalidTokenException
import jakarta.validation.Valid
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/api/auth"])
class UserController(
    private val userService: UserService,
    private val authService: AuthService
) {

    @PostMapping(value = ["/register"])
    fun register(
        @Valid @RequestBody body: RegisterRequest
    ): RegisterResponse {
        return authService.register(
            firstName = body.firstName,
            lastName = body.lastName,
            username = body.username,
            email = body.email,
            age = body.age,
            password = body.password
        ).toResponse()
    }

    @PostMapping(value = ["/login"])
    fun login(
        @RequestBody body: LoginRequest
    ): LoginResponse {
        return authService.login(
            username = body.username,
            password = body.password
        ).toResponse()
    }

    @PostMapping(value = ["/refresh"])
    fun refresh(
        @RequestBody body: RefreshRequest
    ): LoginResponse {
        return authService.refresh(refreshToken = body.refreshToken).toResponse()
    }

    @PostMapping(value = ["/logout"])
    fun logout(
        @RequestBody body: LogoutRequest
    ) {
        authService.logout(refreshToken = body.refreshToken)
    }

    @PostMapping(value = ["/forgot-password"])
    fun forgotPassword(
        @Valid @RequestBody body: ForgotPasswordRequest
    ) {
        userService.forgetPassword(username = body.username)
    }

    @PostMapping(value = ["/reset-password"])
    fun resetPassword(
        @Valid @RequestBody body: ResetPasswordRequest
    ) {
        userService.resetPassword(
            resetPasswordToken = body.resetPasswordToken,
            newPassword = body.newPassword
        )
    }

    @PostMapping(value = ["/change-password"])
    fun changePassword(
        @Valid @RequestBody body: ChangePasswordRequest
    ) {
        val userId = SecurityContextHolder.getContext().authentication?.principal as? UserId
            ?: throw InvalidTokenException()

        userService.changePassword(
            userId = userId,
            oldPassword = body.oldPassword,
            newPassword = body.newPassword
        )
    }

}