package privatebinPaste

import org.bitcoinj.core.Base58
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec
import javax.net.ssl.HttpsURLConnection

/**
 * @author zp4rker
 */

fun main(args: Array<String>) {
    paste(args.joinToString(" "))
}

fun paste(contents: String) {
    val url = "https://paste.zp4rker.com"

    // password
    val keyGen = KeyGenerator.getInstance("AES").also { it.init(192) }
    val pwd = Base64.getEncoder().encode(keyGen.generateKey().encoded)

    // iv
    val ivBytes = ByteArray(16)
    Random().nextBytes(ivBytes)
    val iv = Base64.getEncoder().encode(ivBytes)

    // salt
    val saltBytes = ByteArray(8)
    Random().nextBytes(saltBytes)
    val salt = Base64.getEncoder().encode(saltBytes)

    // message
    val pasteData = JSONObject(mapOf("paste" to contents))
    val pasteBytes = pasteData.toString().toByteArray()

    // secret
    val secretFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
    val keySpec = PBEKeySpec(String(pwd).toCharArray(), saltBytes, 100000, 256)
    val secret = SecretKeySpec(secretFactory.generateSecret(keySpec).encoded, "AES")

    // adata
    val adataArray = JSONArray().apply {
        put(JSONArray(arrayOf(String(iv), String(salt), 100000, 256, 128, "aes", "gcm", "none")))
        put("plaintext")
        put(0)
        put(0)
    }

    // cipher
    val cipher = Cipher.getInstance("AES/GCM/NoPadding")
    val cipherSpec = GCMParameterSpec(128, ivBytes)
    cipher.init(Cipher.ENCRYPT_MODE, secret, cipherSpec)
    cipher.updateAAD(adataArray.toString().toByteArray())
    val cipherBytes = cipher.doFinal(pasteBytes)
    val cipherText = Base64.getEncoder().encodeToString(cipherBytes)

    // payload
    val payload = JSONObject().apply {
        put("v", 2)
        put("adata", adataArray)
        put("ct", cipherText)
        put("meta", JSONObject(mapOf("expire" to "never")))
    }

    val con = URL(url).openConnection() as HttpsURLConnection
    with(con) {
        requestMethod = "POST"
        doOutput = true
        setRequestProperty("X-Requested-With", "JSONHttpRequest")
        outputStream.write(payload.toString().toByteArray())

        val responseCode = responseCode
        println("Response code: $responseCode")
        if (responseCode == 200) {
            val response = JSONObject(String(inputStream.readBytes()))
            if (response.getInt("status") == 0) {
                val pasteUrl = response.getString("url")
                val deleteToken = response.getString("deletetoken")
                val finalUrl = "$url$pasteUrl#${Base58.encode(pwd)}"
                val deleteUrl = "$url$pasteUrl&deletetoken=$deleteToken"
                println("Paste URL: $finalUrl")
                println("Delete URL: $deleteUrl")
            } else {
                println(response.get("message"))
            }
        }
    }
}