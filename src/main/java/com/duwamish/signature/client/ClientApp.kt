package com.duwamish.signature.client

import com.duwamish.signature.HttpSignature
import java.security.MessageDigest
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class ClientApp {

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

        val dd_MMM_yyyy = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss zzz")

        private val digest = MessageDigest.getInstance("SHA-256").digest("{}".toByteArray())
        private val payloadDigest = "SHA-256=" + String(Base64.getEncoder().encode(digest))

        @JvmStatic
        fun main(args: Array<String>) {

            val getAuthenticationHeader = httpSignature.createAuthenticationHeader(
                    "GET",
                    "jsonplaceholder.typicode.com",
                    "/todos/1",
                    payloadDigest,
                    ZonedDateTime.now().format(dd_MMM_yyyy),
                    "application/json",
                    "*/*",
                    10
            )

            println(getAuthenticationHeader)
        }

    }
}
