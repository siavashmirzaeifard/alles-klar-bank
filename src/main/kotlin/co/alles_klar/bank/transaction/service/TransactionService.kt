package co.alles_klar.bank.transaction.service

import co.alles_klar.bank.account.database.mapper.toDomain
import co.alles_klar.bank.account.service.AccountService
import co.alles_klar.bank.transaction.database.entity.TransactionEntity
import co.alles_klar.bank.transaction.database.mapper.toDomain
import co.alles_klar.bank.transaction.database.repository.TransactionRepository
import co.alles_klar.bank.transaction.domain.model.Transaction
import co.alles_klar.bank.utility.constant.AccountId
import co.alles_klar.bank.utility.constant.TransactionTypes
import co.alles_klar.bank.utility.constant.UserId
import co.alles_klar.bank.utility.exception.AccountNotFoundException
import co.alles_klar.bank.utility.exception.InsufficientMoneyException
import co.alles_klar.bank.utility.exception.InvalidTransactionException
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.Lock
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional

@Service
class TransactionService(
    private val transactionRepository: TransactionRepository,
    private val accountService: AccountService
) {

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Transactional(
        rollbackFor = [Exception::class],
        isolation = Isolation.SERIALIZABLE
    )
    fun transfer(
        userId: UserId,
        amount: Double,
        fromAccountId: UserId,
        toAccountId: AccountId
    ): Transaction {
        val accounts = accountService.getAll(userId = userId)

        val fromAccount = accountService.getAccount(accountId = fromAccountId)
            .orElseThrow { AccountNotFoundException() }

        val toAccount = accountService.getAccount(accountId = toAccountId)
            .orElseThrow { AccountNotFoundException() }

        if (!accounts.contains(fromAccount.toDomain())) {
            throw InvalidTransactionException(message = "Source account does not belong to you.")
        }

        if (fromAccount.balance < amount) {
            throw InsufficientMoneyException()
        }

        return fromAccount.id?.let { fromAccountId ->
            toAccount.id?.let { toAccountId ->
                accountService.updateAccount(
                    account = fromAccount.apply {
                        this.balance -= amount
                    }
                )

                accountService.updateAccount(
                    account = toAccount.apply {
                        this.balance += amount
                    }
                )

                transactionRepository.saveAndFlush(
                    TransactionEntity(
                        fromAccountId = fromAccountId,
                        toAccountId = toAccountId,
                        amount = amount,
                        transactionType = TransactionTypes.TRANSFER
                    )
                ).toDomain()
            } ?: throw AccountNotFoundException()
        } ?: throw AccountNotFoundException()
    }

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Transactional(
        rollbackFor = [Exception::class],
        isolation = Isolation.SERIALIZABLE
    )
    fun withdraw(
        userId: UserId,
        fromAccountId: AccountId,
        amount: Double
    ): Transaction {
        val accounts = accountService.getAll(userId = userId)
        val account = accountService.getAccount(accountId = fromAccountId)
            .orElseThrow { AccountNotFoundException() }

        if (!accounts.contains(account.toDomain())) {
            throw InvalidTransactionException(message = "This account does not belong to you.")
        }

        if (account.balance < amount) {
            throw InsufficientMoneyException()
        }

        return account.id?.let {
            accountService.updateAccount(
                account = account.apply {
                    this.balance -= amount
                }
            )

            transactionRepository.saveAndFlush(
                TransactionEntity(
                    fromAccountId = it,
                    amount = amount,
                    transactionType = TransactionTypes.WITHDRAW
                )
            ).toDomain()
        } ?: throw AccountNotFoundException()
    }

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Transactional(
        rollbackFor = [Exception::class],
        isolation = Isolation.SERIALIZABLE
    )
    fun deposit(
        toAccountId: AccountId,
        amount: Double
    ): Transaction {
        val account = accountService.getAccount(accountId = toAccountId)
            .orElseThrow { AccountNotFoundException() }

        return account.id?.let {
            accountService.updateAccount(
                account = account.apply {
                    this.balance += amount
                }
            )

            transactionRepository.saveAndFlush(
                TransactionEntity(
                    toAccountId = it,
                    amount = amount,
                    transactionType = TransactionTypes.DEPOSIT
                )
            ).toDomain()
        } ?: throw AccountNotFoundException()
    }

}