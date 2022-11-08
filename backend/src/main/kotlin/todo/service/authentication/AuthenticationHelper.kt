package todo.service.authentication

import java.security.MessageDigest
import java.util.Base64

class AuthenticationHelper {
    private val salt = "08387290"

    fun computePasswordHash(password: String): String {
        val messageDigest = MessageDigest.getInstance("SHA-256")
        val saltedPassword = salt + password
        val encodedSaltedPassword = saltedPassword.toByteArray(Charsets.UTF_8)
        val bytes = messageDigest.digest(encodedSaltedPassword)
        return Base64.getUrlEncoder().encodeToString(bytes)
    }
}
