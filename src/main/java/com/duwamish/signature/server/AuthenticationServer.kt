package com.duwamish.signature.server

import com.duwamish.signature.api.HttpSignatureV2

public class AuthenticationServer {

    companion object {

        val getHttpSignature = HttpSignatureV2(
                "secret-key-alias",
                "hmac-sha256",
                listOf(
                        "Created"
                ),
                "symmetric-password",
                "HmacSHA256"
        )

        fun authenticate(method: String,
                         uri: String,
                         authHeader: String,
                         requestHeaders: Map<String, String>) {
            val authHeaders = authHeader
                    .split("Signature ")[1]
                    .split(",")
                    .map { k ->
                        val kv = k.split("=")
                        mapOf(kv[0] to kv[1])

                    }.fold(emptyMap<String, String>()) { acc, elem -> acc.plus(elem) }

            val signingHeaders = authHeaders["headers"]!!
            val signatureKey= authHeaders["keyId"]
            val expectedSignature = authHeaders["signature"]

            val signingValue = requestHeaders[signingHeaders]!!

            //
            val actualSignature = getHttpSignature.signature(method, uri, requestHeaders)

            println(actualSignature)
            println(expectedSignature)
            println(actualSignature == expectedSignature)
        }

        @JvmStatic
        fun main(args: Array<String>) {

            val authHeader =
                    "Signature keyId=secret-key-alias," +
                            "algorithm=hmac-sha256," +
                            "headers=created," +
                            "signature=gLhSJlRX2i7TpEETQeq4a6Jp5Wo+z2mEGwW0hLRgKZM="

            authenticate(
                    "GET",
                    "/todos/1",
                    authHeader,
                    mapOf("created" to  "Sat, 21 Dec 2019 22:52:30 PST")
            )
        }
    }
}
