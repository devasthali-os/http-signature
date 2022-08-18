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
- https://github.com/tomitribe/http-signatures-java
- https://github.com/joyent/node-http-signature/blob/master/http_signing.md
