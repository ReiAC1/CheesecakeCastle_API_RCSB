package com.revature.restaurant_api.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.restaurant_api.users.UsersModel;
import com.revature.restaurant_api.users.UsersService;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

public class TokenHandler {

    private static TokenHandler tokenHandler;
    private ObjectMapper mapper;
    private UsersService usersService;

    private TokenHandler(ObjectMapper mapper, UsersService usersService){
        this.mapper = mapper;
        this.usersService = usersService;
    }

    public static void setupInstance(ObjectMapper mapper, UsersService usersService) {
        tokenHandler = new TokenHandler(mapper, usersService);
    }

    public static TokenHandler getInstance() {
        return tokenHandler;
    }


    // encodes our header and body into a SHA256 digest
    private String encodeSHA256(String header, String body) {
        // first we need to encode into base64
        String encoded = Base64.getEncoder().encodeToString((header + "." + body).getBytes(StandardCharsets.UTF_8));

        // next we will use SHA256 encoding to create a digest of our header + body + key
        // this will ensure that not only do we create a unique time-constrained key,
        // the secret key will also prevent others from creating a new token
        String key = "testKey";

        try
        {
            encoded += key;

            // Get java's built in SHA256 encryption
            java.security.MessageDigest sha256 = java.security.MessageDigest.getInstance("SHA-256");

            // convert the inputted string into an encrypted byte array
            byte[] digestArray = sha256.digest(encoded.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();

            // loop through the byte array
            for (byte b : digestArray)
            {
                // Since sometimes when you use toHexString you'll end up with padded numbers such as 0xFFFFFF82 instead
                // of 0x82, we ensure to only keep the important 0x82 by using the & operator on 0xFF. This ensures our
                // number is between 0 and 255, depending on what the last 8 bits are in the original byte.
                // Then additionally, we need to ensure the 9th bit is set to true
                // afterward we can grab the 2nd character in the substring, which then gives us the correct SHA256 byte

                sb.append(Integer.toHexString((b & 0xFF) | (1 << 9)).substring(1));
            }

            // Finally SHA256 outputs are usually in uppercase, so let's convert it
            return sb.toString().toUpperCase();
        }
        catch (java.security.NoSuchAlgorithmException e)
        {
            return null; // if there's an issue, return null
        }

    }


    // encodes the JSON using the current time as a reference for expiration
    // jsonObject | the object, in JSON fortmat, you wish to create a token for
    // id | the ID of the object
    // expiration of header is auto-generated
    // returns a token with a proper header
    public String encodeJSON(String jsonObject, int id) {
        return encodeJSON(jsonObject, id, Instant.now().toEpochMilli() + (3600 * 2 * 1000));
    }

    // encodes a JSON string using a predefined id and expiration
    // jsonObject | the object, in JSON format, you wish to create a token for
    // id | the ID of the object
    // expiration | the time, in millis, that the token expires
    // returns a token with a proper header
    public String encodeJSON(String jsonObject, int id, long expiration) {

        try {
            // this is our standard token, using an id, expiration, and token
            // expiring time is always 2 hours
            TokenHeader tHeader = new TokenHeader();
            tHeader.id = id;
            tHeader.exp = expiration;

            String header = mapper.writeValueAsString(tHeader);

            String encoded = encodeSHA256(header, jsonObject);

            if (encoded == null)
                return null;

            return String.format("%s.%s", header, encoded);
        } catch (JsonProcessingException e) {
            e.printStackTrace();

            return null;
        }

    }


    // compares a token with an actual JSON value
    // token | the header + SHA256 digest
    // actual | the actual object converted into a JSON string
    // returns true if the token is good
    // returns false otherwise
    public boolean isTokenValid(String token, String actual) {
        try {
            // get the headerJSON value by splitting by a period
            String headerJSON = token.split("\\.")[0];

            // next, turn the header into a readable object
            TokenHeader tokenHeader = mapper.readValue(headerJSON, TokenHeader.class);

            // if the token is expired, return false
            if (tokenHeader.exp <= Instant.now().toEpochMilli())
                return false;

            // get the expected, real encoded object by using the header's id and expiration
            String realEncoded = encodeJSON(actual, tokenHeader.id, tokenHeader.exp);

            // if our expected encoded token is equal to the inputted token, the token is valid
            return token.equals(realEncoded);

        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return false;
    }


    // Simple method for getting the authUser from a token digest
    // token | the full token of the authUser
    // returns null if invalid request or no user found
    // returns the associated UsersModel otherwise
    public UsersModel getAuthUser(String token) {
       try {
           // get the header of our token
           String headerS = token.split("\\.")[0];
           TokenHeader header = mapper.readValue(headerS, TokenHeader.class);

           // try to get the UsersModel associated with the header
           UsersModel uModel = usersService.getByID(header.id);

           // if we have no UsersModel, or the TokenHandler cannot validate token, give unauthorized error
           if (uModel == null || !isTokenValid(token, mapper.writeValueAsString(uModel))) {
               return null;
           }

           return uModel;
       } catch (JsonMappingException e) {
           e.printStackTrace();
       } catch (JsonProcessingException e) {
           e.printStackTrace();
       }

       return null;
    }
}
