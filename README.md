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
## with v1
====================GET==================
Signature keyId="secret-key-alias",algorithm="hmac-sha256",headers="created",signature="gLhSJlRX2i7TpEETQeq4a6Jp5Wo+z2mEGwW0hLRgKZM="
======================POST===================
Signature keyId="secret-key-alias",algorithm="hmac-sha256",headers="host created content-type digest accept content-length",signature="9DbJz/JbPWzM0U03BGfuWzkiKk1KHngTZjvYI7gcGMw="


##with v2
====================GET==================
Signature keyId="secret-key-alias",algorithm="hmac-sha256",headers="created",signature="gLhSJlRX2i7TpEETQeq4a6Jp5Wo+z2mEGwW0hLRgKZM="
======================POST===================
Signature keyId="secret-key-alias",algorithm="hmac-sha256",headers="host created content-type digest accept content-length",signature="9DbJz/JbPWzM0U03BGfuWzkiKk1KHngTZjvYI7gcGMw="

```

references
--

- https://github.com/tomitribe/http-signatures-java
- https://github.com/joyent/node-http-signature/blob/master/http_signing.md
