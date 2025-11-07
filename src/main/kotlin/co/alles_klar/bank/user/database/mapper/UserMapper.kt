package co.alles_klar.bank.user.database.mapper

import co.alles_klar.bank.user.database.entity.UserEntity
import co.alles_klar.bank.user.domain.model.User

fun UserEntity.toDomain(): User {
    return User(
        id = this.id!!,
        fullName = this.firstName + " " + this.lastName,
        username = this.username,
        email = this.email,
        age = this.age,
//        defaultAccountId = this.defaultAccountId
        createdAt = this.createdAt
    )
}