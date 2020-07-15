package com.volleyballlondon.dev;


import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.volleyballlondon.exceptions.NameAlreadyExistsException;
import com.volleyballlondon.exceptions.NameInvalidCharactersException;
import com.volleyballlondon.exceptions.NameLengthException;
import com.volleyballlondon.persistence.model.League;
import com.volleyballlondon.persistence.services.LeagueDbService;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LeagueServiceTest extends VolleyballServiceTest {

    /** Jersey Web Service Client */
	private static Client client;; 

    /** URL for the create league service on the server */
    private static final String LEAGUE_SERVICE_URL =
        "http://localhost:8080/VolleyBallLeagueSystem/rest/LeagueService/leagues";

    {
        client = ClientBuilder.newClient();
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(LeagueDbService.class);
        ctx.refresh();
        leagueDbService = (LeagueDbService) ctx.getBean("leagueDbBean");
    }
    
    private static LeagueDbService leagueDbService;
    
    /* Test Methods. */

    /**
     * Always run this test first, therefore name is prefixed "a_".
     * This is to ensure the database has not change before the 'get' test.
     * @see LeagueService#getLeagues()
     * @verifies get all leagues
     */
    @Test
    public void a_getLeagues_shouldGetAllLeagues() throws Exception {
        String[] expectedLeagues = {"League 01",
                                    "League 02",
                                    "League 03",
                                    "League 04",
                                    "League 05",
                                    "League 06",
                                    "League 07",
                                    "League 08",
                                    "League 09",
                                    "League 10",
                                    "Men's Division 03 - (South)"};
        List<League> actualLeagues = getLeagues();

        assertLeagues(expectedLeagues, actualLeagues);
    }

    /**
    * @see LeagueService#createLeagueByForm(String,HttpServletResponse)
    * @verifies create new league
    */
    @Test
    public void createLeagueByForm_shouldCreateNewLeague() throws Exception {
        long leagueCount = readLeagueCount();
        String newLeague = "Division 01";
        String jsonResponse = createLeague(newLeague);

        assertNewLeague(newLeague);
        assertJsonResponse(VolleyballService.JSON_STATUS_SUCCESS,
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
        String jsonResponse = createLeague(newLeague);

        assertNewLeague(newLeague);
        assertJsonResponse(VolleyballService.JSON_STATUS_SUCCESS,
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
        String jsonResponse = createLeague(newLeague);

        assertJsonResponse(VolleyballService.JSON_STATUS_FAILURE,
            originalLeague, NameAlreadyExistsException.ERROR_MESSAGE,
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
        String jsonResponse = createLeague(newLeague);
        assertJsonResponse(VolleyballService.JSON_STATUS_FAILURE,
            newLeague, NameInvalidCharactersException.ERROR_MESSAGE,
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
        String jsonResponse = createLeague(newLeague);
        assertJsonResponse(VolleyballService.JSON_STATUS_FAILURE,
            EMPTY_STRING, NameInvalidCharactersException.ERROR_MESSAGE,
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
        String jsonResponse = createLeague(newLeague);
        assertJsonResponse(VolleyballService.JSON_STATUS_FAILURE,
            newLeague, NameLengthException.ERROR_MESSAGE + newLeague.length() + ".",
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
        String jsonResponse = createLeague(newLeague);
        assertJsonResponse(VolleyballService.JSON_STATUS_FAILURE,
            newLeague, NameInvalidCharactersException.ERROR_MESSAGE,
            jsonResponse);
        assertLeagueCount(leagueCount);
    }

    /**
     * @see LeagueService#updateLeagueByForm(int,String,HttpServletResponse)
     * @verifies update league
     */
    @Test
    public void updateLeagueByForm_shouldUpdateLeague() throws Exception {
        long leagueCount = readLeagueCount();
        String oldLeagueName = "League 02";
        String newLeagueName = "Division 02";
        long oldId = getLeagueIdByName(oldLeagueName);
        String jsonResponse = updateLeague(oldId, newLeagueName);

        assertLeagueNameById(oldId, newLeagueName);
        assertJsonResponse(VolleyballService.JSON_STATUS_SUCCESS,
            LeagueService.JSON_MESSAGE_SUCCESS, jsonResponse);
        assertLeagueCount(leagueCount);
    }

    /**
     * @see LeagueService#updateLeagueByForm(int,String,HttpServletResponse)
     * @verifies update league with all valid characters
     */
    @Test
    public void updateLeagueByForm_shouldUpdateLeagueWithAllValidCharacters() throws Exception {
        long leagueCount = readLeagueCount();
        String oldLeagueName = "League 03";
        String newLeagueName = "Men's Division 03 - (North)";
        long oldId = getLeagueIdByName(oldLeagueName);
        String jsonResponse = updateLeague(oldId, newLeagueName);

        assertLeagueNameById(oldId, newLeagueName);
        assertJsonResponse(VolleyballService.JSON_STATUS_SUCCESS,
            LeagueService.JSON_MESSAGE_SUCCESS, jsonResponse);
        assertLeagueCount(leagueCount);
    }

    /**
     * @see LeagueService#updateLeagueByForm(int,String,HttpServletResponse)
     * @verifies update league with different case
     */
    @Test
    public void updateLeagueByForm_shouldUpdateLeagueWithDifferentCase() throws Exception {
        long leagueCount = readLeagueCount();
        String oldLeagueName = "League 04";
        String newLeagueName = "LEAGUE 04";
        long oldId = getLeagueIdByName(oldLeagueName);
        String jsonResponse = updateLeague(oldId, newLeagueName);

        assertLeagueNameById(oldId, newLeagueName);
        assertJsonResponse(VolleyballService.JSON_STATUS_SUCCESS,
            LeagueService.JSON_MESSAGE_SUCCESS, jsonResponse);
        assertLeagueCount(leagueCount);
    }

    /**
     * @see LeagueService#updateLeagueByForm(int,String,HttpServletResponse)
     * @verifies not update if league already exists
     */
    @Test
    public void updateLeagueByForm_shouldNotUpdateIfLeagueAlreadyExists() throws Exception {
        long leagueCount = readLeagueCount();
        String oldLeagueName = "League 05";
        String originalLeagueName = "Men's Division 03 - (South)";
        String newLeagueName = "Men's DIVISION 03 - (SOUTH)";
        long oldId = getLeagueIdByName(oldLeagueName);
        String jsonResponse = updateLeague(oldId, newLeagueName);

        assertLeagueNameById(oldId, oldLeagueName);
        assertJsonResponse(VolleyballService.JSON_STATUS_FAILURE,
            originalLeagueName, NameAlreadyExistsException.ERROR_MESSAGE,
            jsonResponse);
        assertLeagueCount(leagueCount);
    }

    /**
     * @see LeagueService#updateLeagueByForm(int,String,HttpServletResponse)
     * @verifies not update given invalid character
     */
    @Test
    public void updateLeagueByForm_shouldNotUpdateGivenInvalidCharacter() throws Exception {
        long leagueCount = readLeagueCount();
        String oldLeagueName = "League 06";
        String newLeagueName = "Men's Division 04@ - (North)";
        long oldId = getLeagueIdByName(oldLeagueName);
        String jsonResponse = updateLeague(oldId, newLeagueName);

        assertLeagueNameById(oldId, oldLeagueName);
        assertJsonResponse(VolleyballService.JSON_STATUS_FAILURE,
            newLeagueName, NameInvalidCharactersException.ERROR_MESSAGE,
            jsonResponse);
        assertLeagueCount(leagueCount);
    }

    /**
     * @see LeagueService#updateLeagueByForm(int,String,HttpServletResponse)
     * @verifies not update given empty string
     */
    @Test
    public void updateLeagueByForm_shouldNotUpdateGivenEmptyString() throws Exception {
        long leagueCount = readLeagueCount();
        String oldLeagueName = "League 07";
        String newLeagueName = EMPTY_STRING;
        long oldId = getLeagueIdByName(oldLeagueName);
        String jsonResponse = updateLeague(oldId, newLeagueName);

        assertLeagueNameById(oldId, oldLeagueName);
        assertJsonResponse(VolleyballService.JSON_STATUS_FAILURE,
            EMPTY_STRING, NameInvalidCharactersException.ERROR_MESSAGE,
            jsonResponse);
        assertLeagueCount(leagueCount);
    }

    /**
     * @see LeagueService#updateLeagueByForm(int,String,HttpServletResponse)
     * @verifies not update given league name too long
     */
    @Test
    public void updateLeagueByForm_shouldNotUpdateGivenLeagueNameTooLong() throws Exception {
        long leagueCount = readLeagueCount();
        String oldLeagueName = "League 08";
        String newLeagueName = "Men's Division 06 - (North - South)";
        long oldId = getLeagueIdByName(oldLeagueName);
        String jsonResponse = updateLeague(oldId, newLeagueName);

        assertLeagueNameById(oldId, oldLeagueName);
        assertJsonResponse(VolleyballService.JSON_STATUS_FAILURE,
            newLeagueName, NameLengthException.ERROR_MESSAGE + newLeagueName.length() + ".",
            jsonResponse);
        assertLeagueCount(leagueCount);
    }

    /**
     * @see LeagueService#updateLeagueByForm(int,String,HttpServletResponse)
     * @verifies not update given not begin with uppercase
     */
    @Test
    public void updateLeagueByForm_shouldNotUpdateGivenNotBeginWithUppercase() throws Exception {
        long leagueCount = readLeagueCount();
        String oldLeagueName = "League 09";
        String newLeagueName = "men's Division 07";
        long oldId = getLeagueIdByName(oldLeagueName);
        String jsonResponse = updateLeague(oldId, newLeagueName);

        assertLeagueNameById(oldId, oldLeagueName);
        assertJsonResponse(VolleyballService.JSON_STATUS_FAILURE,
            newLeagueName, NameInvalidCharactersException.ERROR_MESSAGE,
            jsonResponse);
        assertLeagueCount(leagueCount);
    }

    /**
     * @see LeagueService#deleteLeague(long)
     * @verifies delete league
     */
    @Test
    public void deleteLeague_shouldDeleteLeague() throws Exception {
        long leagueCount = readLeagueCount();
        String leagueName = "League 10";
        long leagueId = getLeagueIdByName(leagueName);
        String jsonResponse = deleteLeague(leagueId);

        assertLeagueDeleted(leagueId, leagueName);
        assertJsonResponse(VolleyballService.JSON_STATUS_SUCCESS,
            LeagueService.JSON_MESSAGE_SUCCESS, jsonResponse);
        assertLeagueCount(leagueCount - 1);
    }

    /* Private Methods. */

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
     * Asserts a list of actual leagues with an array of expected leagues.
     */
    private void assertLeagues(String[] expectedLeagues, List<League> actualLeagues) {
        if (actualLeagues.isEmpty()) {
            Assert.fail("No Leagues Found.");
        }

        int loopCount = 0;
        int expectedLeaguesCount = expectedLeagues.length;

        for (League league : actualLeagues) {
            if (loopCount >= expectedLeaguesCount) {
                Assert.fail("Too Many Leagues Found.");
            }
            Assert.assertEquals("Incorrect League.", expectedLeagues[loopCount],
                league.getName());
            loopCount++;
        }

        if (loopCount != expectedLeaguesCount) {
            Assert.fail("Too Few Leagues Found.");
        }
    }

    /**
     * Gets a league id for a given league name.
     */
    private long getLeagueIdByName(String leagueName) {
        List<League> leagues = leagueDbService.findByNameIgnoreCase(leagueName);
        if (leagues.size() != 1) {
            Assert.fail("Incorrect No. of leagues returned. " + leagues.size() +
                " for league name: " + leagueName);
        }
        return leagues.get(0).getId();
    }

    /**
     * Asserts the league with the id 'leagueId' has the name leagueName.
     */
    private void assertLeagueNameById(long leagueId, String leagueName) {
        Optional<League> leagueOptional = leagueDbService.getById(leagueId);
        League league;
        
        if (leagueOptional.isPresent()) {
            league = leagueOptional.get();
            Assert.assertEquals("Incorrect league name for id: " + leagueId,
                leagueName, league.getName());
        } else {
            Assert.fail("No league found for id: " + leagueId);
        }
    }

    private void assertLeagueDeleted(long leagueId, String leagueName) {
        Optional<League> leagueOptional = leagueDbService.getById(leagueId);
        if (leagueOptional.isPresent()) {
            League league = leagueOptional.get();
            Assert.fail("Should not find league id: " + league.getId() +
                " league name: " + league.getName());
        }

        List<League> leagues = leagueDbService.findByNameIgnoreCase(leagueName);
        if (!leagues.isEmpty()) {
            Assert.fail("Should not find league " + leagues.get(0).getName() +
                " league id: " + leagues.get(0).getId());
        }
    }

    private long readLeagueCount() {
        return leagueDbService.count();
    }

    /* Server Methods. */

    /**
     * Uses the Jersey Web Service Client to test the web create services.
     * Calls the get service on the server
     */
    private List<League> getLeagues() {
        GenericType<List<League>> list = new GenericType<List<League>>() {};
        List<League> leagues = null;

        try {
            leagues = client
                .target(LEAGUE_SERVICE_URL)
                .request(MediaType.APPLICATION_JSON)
                .get(list);
        } catch (Exception e) {
            System.out.println("Read request cannot connect to the Server");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return leagues;
    }
    
    /**
     * Creates a form with the given league name, and calls the 
     * create league service on the server
     */
     private String createLeague(String league) {
        Form form = new Form();
        form.param("new-league-name", league);

        String response = null;
        try {
            response = client
               .target(LEAGUE_SERVICE_URL)
               .request(MediaType.APPLICATION_JSON)
               .post(Entity.entity(form,
                    MediaType.APPLICATION_FORM_URLENCODED_TYPE),
                    String.class);
        } catch (Exception e) {
            System.out.println("Create request cannot connect to the Server");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

          return response;
    }

    /**
     * Creates a form with the given league id and name, and calls the 
     * update league service on the server
     */
    private String updateLeague(long leagueId, String leagueName) {
        Form form = new Form();
        form.param("league-id", String.valueOf(leagueId));
        form.param("new-league-name", leagueName);

        String response = null;
        try {
            response = client
               .target(LEAGUE_SERVICE_URL)
               .request(MediaType.APPLICATION_JSON)
               .put(Entity.entity(form,
                    MediaType.APPLICATION_FORM_URLENCODED_TYPE),
                    String.class);
        } catch (Exception e) {
            System.out.println("Update request cannot connect to the Server");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

          return response;
    }

    /**
     * Calls the delete service on the server
     * Deletes the league with the given id.
     */
    private String deleteLeague(long leagueId) {
        String response = null;
        try {
            response = client
                .target(LEAGUE_SERVICE_URL)
                .path("/{leagueid}")
                .resolveTemplate("leagueid", leagueId)
                .request(MediaType.APPLICATION_JSON)
                .delete(String.class); 
        } catch (Exception e) {
            System.out.println("Delete request cannot connect to the Server");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

          return response;
    }

}
