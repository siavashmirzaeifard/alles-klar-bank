package co.alles_klar.bank.utility.encoder

import co.alles_klar.bank.utility.exception.InvalidPasswordException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class PasswordEncoder {

    private val bCryptPasswordEncoder = BCryptPasswordEncoder()

    fun encode(rawPassword: String): String {
        return bCryptPasswordEncoder.encode(rawPassword)
            ?: throw InvalidPasswordException()
    }

    fun matches(
        rawPassword: String,
        encodedPassword: String
    ): Boolean {
        return bCryptPasswordEncoder.matches(
            rawPassword,
            encodedPassword
        )
    }

}