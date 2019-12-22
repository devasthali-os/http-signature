http message authentication
--------------------

GET
---

```bash
curl -H "Authorization: Signature keyId=secret-key-alias,algorithm=hmac-sha256,headers=host created content-type accept,signature=zywGZpc5Wqps17UwwcRTcfyasknxAavN3Grhx5co0Kw=" \
   -H "Host: jsonplaceholder.typicode.com" \
   -H "Created: 2019-12-21T12:37:55.553325-08:00" \
   -H "Content-Type: application/json" \
   -H "Digest: " \ 
   -H "Accept: */*" \
   -H "Content-Length: 10" \
  "jsonplaceholder.typicode.com/todos/1"
```

POST/ PUT/ PATCH/
----

```bash
curl --request POST \
   -H "Authorization: Signature keyId=secret-key-alias,algorithm=hmac-sha256,headers=host created content-type digest accept content-length,signature=zywGZpc5Wqps17UwwcRTcfyasknxAavN3Grhx5co0Kw=" \
   -H "Host: jsonplaceholder.typicode.com" \
   -H "Created: 2019-12-21T12:37:55.553325-08:00" \
   -H "Content-Type: application/json" \
   -H "Digest: " \
   -H "Accept: */*" \
   -H "Content-Length: 10" \
   -d '{}'
  "jsonplaceholder.typicode.com/todos"
```

references
--

- https://github.com/tomitribe/http-signatures-java
- https://github.com/joyent/node-http-signature/blob/master/http_signing.md
