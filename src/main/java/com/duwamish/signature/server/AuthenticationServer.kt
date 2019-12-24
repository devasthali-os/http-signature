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

        @JvmStatic
        fun main(args: Array<String>) {

            val authHeader =
                    "Signature keyId=secret-key-alias," +
                            "algorithm=hmac-sha256," +
                            "headers=created," +
                            "signature=gLhSJlRX2i7TpEETQeq4a6Jp5Wo%2Bz2mEGwW0hLRgKZM%3D"

            getHttpSignature.getSignatureString(
                    "GET",
                    "/todos/1",
                    authHeader,
                    mapOf("created" to "Sat, 21 Dec 2019 22:52:30 PST")
            )
        }
    }
}
