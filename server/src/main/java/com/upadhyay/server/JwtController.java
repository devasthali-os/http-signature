package com.upadhyay.server;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class JwtController {

    private static final String SECRET_KEY = "upadhyay";

    @GetMapping("/decode-jwt")
    public String decodeJwt(@RequestHeader("Authorization") String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            DecodedJWT jwt = JWT.require(algorithm).build().verify(token.replace("Bearer ", ""));
            String userId = jwt.getClaim("user_id").asString();
            String permissions = jwt.getClaim("permissions").asList(String.class).toString();
            return "User ID: " + userId + ", Permissions: " + permissions;
        } catch (JWTDecodeException exception) {
            return "Invalid token";
        }
    }
}
