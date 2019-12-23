package com.duwamish.signature.api

import org.tomitribe.auth.signatures.Signature
import org.tomitribe.auth.signatures.Signer
import java.util.*
import javax.crypto.spec.SecretKeySpec

public class HttpSignature(keyId: String,
                           algorithm: String,
                           partOfSignatureHeaders: List<String>,
                           symmetricPassword: String,
                           symmetricAlgo: String): IHttpSignature {

    private val signature = Signature(
            keyId,
            algorithm,
            null,
            partOfSignatureHeaders
    )

    private val httpSigner = Signer(
            SecretKeySpec(symmetricPassword.toByteArray(), algorithm),
            signature
    )

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
        if(digest != null) {
            partOfSignature["Digest"] = digest
        }
        partOfSignature["Accept"] = acceptContent
        partOfSignature["Content-Length"] = contentLength.toString()

        val authorizationHeader = httpSigner.sign(method, uri, partOfSignature)

        return authorizationHeader.toString()
    }
}
