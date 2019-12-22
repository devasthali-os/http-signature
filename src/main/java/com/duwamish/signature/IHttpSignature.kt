package com.duwamish.signature

public interface IHttpSignature {

    fun createAuthenticationHeader(
            method: String,
            host: String,
            uri: String,
            digest: String,
            created: String,
            contentType: String,
            acceptContent: String,
            contentLength: Int): String
}
