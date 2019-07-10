package com.volleyballlondon.dev;


import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.volleyballlondon.exceptions.LeagueAlreadyExistsException;
import com.volleyballlondon.exceptions.LeagueInvalidCharactersException;
import com.volleyballlondon.exceptions.LeagueLengthException;
import com.volleyballlondon.persistence.model.League;
import com.volleyballlondon.persistence.services.LeagueDbService;

public class LeagueServiceTest {

    /** Jersey Web Service Client */
	private static Client client;; 

    /** URL for the create league service on the server */
    private static final String REST_SERVICE_URL =
        "http://localhost:8080/VolleyBallLeagueSystem/rest/LeagueService/leagues";

    private static final String EMPTY_STRING = "";

    {
        client = ClientBuilder.newClient();
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(LeagueDbService.class);
        ctx.refresh();
        leagueDbService = (LeagueDbService) ctx.getBean("mainBean");
    }
    
    private static LeagueDbService leagueDbService;
    
    /**
    * @see LeagueService#createLeagueByForm(String,HttpServletResponse)
    * @verifies create new league
    */
    @Test
    public void createLeagueByForm_shouldCreateNewLeague() throws Exception {
        long leagueCount = readLeagueCount();
        String newLeague = "Division 01";
        String jsonResponse = create(newLeague);

        assertNewLeague(newLeague);
        assertJsonResponse(LeagueService.JSON_STATUS_SUCCESS,
            LeagueService.JSON_MESSAGE_SUCCESS, jsonResponse);
        assertLeagueCount(leagueCount + 1);
    }

    /**
     * @see LeagueService#createLeagueByForm(String,HttpServletResponse)
     * @verifies create new league with all valid characters
     */
    @Test
    public void createLeagueByForm_shouldCreateNewLeagueWithAllValidCharacters() throws Exception {
        long leagueCount = readLeagueCount();
        String newLeague = "Men's Division 02 - (North)";
        String jsonResponse = create(newLeague);

        assertNewLeague(newLeague);
        assertJsonResponse(LeagueService.JSON_STATUS_SUCCESS,
            LeagueService.JSON_MESSAGE_SUCCESS, jsonResponse);
        assertLeagueCount(leagueCount + 1);
    }

    /**
     * @see LeagueService#createLeagueByForm(String,HttpServletResponse)
     * @verifies fail if league already exists
     */
    @Test
    public void createLeagueByForm_shouldFailIfLeagueAlreadyExists() throws Exception {
        long leagueCount = readLeagueCount();
        String originalLeague = "Men's Division 03 - (South)";
        String newLeague = "Men's DIVISION 03 - (SOUTH)";
        String jsonResponse = create(newLeague);

        assertJsonResponse(LeagueService.JSON_STATUS_FAILURE,
            originalLeague, LeagueAlreadyExistsException.ERROR_MESSAGE,
            jsonResponse);
        assertLeagueCount(leagueCount);
    }

    /**
     * @see LeagueService#createLeagueByForm(String,HttpServletResponse)
     * @verifies fail given invalid character
     */
    @Test
    public void createLeagueByForm_shouldFailGivenInvalidCharacter() throws Exception {
        long leagueCount = readLeagueCount();
        String newLeague = "Men's Division 04; - (North)";
        String jsonResponse = create(newLeague);
        assertJsonResponse(LeagueService.JSON_STATUS_FAILURE,
            LeagueInvalidCharactersException.ERROR_MESSAGE,
            jsonResponse);
        assertLeagueCount(leagueCount);
    }

    /**
     * @see LeagueService#createLeagueByForm(String,HttpServletResponse)
     * @verifies fail given empty string
     */
    @Test
    public void createLeagueByForm_shouldFailGivenEmptyString() throws Exception {
        long leagueCount = readLeagueCount();
        String newLeague = EMPTY_STRING;
        String jsonResponse = create(newLeague);
        assertJsonResponse(LeagueService.JSON_STATUS_FAILURE,
            LeagueInvalidCharactersException.ERROR_MESSAGE,
            jsonResponse);
        assertLeagueCount(leagueCount);
    }

    /**
     * @see LeagueService#createLeagueByForm(String,HttpServletResponse)
     * @verifies fail given league name too long
     */
    @Test
    public void createLeagueByForm_shouldFailGivenLeagueNameTooLong() throws Exception {
        long leagueCount = readLeagueCount();
        String newLeague = "Men's Division 06 - (North - South)";
        String jsonResponse = create(newLeague);
        assertJsonResponse(LeagueService.JSON_STATUS_FAILURE,
            LeagueLengthException.ERROR_MESSAGE + newLeague.length() + ".",
            jsonResponse);
        assertLeagueCount(leagueCount);
    }

    /**
     * @see LeagueService#createLeagueByForm(String,HttpServletResponse)
     * @verifies fail given not begin with uppercase
     */
    @Test
    public void createLeagueByForm_shouldFailGivenNotBeginWithUppercase() throws Exception {
        long leagueCount = readLeagueCount();
        String newLeague = "men's Division 07";
        String jsonResponse = create(newLeague);
        assertJsonResponse(LeagueService.JSON_STATUS_FAILURE,
            LeagueInvalidCharactersException.ERROR_MESSAGE,
            jsonResponse);
        assertLeagueCount(leagueCount);
    }
    /**
     * Asserts the expected status and the expected message in the actual
     * JSON response
     * Fails the test if JSON response cannot be parsed
     */
    private void assertJsonResponse(Boolean expectedStatus, String expectedMessage,
        String actualJsonResponse) {
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(actualJsonResponse);
            Assert.assertEquals("Incorrect Message", expectedMessage,
                (String)jsonObject.get(LeagueService.JSON_MESSAGE));
            Assert.assertEquals("Incorrect Status", expectedStatus,
                (Boolean)jsonObject.get(LeagueService.JSON_STATUS));
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
    private void assertJsonResponse(Boolean expectedStatus, String expectedMessage1,
        String expectedMessage2, String actualJsonResponse) {
        assertJsonResponse(expectedStatus, expectedMessage1 + expectedMessage2,
            actualJsonResponse);
    }

    /**
     * Asserts the expected league count on the database
     */
    private void assertLeagueCount(long expectedLeagueCount) {
        long actualLeagueCount = readLeagueCount();
        Assert.assertEquals("Incorrect League Count", expectedLeagueCount,
            actualLeagueCount);
    }

    /**
     * Asserts a new league with the expected league name has been created on the
     * database
     */
    private void assertNewLeague(String expectedLeagueName) {
        League actualLeague = leagueDbService.findFirstByIdOrderByIdDesc();

        if (actualLeague == null) {
           Assert.fail("League " + expectedLeagueName + " not created");
        }
        Assert.assertEquals("League not created", expectedLeagueName,
            actualLeague.getName());
    }

    /**
     * Uses the Jersey Web Service Client to test the web services.
     * Creates a form with the given league name, and calls the 
     * create league service on the server
     */
     private String create(String league) {
        Form form = new Form();
        form.param("league", league);

        String response = null;
        try {
            response = client
               .target(REST_SERVICE_URL)
               .request(MediaType.APPLICATION_JSON)
               .post(Entity.entity(form,
                    MediaType.APPLICATION_FORM_URLENCODED_TYPE),
                    String.class);
        } catch (Exception e) {
            System.out.println("Client cannot connect to the Server");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

          return response;
    }

      private long readLeagueCount() {
        return leagueDbService.count();
    }


}
