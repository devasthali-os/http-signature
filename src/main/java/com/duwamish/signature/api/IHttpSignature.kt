package com.duwamish.signature.api

public interface IHttpSignature {

    fun createAuthenticationHeader(
            method: String,
            host: String,
            uri: String,
            digest: String?,
            created: String,
            contentType: String,
            acceptContent: String,
            contentLength: Int): String
}
