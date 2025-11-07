package co.alles_klar.bank.account.database.repository

import co.alles_klar.bank.account.database.entity.AccountEntity
import co.alles_klar.bank.user.database.entity.UserEntity
import co.alles_klar.bank.utility.constant.AccountId
import co.alles_klar.bank.utility.constant.UserId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface AccountRepository: JpaRepository<AccountEntity, AccountId> {

    fun findByUserId(userId: UserId): Set<AccountEntity>

    @Query(value = """
        SELECT a
        FROM AccountEntity a
        WHERE a.user = :user 
        AND a.accountType = CHECKING
    """)
    fun findByUserWhereAccountTypeIs(user: UserEntity): AccountEntity?

}