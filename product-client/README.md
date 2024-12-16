Product client
--

This client calls product server with JWT permissions digitally signed using 
secret key.

```bash
2024-12-15 17:24:53.696  INFO 13281 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8081 (http) with context path ''
2024-12-15 17:24:53.707  INFO 13281 --- [           main] c.upadhyay.client.JwtClientApplication   : Started JwtClientApplication in 2.779 seconds (JVM running for 3.241)
Response from server: User ID: upadhyay, Permissions: [CAN_ADD_PRODUCT, CAN_EDIT_PRODUCT]
```
