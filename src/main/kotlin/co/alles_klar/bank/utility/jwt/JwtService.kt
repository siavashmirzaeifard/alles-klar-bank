package co.alles_klar.bank.utility.jwt

import co.alles_klar.bank.utility.constant.UserId
import co.alles_klar.bank.utility.exception.InvalidTokenException
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.Jwts.SIG
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.Base64
import java.util.Date
import java.util.UUID

@Service
class JwtService(
    @param:Value(value = "\${jwt.secret}") private val jwtSecretBase64: String,
    @param:Value(value = "\${jwt.expiry-minutes}")private val expiryInMinutes: Long
) {

    private val jwtSecret = Base64.getDecoder().decode(jwtSecretBase64)
    private val secret = Keys.hmacShaKeyFor(jwtSecret)
    private val accessTokenValidityMs: Long = expiryInMinutes * 60 * 1000L
    val refreshTokenValidityMs: Long = 30 * 24 * 60 * 60 * 1000L

    private fun generateToken(
        userId: UserId,
        type: String,
        expiry: Long
    ): String {
        val now = Date()

        return Jwts
            .builder()
            .subject(userId.toString())
            .claim("type", type)
            .issuedAt(now)
            .expiration(Date(now.time + expiry))
            .signWith(secret, SIG.HS256)
            .compact()
    }

    fun generateAccessToken(userId: UserId): String {
        return generateToken(
            userId = userId,
            type = "access",
            expiry = accessTokenValidityMs
        )
    }

    fun generateRefreshToken(userId: UserId): String {
        return generateToken(
            userId = userId,
            type = "refresh",
            expiry = refreshTokenValidityMs
        )
    }

    private fun parseAllClaims(token: String): Claims? {
        val rawToken = if (token.startsWith("Bearer ")) {
            token.removePrefix("Bearer ")
        } else token

        return try {
            Jwts
                .parser()
                .verifyWith(secret)
                .build()
                .parseSignedClaims(rawToken)
                .payload
        } catch (e: Exception) {
            null
        }
    }

    fun validateToken(
        token: String,
        type: String
    ): Boolean {
        val claims = parseAllClaims(token = token) ?: return false
        val tokenType = claims["type"] as? String ?: return false

        return tokenType == type
    }

    fun getUserIdFromToken(token: String): UserId {
        val claims = parseAllClaims(token = token)
            ?: throw InvalidTokenException()

        return UUID.fromString(claims.subject)
    }

}