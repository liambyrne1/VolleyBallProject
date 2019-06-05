package com.volleyballlondon.test;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.volleyballlondon.dev.LeagueService;
import com.volleyballlondon.exceptions.LeagueAlreadyExistsException;
import com.volleyballlondon.exceptions.LeagueInvalidCharactersException;
import com.volleyballlondon.exceptions.LeagueLengthException;
import com.volleyballlondon.persistence.model.League;
import com.volleyballlondon.persistence.services.LeagueDbService;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class LeagueUT extends TestCase
{
    /** Jersey Web Service Client */
	private static Client c_client;; 

    /** URL for the create league service on the server */
    private static final String C_REST_SERVICE_URL =
        "http://localhost:8080/VolleyBallLeagueSystem/rest/LeagueService/leagues";

    private static final String C_EMPTY_STRING = "";

    /** Command line interface.
     * 
     * @param p_args Not used.
     */
    public static void main(String[] p_args)
    {
        System.out.println("Parameters are: " + (p_args.length == 0 ? "" : "ignored"));
        junit.textui.TestRunner.run(suite());
    }
    
    /** 
     * Static method that allows the framework to
     * to automatically run all the tests in the class. A program
     * provided by the JUnit framework can traverse a list of TestCase classes
     * calling this suite method and get an instance of the TestCase which it then
     * executes. See the main method in class for an example.
     * @return an instance of junit.framework.Test
     */
    public static Test suite()
    {
        return new TestSuite(LeagueUT.class);
    }

    {
        c_client = ClientBuilder.newClient();
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(LeagueDbService.class);
        ctx.refresh();
        System.out.println("Load context");
        leagueDbService = (LeagueDbService) ctx.getBean("mainBean");
    }
    
    private static LeagueDbService leagueDbService;
    
    /** Standard constructor.
     * @param p_name Name of test.
     */
    public LeagueUT(String p_name)
    {
        super(p_name);
    }

    /** 
     * Test 01. Creates a new league "Division 01" which does not already exist.
     * Returns a success.
     *
     * Asserts new league "Division 01" has been created on database
     * Asserts JSON response is correct
     * Asserts league count has increased by 1
     */
    public void test01NewLeague()
    {
        long l_leagueCount = readLeagueCount();
        String l_newLeague = "Division 01";
        String l_jsonResponse = create(l_newLeague);

        assertNewLeague(l_newLeague);
        assertJsonResponse(LeagueService.C_JSON_STATUS_SUCCESS,
            LeagueService.C_JSON_MESSAGE_SUCCESS, l_jsonResponse);
        assertLeagueCount(l_leagueCount + 1);
    }

    /** 
     * Test 02. Creates a new league "Men's Division 02 - (North)"
     * which does not already exist.
     * This league name contains all the valid characters, lower and upper case
     * letters, numbers, spaces, apostrophe, brackets and dash.
     * Returns a success.
     *
     * Asserts new league "Men's Division 02 - (North)" has been created on database
     * Asserts JSON response is correct
     * Asserts league count has increased by 1
     */
    public void test02NewLeagueAllCharacters()
    {
        long l_leagueCount = readLeagueCount();
        String l_newLeague = "Men's Division 02 - (North)";
        String l_jsonResponse = create(l_newLeague);

        assertNewLeague(l_newLeague);
        assertJsonResponse(LeagueService.C_JSON_STATUS_SUCCESS,
            LeagueService.C_JSON_MESSAGE_SUCCESS, l_jsonResponse);
        assertLeagueCount(l_leagueCount + 1);
    }

    /** 
     * Test 03. Creates a league "Men's DIVISION 03 - (SOUTH)" that already exists.
     * "Men's Division 03 - (South)" already exists on the database. (Note lower case letters)
     * The MySQL operator "=" is case insensitive in a string comparison.
     * Returns a failure, league already exists.
     *
     * Asserts JSON response is correct
     * Asserts league count has not changed
     */
    public void test03LeagueAlreadyExists()
    {
        long l_leagueCount = readLeagueCount();
        String l_originalLeague = "Men's Division 03 - (South)";
        String l_newLeague = "Men's DIVISION 03 - (SOUTH)";
        String l_jsonResponse = create(l_newLeague);

        assertJsonResponse(LeagueService.C_JSON_STATUS_FAILURE,
            l_originalLeague, LeagueAlreadyExistsException.C_ERROR_MESSAGE,
            l_jsonResponse);
        assertLeagueCount(l_leagueCount);
    }

    /** 
     * Test 04. Creates a new league "Men's Division 04; - (North)"
     * which does not already exist.
     * This league name contains an invalid character ";".
     * Returns a failure, league name contains invalid character.
     *
     * Asserts JSON response is correct
     * Asserts league count has not changed
     */
    public void test04NewLeagueInvalidCharacters()
    {
        long l_leagueCount = readLeagueCount();
        String l_newLeague = "Men's Division 04; - (North)";
        String l_jsonResponse = create(l_newLeague);
        assertJsonResponse(LeagueService.C_JSON_STATUS_FAILURE,
            LeagueInvalidCharactersException.C_ERROR_MESSAGE,
            l_jsonResponse);
        assertLeagueCount(l_leagueCount);
    }

    /** 
     * Test 05. Empty string
     * Returns a failure, league name contains invalid character.
     *
     * Asserts JSON response is correct
     * Asserts league count has not changed
     */
    public void test05EmptyString()
    {
        long l_leagueCount = readLeagueCount();
        String l_newLeague = C_EMPTY_STRING;
        String l_jsonResponse = create(l_newLeague);
        assertJsonResponse(LeagueService.C_JSON_STATUS_FAILURE,
            LeagueInvalidCharactersException.C_ERROR_MESSAGE,
            l_jsonResponse);
        assertLeagueCount(l_leagueCount);
    }

    /** 
     * Test 06. Creates a new league "Men's Division 06 - (North - South)"
     * which does not already exist.
     * This league name contains 35 characters.
     * Maximum length of a league name is 30 characters
     * Returns a failure, league name is too long.
     *
     * Asserts JSON response is correct
     * Asserts league count has not changed
     */
    public void test06NewLeagueTooLong()
    {
        long l_leagueCount = readLeagueCount();
        String l_newLeague = "Men's Division 06 - (North - South)";
        String l_jsonResponse = create(l_newLeague);
        assertJsonResponse(LeagueService.C_JSON_STATUS_FAILURE,
            LeagueLengthException.C_ERROR_MESSAGE + l_newLeague.length() + ".",
            l_jsonResponse);
        assertLeagueCount(l_leagueCount);
    }

    /** 
     * Test 07. Creates a new league "men's Division 07"
     * which does not already exist.
     * This league name does not begin with an uppercase letter.
     * Returns a failure, league name contains invalid character.
     *
     * Asserts JSON response is correct
     * Asserts league count has not changed
     */
    public void test07NewLeagueNoUppercaseCharacter()
    {
        long l_leagueCount = readLeagueCount();
        String l_newLeague = "men's Division 07";
        String l_jsonResponse = create(l_newLeague);
        assertJsonResponse(LeagueService.C_JSON_STATUS_FAILURE,
            LeagueInvalidCharactersException.C_ERROR_MESSAGE,
            l_jsonResponse);
        assertLeagueCount(l_leagueCount);
    }

    /**
     * Asserts the expected status and the expected message in the actual
     * JSON response
     * Fails the test if JSON response cannot be parsed
     */
    private void assertJsonResponse(Boolean p_expectedStatus, String p_expectedMessage,
        String p_actualJsonResponse) {
        JSONParser l_parser = new JSONParser();
        try {
            JSONObject l_jsonObject = (JSONObject) l_parser.parse(p_actualJsonResponse);
            assertEquals("Incorrect Message", p_expectedMessage,
                (String)l_jsonObject.get(LeagueService.C_JSON_MESSAGE));
            assertEquals("Incorrect Status", p_expectedStatus,
                (Boolean)l_jsonObject.get(LeagueService.C_JSON_STATUS));
        } catch (ParseException l_e) {
            System.out.println("position: " + l_e.getPosition());
            System.out.println(l_e);
            fail("Unable to parse json response.");
        }
    }

    /**
     * Concatenates the expected message1 and message2 into the message value
     * before asserting the JSON response
     */
    private void assertJsonResponse(Boolean p_expectedStatus, String p_expectedMessage1,
        String p_expectedMessage2, String p_actualJsonResponse) {
        assertJsonResponse(p_expectedStatus, p_expectedMessage1 + p_expectedMessage2,
            p_actualJsonResponse);
    }

    /**
     * Asserts the expected league count on the database
     */
    private void assertLeagueCount(long p_expectedLeagueCount) {
        long l_actualLeagueCount = readLeagueCount();
        assertEquals("Incorrect League Count", p_expectedLeagueCount,
            l_actualLeagueCount);
    }

    /**
     * Asserts a new league with the expected league name has been created on the
     * database
     */
    private void assertNewLeague(String l_expectedLeagueName) {
        League l_actualLeague = leagueDbService.findFirstByIdOrderByIdDesc();

        if (l_actualLeague == null) {
           fail("League " + l_expectedLeagueName + " not created");
        }
        assertEquals("League not created", l_expectedLeagueName,
            l_actualLeague.getName());
    }

    /**
     * Uses the Jersey Web Service Client to test the web services.
     * Creates a form with the given league name, and calls the 
     * create league service on the server
     */
     private String create(String p_league) {
        Form l_form = new Form();
        l_form.param("league", p_league);

        String l_response = null;
        try {
            l_response = c_client
               .target(C_REST_SERVICE_URL)
               .request(MediaType.APPLICATION_JSON)
               .post(Entity.entity(l_form,
                    MediaType.APPLICATION_FORM_URLENCODED_TYPE),
                    String.class);
        } catch (Exception e) {
            System.out.println("Client cannot connect to the Server");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

          return l_response;
    }

  private long readLeagueCount() {
    return leagueDbService.count();
}

    /**This method is used to setup anything required by each test. */
    protected void setUp()
    {
    }
}
