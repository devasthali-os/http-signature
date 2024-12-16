package com.upadhyay.server;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.upadhyay.server.security.SecurityConfig.SECRET_KEY;

@RestController
@RequestMapping("/api")
public class ProductController {

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

    @GetMapping("/add-product")
    public ResponseEntity<String> addProduct(@RequestHeader("Authorization") String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            DecodedJWT jwt = JWT.require(algorithm).build().verify(token.replace("Bearer ", ""));
            List<String> permissions = jwt.getClaim("permissions").asList(String.class);
            if (permissions.contains("CAN_ADD_PRODUCT")) {
                return ResponseEntity.ok("Product added successfully!");
            } else {
                return ResponseEntity.status(403).body("Forbidden: Missing CAN_ADD_PRODUCT permission");
            }
        } catch (JWTDecodeException exception) {
            return ResponseEntity.status(401).body("Invalid token");
        }
    }

    @GetMapping("/edit-product")
    public ResponseEntity<String> editProduct() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principalUser = (User) authentication.getPrincipal();
        List<String> permissions = principalUser.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        if (permissions.contains("CAN_EDIT_PRODUCT")) {
            return ResponseEntity.ok("Product edited successfully!");
        } else {
            return ResponseEntity.status(403).body("Forbidden: Missing CAN_EDIT_PRODUCT permission");
        }
    }

    @PreAuthorize("hasAuthority('CAN_DELETE_PRODUCT')")
    @DeleteMapping("/delete-product")
    public ResponseEntity<String> deleteProduct() {
            return ResponseEntity.ok("Product deleted successfully!");
    }
}
