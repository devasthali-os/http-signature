package com.duwamish.signature.client

import com.duwamish.signature.HttpSignature
import java.security.MessageDigest
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class ClientApp {

    companion object {

        val getHttpSignature = HttpSignature(
                "secret-key-alias",
                "hmac-sha256",
                listOf(
                        "Created"
                ),
                "symmetric-password",
                "HmacSHA256"
        )

        val postHttpSignature = HttpSignature(
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

        val dd_MMM_yyyy = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss zzz")

        @JvmStatic
        fun main(args: Array<String>) {

            //
            //GET
            //
            val getAuthenticationHeader = getHttpSignature.createAuthenticationHeader(
                    "GET",
                    "jsonplaceholder.typicode.com",
                    "/todos/1",
                    null,
                    ZonedDateTime.now().format(dd_MMM_yyyy),
                    "application/json",
                    "*/*",
                    10
            )

            println("====================GET==================")
            println(getAuthenticationHeader)

            //
            //POST
            //
            val payload = """
                {
                  "a": "b"
                }
            """.trimIndent()

            val digest = MessageDigest.getInstance("SHA-256").digest(payload.toByteArray())
            val payloadDigest = "SHA-256=" + String(Base64.getEncoder().encode(digest))

            val postAuthenticationHeader = postHttpSignature.createAuthenticationHeader(
                    "POST",
                    "jsonplaceholder.typicode.com",
                    "/todos",
                    payloadDigest,
                    ZonedDateTime.now().format(dd_MMM_yyyy),
                    "application/json",
                    "*/*",
                    10
            )

            println("======================POST===================")
            println(postAuthenticationHeader)
        }

    }
}
