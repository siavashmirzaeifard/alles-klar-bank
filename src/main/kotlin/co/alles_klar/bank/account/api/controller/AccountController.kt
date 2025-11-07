package co.alles_klar.bank.account.api.controller

import co.alles_klar.bank.account.api.dto.AccountResponse
import co.alles_klar.bank.account.domain.mapper.toResponse
import co.alles_klar.bank.account.service.AccountService
import co.alles_klar.bank.utility.constant.UserId
import co.alles_klar.bank.utility.exception.InvalidTokenException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/api/account"])
class AccountController(
    private val accountService: AccountService
) {

    @GetMapping(value = ["/"])
    fun getAll(): Set<AccountResponse> {
        val userId = SecurityContextHolder.getContext().authentication?.principal as? UserId
            ?: throw InvalidTokenException()

        return accountService.getAll(userId = userId).map { it.toResponse() }.toSet()
    }

}