package co.alles_klar.bank.transaction.api.controller

import co.alles_klar.bank.transaction.api.dto.DepositRequest
import co.alles_klar.bank.transaction.api.dto.DepositResponse
import co.alles_klar.bank.transaction.api.dto.TransferRequest
import co.alles_klar.bank.transaction.api.dto.TransferResponse
import co.alles_klar.bank.transaction.api.dto.WithdrawRequest
import co.alles_klar.bank.transaction.api.dto.WithdrawResponse
import co.alles_klar.bank.transaction.domain.mapper.toDepositResponse
import co.alles_klar.bank.transaction.domain.mapper.toTransferResponse
import co.alles_klar.bank.transaction.domain.mapper.toWithdrawResponse
import co.alles_klar.bank.transaction.service.TransactionService
import co.alles_klar.bank.utility.constant.UserId
import co.alles_klar.bank.utility.exception.InvalidTokenException
import jakarta.validation.Valid
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/api/transaction"])
class TransactionController(
    private val transactionService: TransactionService
) {

    @PostMapping(value = ["/transfer"])
    fun transfer(
        @Valid @RequestBody body: TransferRequest
    ): TransferResponse {
        val userId = SecurityContextHolder.getContext().authentication?.principal as? UserId
            ?: throw InvalidTokenException()

        return transactionService.transfer(
            userId = userId,
            fromAccountId = body.fromAccountId,
            toAccountId = body.toAccountId,
            amount = body.amount
        ).toTransferResponse()
    }

    @PostMapping(value = ["/deposit"])
    fun deposit(
        @Valid @RequestBody body: DepositRequest
    ): DepositResponse {
        return transactionService.deposit(
            toAccountId = body.toAccountId,
            amount = body.amount
        ).toDepositResponse()
    }

    @PostMapping(value = ["/withdraw"])
    fun withdraw(
        @Valid @RequestBody body: WithdrawRequest
    ): WithdrawResponse {
        val userId = SecurityContextHolder.getContext().authentication?.principal as? UserId
            ?: throw InvalidTokenException()

        return transactionService.withdraw(
            userId = userId,
            fromAccountId = body.fromAccountId,
            amount = body.amount
        ).toWithdrawResponse()
    }

}