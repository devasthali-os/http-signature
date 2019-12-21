package com.duwamish

import java.time.OffsetDateTime

class App {

    companion object {

        val httpSignature = HttpSignature(
                "secret-key-alias",
                "hmac-sha256",
                listOf(
                        "Host",
                        "Created",
                        "Content-Type",
                        "Digest",
                        "Accept",
                        "Content-Length"),
                "symmetric-password",
                "HmacSHA256"
        )

        @JvmStatic
        fun main(args: Array<String>) {
            val authenticationHeader = httpSignature.createAuthenticationHeader(
                    "GET",
                    "jsonplaceholder.typicode.com",
                    "/todos/1",
                    "SHA-256=X48E9qOokqqrvdts8nOJRJN3OWDUoyWxBf7kbu9DBPE=",
                    OffsetDateTime.now(),
                    "application/json",
                    "*/*",
                    10
            )

            println(authenticationHeader)
        }

    }
}
