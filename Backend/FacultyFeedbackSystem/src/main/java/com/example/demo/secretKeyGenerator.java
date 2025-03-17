package com.example.demo;

import java.util.Base64;

import io.jsonwebtoken.security.Keys;

public class secretKeyGenerator {
	public static void main(String[] args) {
        String key = Base64.getEncoder().encodeToString(Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256).getEncoded());
        System.out.println("Generated Secret Key: " + key);
    }
}
