package com.duwamish

import org.tomitribe.auth.signatures.Signature
import org.tomitribe.auth.signatures.Signer
import java.security.Key
import java.time.OffsetDateTime
import java.util.*
import javax.crypto.spec.SecretKeySpec

public class HttpSignature(keyId: String,
                           algorithm: String,
                           partOfSignatureHeaders: List<String>,
                           symmetricPassword: String,
                           symmetricAlgo: String) {

    val signature = Signature(
            keyId,
            algorithm,
            null,
            partOfSignatureHeaders
    )

    val httpSigner = Signer(
            SecretKeySpec(symmetricPassword.toByteArray(), algorithm),
            signature
    )

    fun createAuthenticationHeader(
            method: String,
            host: String,
            uri: String,
            digest: String,
            created: OffsetDateTime,
            contentType: String,
            acceptContent: String,
            contentLength: Int): String {

        val partOfSignature: MutableMap<String, String> = HashMap()
        partOfSignature["Host"] = host
        partOfSignature["Created"] = created.toString()
        partOfSignature["Content-Type"] = contentType
        partOfSignature["Digest"] = digest
        partOfSignature["Accept"] = acceptContent
        partOfSignature["Content-Length"] = contentLength.toString()

        val authorizationHeader = httpSigner.sign(method, uri, partOfSignature)

        return authorizationHeader.toString()
    }
}
