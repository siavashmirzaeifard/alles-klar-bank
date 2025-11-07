package co.alles_klar.bank.transaction.api.dto

import co.alles_klar.bank.utility.constant.AccountId
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

data class DepositRequest(

    @field:NotNull
    @field:NotEmpty
    val toAccountId: AccountId,

    @field:Min(
        value = 0,
        message = "Amount should be greater than 0",
    )
    val amount: Double

)