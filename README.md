http message authentication
--------------------

```bash
curl -H "Authorization: Signature keyId=key-alias,algorithm=hmac-sha256,headers=host created content-type digest accept content-length,signature=zywGZpc5Wqps17UwwcRTcfyasknxAavN3Grhx5co0Kw=" \
   -H "Host: jsonplaceholder.typicode.com" \
   -H "Created: 2019-12-21T12:37:55.553325-08:00" \
   -H "Content-Type: application/json" \
   -H "Digest: " \ 
   -H "Accept: */*" \
   -H "Content-Length: 10" \
  "jsonplaceholder.typicode.com/todos/1"
```

references
--

- https://github.com/tomitribe/http-signatures-java
- https://github.com/joyent/node-http-signature/blob/master/http_signing.md
