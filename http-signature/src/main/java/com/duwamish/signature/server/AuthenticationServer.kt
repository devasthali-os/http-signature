package com.duwamish.signature.server

import com.duwamish.signature.api.HttpSignatureV2

public class AuthenticationServer {

    companion object {

        val getHttpSignature = HttpSignatureV2(
                "secret-key-alias",
                "hmac-sha256",
                listOf(
                        "Created",
                        "(request-target)"
                ),
                "HmacSHA256"
        )

        @JvmStatic
        fun main(args: Array<String>) {

            val actualSignature = "sAWZGJTlI5%2FDqFPeNYPZDTFSQ981ZhrGfX92TSzBnfI%3D"

            val authHeader =
                    "Signature keyId=secret-key-alias," +
                            "algorithm=hmac-sha256," +
                            "headers=created (request-target)," +
                            "signature=" + actualSignature

            val expectedSignature = getHttpSignature.getSignatureString(
                    "GET",
                    "/todos/1",
                    authHeader,
                    mapOf("created" to "Sat, 21 Dec 2019 22:52:30 PST")
            )

            println(actualSignature)
            println(expectedSignature)
            println(actualSignature == expectedSignature)
        }
    }
}
