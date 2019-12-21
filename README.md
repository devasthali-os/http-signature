http message authentication
--------------------

- https://github.com/joyent/node-http-signature/blob/master/http_signing.md


```java
curl -H "Signature keyId=key-alias,algorithm=hmac-sha256,headers=host created content-type digest accept content-length,signature=zywGZpc5Wqps17UwwcRTcfyasknxAavN3Grhx5co0Kw=" \ " +
   -H "Host: jsonplaceholder.typicode.com",
   -H "Created: 2019-12-21T12:37:55.553325-08:00",
   -H ""
  jsonplaceholder.typicode.com/todos/1
```