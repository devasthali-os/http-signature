http message authentication
--------------------

This information can be verified and trusted because it is digitally signed. 
JWTs can be signed using a secret (with the HMAC algorithm) or a public/private key pair using RSA or ECDSA.

**Authorization:** 
This is the most common scenario for using JWT. 
Once the user is logged in, each subsequent request will include the JWT, 
allowing the user to access routes, services, and resources that are permitted 
with that token.

**Information Exchange:** 
JSON Web Tokens are a good way of securely transmitting information between parties. 
Because JWTs can be signed — for example, using public/private key pairs — you can be sure the senders 
are who they say they are. Additionally, as the signature is calculated using the header and the payload, 
you can also verify that the content hasn't been tampered with.

example: https://jwt.io/#debugger-io
```bash
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
