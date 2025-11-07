package co.alles_klar.bank.utility.encoder

import org.springframework.stereotype.Component
import java.security.MessageDigest
import java.util.Base64

@Component
class TokenEncoder {

    private val messageDigest = MessageDigest.getInstance("SHA-256")

    fun encode(rawToken: String): String {
        val bytes = rawToken.toByteArray()
        val hashedBytes = messageDigest.digest(bytes)

        return Base64.getEncoder().encodeToString(hashedBytes)
    }

}