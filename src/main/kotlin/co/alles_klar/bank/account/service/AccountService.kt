package co.alles_klar.bank.account.service

import co.alles_klar.bank.account.database.entity.AccountEntity
import co.alles_klar.bank.account.database.mapper.toDomain
import co.alles_klar.bank.account.database.repository.AccountRepository
import co.alles_klar.bank.account.domain.model.Account
import co.alles_klar.bank.user.database.entity.UserEntity
import co.alles_klar.bank.utility.constant.AccountId
import co.alles_klar.bank.utility.constant.AccountTypes
import co.alles_klar.bank.utility.constant.UserId
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.Lock
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import java.util.Optional

@Service
class AccountService(
    private val accountRepository: AccountRepository
) {

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Transactional(rollbackFor = [Exception::class])
    fun create(
        accountName: String,
        user: UserEntity,
        balance: Double,
        accountType: AccountTypes
    ): Account {
        return accountRepository.saveAndFlush(
            AccountEntity(
                accountName = accountName,
                user = user,
                balance = balance,
                accountType = accountType
            )
        ).toDomain()
    }

    fun getAll(userId: UserId): Set<Account> {
        return accountRepository.findByUserId(userId = userId).map { it.toDomain() }.toSet()
    }

    fun getAccount(accountId: AccountId): Optional<AccountEntity> {
        return accountRepository.findById(accountId)
    }

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Transactional(
        rollbackFor = [Exception::class],
        isolation = Isolation.SERIALIZABLE
    )
    fun updateAccount(account: AccountEntity) {
        accountRepository.saveAndFlush(account)
    }

}