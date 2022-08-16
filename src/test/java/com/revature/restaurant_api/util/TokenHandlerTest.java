package com.revature.restaurant_api.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenHandlerTest {

    ObjectMapper oMapper;

    public TokenHandlerTest() {
        oMapper = new ObjectMapper();
        TokenHandler.setupInstance(oMapper);
    }

    @Test
    void encodeJSON() {
        TestStructure test = new TestStructure();
        test.name = "Rei";
        test.age = 25;

        String actual = "{\"id\":10,\"exp\":1660675679509}.AD537E38641EBEE54F1DF99EA0DE7068AF4A1AEE628DAB7D97D87EF238F3A739";

        try {
            String payload = TokenHandler.getInstance().encodeJSON(oMapper.writeValueAsString(test), 10, 1660675679509L);
            assertEquals(actual, payload);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void compareToken() {
        TestStructure test = new TestStructure();
        test.name = "Rei";
        test.age = 25;

        try {
            // Testing using a freshly generated token and comparing it with the same object values
            String input = TokenHandler.getInstance().encodeJSON(oMapper.writeValueAsString(test), 10);
            String testPayload = oMapper.writeValueAsString(test);
            assertEquals(true, TokenHandler.getInstance().isTokenValid(input, testPayload));

            // Testing against the above generated token, but with changed values
            test.age = 26;
            testPayload = oMapper.writeValueAsString(test);
            assertEquals(false, TokenHandler.getInstance().isTokenValid(input, testPayload));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            fail();
        }


    }

    private class TestStructure {
        String name;
        int age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}
