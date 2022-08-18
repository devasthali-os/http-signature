http message authentication
--------------------

GET
---

```bash
curl -H "Authorization: Signature keyId=secret-key-alias,algorithm=hmac-sha256,headers=host created content-type accept content-length,signature=6hxh+dhLeoSq4sNlA5c2gPSeXqwG9mphV7ZfHD97ZPw=" \
   -H "Host: jsonplaceholder.typicode.com" \
   -H "Created: Sat, 21 Dec 2019 22:52:30 PST" \
   -H "Content-Type: application/json" \
   -H "Accept: */*" \
   -H "Content-Length: 0" \
  "jsonplaceholder.typicode.com/todos/1"
```

POST/ PUT/ PATCH/
----

```bash
curl --request POST \
   -H "Authorization: Signature keyId=secret-key-alias,algorithm=hmac-sha256,headers=host created content-type digest accept content-length,signature=zywGZpc5Wqps17UwwcRTcfyasknxAavN3Grhx5co0Kw=" \
   -H "Host: jsonplaceholder.typicode.com" \
   -H "Created: Sat, 21 Dec 2019 22:52:30 PST" \
   -H "Content-Type: application/json" \
   -H "Digest: " \
   -H "Accept: */*" \
   -H "Content-Length: 10" \
   -d '{}'
  "jsonplaceholder.typicode.com/todos"
```

run app
-

```bash
====================GET==================
signing string: 
created: Sat, 21 Dec 2019 22:52:30 PST
(request-target): get /todos/1

Signature keyId=secret-key-alias,algorithm=hmac-sha256,headers=created (request-target),signature=sAWZGJTlI5%2FDqFPeNYPZDTFSQ981ZhrGfX92TSzBnfI%3D
======================POST===================
signing string: 
host: jsonplaceholder.typicode.com
created: Sat, 21 Dec 2019 22:52:30 PST
content-type: application/json
digest: SHA-256=HkkPe9NEvIc2nQ9B99butuBE/CH7nFz3/0g9rVMPq9A=
accept: */*
content-length: 10
Signature keyId=secret-key-alias,algorithm=hmac-sha256,headers=host created content-type digest accept content-length,signature=4RCWHOdYIvtWy3mrGEXOGGCGGMyh1s4YXWh%2FDGgx0TQ%3D
```

references
--

- https://datatracker.ietf.org/doc/html/draft-cavage-http-signatures-01
- https://en.wikipedia.org/wiki/HMAC
- https://github.com/prayagupa/tls.kotlin/tree/master/client
- https://github.com/prayagupa/tls-python/tree/master/socket-client
- https://jwt.io/introduction
- - https://github.com/auth0/java-jwt
- 
```
This information can be verified and trusted because it is digitally signed. JWTs can be signed using a secret (with the HMAC algorithm) or a public/private key pair using RSA or ECDSA.

Authorization: This is the most common scenario for using JWT. Once the user is logged in, each subsequent request will include the JWT, allowing the user to access routes, services, and resources that are permitted with that token.

Information Exchange: 
JSON Web Tokens are a good way of securely transmitting information between parties. Because JWTs can be signed—for example, using public/private key pairs—you can be sure the senders are who they say they are. Additionally, as the signature is calculated using the header and the payload, you can also verify that the content hasn't been tampered with.

example: 
--header 
{
  "alg": "HS256",
  "typ": "JWT"
}

--data
{
  "name": "Prayag",
  "platform": "andoird",
  "sku": 1516239022
}

verify-signature: 
base64_encode(HMACSHA256(
  base64UrlEncode(header) + "." +
  base64UrlEncode(payload),
  prayag-key
))
```
- https://github.com/tomitribe/http-signatures-java
- https://github.com/joyent/node-http-signature/blob/master/http_signing.md
