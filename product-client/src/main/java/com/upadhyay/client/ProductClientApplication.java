package com.upadhyay.client;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Date;

@SpringBootApplication
public class ProductClientApplication implements CommandLineRunner {

    private static final String SECRET_KEY = "upadhyay";

    private final WebClient webClient;

    public ProductClientApplication() {
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:8080")
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(ProductClientApplication.class, args);
    }

    @Override
    public void run(String... args) {
        String token = permissionToken("upadhyay", new String[]{
                "CAN_ADD_PRODUCT",
                "CAN_RETRIEVE_PRODUCT",
                "CAN_EDIT_PRODUCT",
                "CAN_DELETE_PRODUCT"
        });

        String response = webClient.get()
                .uri("/api/decode-jwt")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        System.out.println("Response from /decode-jwt: " + response);

        String addProductResponse = webClient.get().uri("/api/add-product")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        System.out.println("Response from /add-product: " + addProductResponse);

        String editProductResponse = webClient.get().uri("/api/edit-product")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        System.out.println("Response from /edit-product: " + editProductResponse);

        String deleteProductResponse = webClient.delete().uri("/api/delete-product")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        System.out.println("Response from /delete-product: " + deleteProductResponse);
    }

    public static String permissionToken(String userId, String[] permissions) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        return JWT.create().withClaim("user_id", userId)
                .withArrayClaim("permissions", permissions)
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600 * 1000)) // 1 hour expiration
                .sign(algorithm);
    }
}
