package com.volleyballlondon.dev;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;

public abstract class VolleyballServiceTest {

    protected static final String EMPTY_STRING = "";

    /**
     * Asserts the expected status and the expected message in the actual
     * JSON response
     * Fails the test if JSON response cannot be parsed
     */
    protected void assertJsonResponse(Boolean expectedStatus, String expectedMessage,
        String actualJsonResponse) {
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(actualJsonResponse);
            Assert.assertEquals("Incorrect Message", expectedMessage,
                (String)jsonObject.get(VolleyballService.JSON_MESSAGE));
            Assert.assertEquals("Incorrect Status", expectedStatus,
                (Boolean)jsonObject.get(VolleyballService.JSON_STATUS));
        } catch (ParseException e) {
            System.out.println("position: " + e.getPosition());
            System.out.println(e);
            Assert.fail("Unable to parse json response.");
        }
    }

    /**
     * Concatenates the expected message1 and message2 into the message value
     * before asserting the JSON response
     */
    protected void assertJsonResponse(Boolean expectedStatus, String expectedMessage1,
        String expectedMessage2, String actualJsonResponse) {
        assertJsonResponse(expectedStatus, expectedMessage1 + expectedMessage2,
            actualJsonResponse);
    }

}
