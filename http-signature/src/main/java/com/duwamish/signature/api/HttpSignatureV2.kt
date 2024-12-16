package com.duwamish.signature.api

import java.io.IOException
import java.net.URLDecoder
import java.net.URLEncoder
import java.security.Key
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

public class HttpSignatureV2(private val symmetricKeyId: String,
                             private val algorithm: String,
                             private val partOfSignatureHeaderDefs: List<String>,
                             private val symmetricAlgo: String) : IHttpSignature {

    private val symmetricKey: Key = SecretKeySpec(symmetricKeyId.toByteArray(), algorithm)

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

        return signatureHeader(method, uri, partOfSignature)
    }

    private fun signatureHeader(method: String,
                                uri: String,
                                headerValues: Map<String, String>): String {
        return signatureString(createSignature(method, uri, headerValues))
    }

    fun createSignature(method: String,
                        uri: String,
                        headerValues: Map<String, String>): String {
        val signingString = createSigningString(
                partOfSignatureHeaderDefs.map { h -> h.toLowerCase() }, method, uri, headerValues)
        println("signing string: \n" + signingString)
        val binarySignature: ByteArray = createHash(signingString.toByteArray())
        val encoded: ByteArray = Base64.getEncoder().encode(binarySignature)
        val signedAndEncodedString = String(encoded, Charsets.UTF_8)
        return URLEncoder.encode(signedAndEncodedString)
    }

    private fun createHash(signingStringBytes: ByteArray?): ByteArray {
        try {
            val mac: Mac = Mac.getInstance(symmetricAlgo)
            mac.init(symmetricKey);
            return mac.doFinal(signingStringBytes);
        } catch (e: NoSuchAlgorithmException) {
            throw IllegalArgumentException(symmetricAlgo)
        } catch (e: Exception) {
            throw IllegalStateException(e)
        }
    }

    @Throws(IOException::class)
    private fun createSigningString(requiredDefs: List<String>,
                                    method: String,
                                    uri: String?,
                                    headerValues: Map<String, String>): String {
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

    override fun getSignatureString(method: String,
                           uri: String,
                           authHeader: String,
                           requestHeaders: Map<String, String>): String {
        val authHeaders = authHeader
                .split("Signature ")[1]
                .split(",")
                .map { k ->
                    val kv = k.split("=")
                    mapOf(kv[0] to kv[1])

                }.fold(emptyMap<String, String>()) { acc, elem -> acc.plus(elem) }

        val signingHeaders = authHeaders["headers"]!!.split(" ")
        val signatureKey = authHeaders["keyId"]
        val expectedSignature = URLDecoder.decode(authHeaders["signature"])

        val signingValue = signingHeaders.map { h ->
            if (h == "(request-target)") {
                "$h:  $method $uri"
            } else {
                h + ": " + requestHeaders[h]!!
            }
        }.joinToString("\n")

        //
        val actualSignature = createSignature(method, uri, requestHeaders)

        return actualSignature
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
                "keyId=" + symmetricKeyId +
                ",algorithm=" + algorithm +
                ",headers=" + join(" ", partOfSignatureHeaderDefs.map { h -> h.toLowerCase() }) +
                ",signature=" + signature
    }

    private fun join(delimiter: String, collection: Collection<*>): String {
        if (collection.isEmpty()) return ""
        val sb = StringBuilder()
        for (obj in collection) {
            sb.append(obj).append(delimiter)
        }
        return sb.substring(0, sb.length - delimiter.length)
    }

    private fun join(delimiter: String, vararg collection: Any?): String? {
        if (collection.isEmpty()) return ""
        val sb = java.lang.StringBuilder()
        for (obj in collection) {
            sb.append(obj).append(delimiter)
        }
        return sb.substring(0, sb.length - delimiter.length)
    }
}
