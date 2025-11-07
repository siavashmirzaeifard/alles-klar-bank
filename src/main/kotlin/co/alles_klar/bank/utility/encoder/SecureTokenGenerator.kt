package co.alles_klar.bank.utility.encoder

import org.springframework.stereotype.Component
import java.security.SecureRandom
import java.util.Base64

@Component
class SecureTokenGenerator {

    private val secureRandom = SecureRandom()

    fun generate(): String {
        val bytes = ByteArray(32) { 0 }
        secureRandom.nextBytes(bytes)

        return Base64
            .getUrlEncoder()
            .withoutPadding()
            .encodeToString(bytes)
    }

}