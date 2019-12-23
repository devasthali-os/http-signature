package com.duwamish.signature.api

import java.io.IOException
import java.security.Key
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

public class HttpSignatureV2(val keyId: String,
                             val algorithm: String,
                             val partOfSignatureHeaderDefs: List<String>,
                             val symmetricPassword: String,
                             val symmetricAlgo: String) : IHttpSignature {

    val key: Key = SecretKeySpec(symmetricPassword.toByteArray(), algorithm)

    override fun createAuthenticationHeader(
            method: String,
            host: String,
            uri: String,
            digest: String?,
            created: String,
            contentType: String,
            acceptContent: String,
            contentLength: Int): String {

        val partOfSignature: MutableMap<String, String> = HashMap()
        partOfSignature["Host"] = host
        partOfSignature["Created"] = created
        partOfSignature["Content-Type"] = contentType
        if (digest != null) {
            partOfSignature["Digest"] = digest
        }
        partOfSignature["Accept"] = acceptContent
        partOfSignature["Content-Length"] = contentLength.toString()

        val authorizationHeader = sign(method, uri, partOfSignature)

        return authorizationHeader
    }

    @Throws(IOException::class)
    fun sign(method: String, uri: String, headerValues: Map<String, String>): String {
        val signingString = createSigningString(partOfSignatureHeaderDefs.map { h -> h.toLowerCase() }, method, uri, headerValues)
        val binarySignature: ByteArray = sign(signingString.toByteArray())
        val encoded: ByteArray = Base64.getEncoder().encode(binarySignature)
        val signedAndEncodedString = String(encoded, Charsets.UTF_8)
        return signatureString(signedAndEncodedString)
    }

    fun sign(signingStringBytes: ByteArray?): ByteArray {
        try {
            val mac: Mac = Mac.getInstance(symmetricAlgo)
            mac.init(key);
            return mac.doFinal(signingStringBytes);
        } catch (e: NoSuchAlgorithmException) {
            throw IllegalArgumentException("HmacSHA1")
        } catch (e: Exception) {
            throw IllegalStateException(e)
        }
    }

    @Throws(IOException::class)
    fun createSigningString(requiredDefs: List<String>, method: String, uri: String?, headerValues: Map<String, String>): String {
        var method = lowercase(method)
        var headers = lowercase(headerValues)

        val list: MutableList<String?> = ArrayList(requiredDefs.size)
        for (keyDef in requiredDefs) {
            if ("(request-target)" == keyDef) {
                list.add(join(" ", "(request-target):", method, uri))
            } else {
                val value = headers[keyDef] ?: throw IllegalArgumentException(keyDef)
                list.add("$keyDef: $value")
            }
        }
        return join("\n", list)
    }

    private fun lowercase(headers: Map<String, String>): Map<String, String> {
        val map: MutableMap<String, String> = HashMap()
        for ((key1, value) in headers) {
            map[key1.toLowerCase()] = value
        }
        return map
    }

    private fun lowercase(spec: String): String {
        return spec.toLowerCase()
    }

    fun signatureString(signature: String): String {
        return "Signature " +
                "keyId=\"" + keyId + '\"' +
                ",algorithm=\"" + algorithm + '\"' +
                ",headers=\"" + join(" ", partOfSignatureHeaderDefs.map { h -> h.toLowerCase() }) + '\"' +
                ",signature=\"" + signature + '\"'
    }

    fun join(delimiter: String, collection: Collection<*>): String {
        if (collection.isEmpty()) return ""
        val sb = StringBuilder()
        for (obj in collection) {
            sb.append(obj).append(delimiter)
        }
        return sb.substring(0, sb.length - delimiter.length)
    }

    fun join(delimiter: String, vararg collection: Any?): String? {
        if (collection.isEmpty()) return ""
        val sb = java.lang.StringBuilder()
        for (obj in collection) {
            sb.append(obj).append(delimiter)
        }
        return sb.substring(0, sb.length - delimiter.length)
    }
}
