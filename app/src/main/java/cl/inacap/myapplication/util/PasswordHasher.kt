package cl.inacap.myapplication.util

import android.util.Base64
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

/**
 * Cifrado de credenciales: las contraseñas nunca se guardan en texto plano.
 * PBKDF2-HMAC-SHA256 con sal aleatoria por usuario.
 * Requiere API 26 o superior (ver minSdk en build.gradle.kts).
 */
object PasswordHasher {
    private const val ITERACIONES = 10_000
    private const val LARGO_BITS = 256

    fun newSalt(): String {
        val bytes = ByteArray(16)
        SecureRandom().nextBytes(bytes)
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }

    fun hash(clave: String, salt: String): String {
        val spec = PBEKeySpec(clave.toCharArray(), Base64.decode(salt, Base64.NO_WRAP), ITERACIONES, LARGO_BITS)
        val bytes = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).encoded
        spec.clearPassword()
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }

    fun verify(clave: String, salt: String, hashEsperado: String): Boolean =
        MessageDigest.isEqual(hash(clave, salt).toByteArray(), hashEsperado.toByteArray())
}
