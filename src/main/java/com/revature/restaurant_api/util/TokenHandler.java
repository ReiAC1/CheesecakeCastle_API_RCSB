package com.revature.restaurant_api.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class JWTHandler {

    private static JWTHandler jwtHandler;

    private JWTHandler() { }

    public static JWTHandler getInstance() {
        if (jwtHandler == null)
            jwtHandler = new JWTHandler();

        return jwtHandler;
    }

    private String encodeHS256(String header, String body) {
        // first we need to encode into base64
        String encoded = Base64.getEncoder().encodeToString((header + "." + body).getBytes(StandardCharsets.UTF_8));

        // next we need to use a HMAC encryption on the encoded string
        String key = "testKey";
        
    }

    public String encodeJSON(String jsonObject) {

        // this is our standard header
        String header = String.format("{%n  %s: %s,%n  %s: %s%n}",
                "\"typ\"", "\"JWT\"",
                "\"alg\"", "\"HS256\"");



        return null;
    }
}
