package co.alles_klar.bank.user.database.repository

import co.alles_klar.bank.user.database.entity.UserEntity
import co.alles_klar.bank.utility.constant.UserId
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<UserEntity, UserId> {

    fun findByUsernameOrEmail(username: String, email: String): UserEntity?
    fun findByUsername(username: String): UserEntity?

}