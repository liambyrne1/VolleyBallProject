package com.volleyballlondon.dev;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
import com.volleyballlondon.persistence.model.Club;
import com.volleyballlondon.persistence.model.Team;
import com.volleyballlondon.persistence.services.ClubDbService;
import com.volleyballlondon.persistence.services.TeamDbService;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ClubServiceTest extends VolleyballServiceTest {

    /** Jersey Web Service Client */
	private static Client client;; 

    /** URL for the create club service on the server */
    private static final String CLUB_SERVICE_URL =
        "http://localhost:8080/VolleyBallLeagueSystem/rest/ClubService/clubs";

    {
        client = ClientBuilder.newClient();
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(ClubDbService.class);
        ctx.register(TeamDbService.class);
        ctx.refresh();
        clubDbService = (ClubDbService) ctx.getBean("clubDbBean");
        teamDbService = (TeamDbService) ctx.getBean("teamDbBean");
    }
    
    private static ClubDbService clubDbService;
    private static TeamDbService teamDbService;
    
    /* Test Methods. */

    @Test
    public void dbServiceTest() throws Exception {
        System.out.println();
        System.out.println("Using TeamDbService");
        List<Team> teamlist = teamDbService.findByClubIdOrderByName((long)1);
        for (Team team : teamlist) {
            System.out.println("Team = " + team);
        }
        System.out.println();
        System.out.println("Adding Team");
        teamDbService.addTeam("Team 04", (long)1);
    }

    /**
     * Always run this test first, therefore name is prefixed "a_".
     * This is to ensure the database has not change before the 'get' test.
     * @see ClubService#getClubs()
     * @verifies get all clubs
     */
    /*@Test*/
    public void a_getClubs_shouldGetAllClubs() throws Exception {
        String[] expectedClubs = {"Club 01",
                                    "Club 02",
                                    "Club 03",
                                    "Club 04",
                                    "Club 05",
                                    "Club 06",
                                    "Club 07",
                                    "Club 08",
                                    "Club 09",
                                    "Club 10",
                                    "Men's Club1234 03 - (South)"};
        List<Club> actualClubs = getClubs();

        assertClubs(expectedClubs, actualClubs);
    }

    /**
    * @see ClubService#createClubByForm(String,HttpServletResponse)
    * @verifies create new club
    */
    /*@Test*/
    public void createClubByForm_shouldCreateNewClub() throws Exception {
        long clubCount = readClubCount();
        String newClub = "Club1234 01";
        String jsonResponse = createClub(newClub);

        assertNewClub(newClub);
        assertJsonResponse(VolleyballService.JSON_STATUS_SUCCESS,
            ClubService.JSON_MESSAGE_SUCCESS_CREATED, jsonResponse);
        assertClubCount(clubCount + 1);
    }

    /**
     * @see ClubService#createClubByForm(String,HttpServletResponse)
     * @verifies create new club with all valid characters
     */
    /*@Test*/
    public void createClubByForm_shouldCreateNewClubWithAllValidCharacters() throws Exception {
        long clubCount = readClubCount();
        String newClub = "Men's Club1234 02 - (North)";
        String jsonResponse = createClub(newClub);

        assertNewClub(newClub);
        assertJsonResponse(VolleyballService.JSON_STATUS_SUCCESS,
            ClubService.JSON_MESSAGE_SUCCESS_CREATED, jsonResponse);
        assertClubCount(clubCount + 1);
    }

    /**
     * @see ClubService#createClubByForm(String,HttpServletResponse)
     * @verifies fail if club already exists
     */
    /*@Test*/
    public void createClubByForm_shouldFailIfClubAlreadyExists() throws Exception {
        long clubCount = readClubCount();
        String originalClub = "Men's Club1234 03 - (South)";
        String newClub = "Men's CLUB1234 03 - (SOUTH)";
        String jsonResponse = createClub(newClub);

        assertJsonResponse(VolleyballService.JSON_STATUS_FAILURE,
            originalClub, NameAlreadyExistsException.ERROR_MESSAGE,
            jsonResponse);
        assertClubCount(clubCount);
    }

    /**
     * @see ClubService#createClubByForm(String,HttpServletResponse)
     * @verifies fail given invalid character
     */
    /*@Test*/
    public void createClubByForm_shouldFailGivenInvalidCharacter() throws Exception {
        long clubCount = readClubCount();
        String newClub = "Men's Club1234 04; - (North)";
        String jsonResponse = createClub(newClub);
        assertJsonResponse(VolleyballService.JSON_STATUS_FAILURE,
            newClub, NameInvalidCharactersException.ERROR_MESSAGE,
            jsonResponse);
        assertClubCount(clubCount);
    }

    /**
     * @see ClubService#createClubByForm(String,HttpServletResponse)
     * @verifies fail given empty string
     */
    /*@Test*/
    public void createClubByForm_shouldFailGivenEmptyString() throws Exception {
        long clubCount = readClubCount();
        String newClub = EMPTY_STRING;
        String jsonResponse = createClub(newClub);
        assertJsonResponse(VolleyballService.JSON_STATUS_FAILURE,
            EMPTY_STRING, NameInvalidCharactersException.ERROR_MESSAGE,
            jsonResponse);
        assertClubCount(clubCount);
    }

    /**
     * @see ClubService#createClubByForm(String,HttpServletResponse)
     * @verifies fail given club name too long
     */
    /*@Test*/
    public void createClubByForm_shouldFailGivenClubNameTooLong() throws Exception {
        long clubCount = readClubCount();
        String newClub = "Men's Club1234 06 - (North - South)";
        String jsonResponse = createClub(newClub);
        assertJsonResponse(VolleyballService.JSON_STATUS_FAILURE,
            newClub, NameLengthException.ERROR_MESSAGE + newClub.length() + ".",
            jsonResponse);
        assertClubCount(clubCount);
    }

    /**
     * @see ClubService#createClubByForm(String,HttpServletResponse)
     * @verifies fail given not begin with uppercase
     */
    /*@Test*/
    public void createClubByForm_shouldFailGivenNotBeginWithUppercase() throws Exception {
        long clubCount = readClubCount();
        String newClub = "men's Club1234 07";
        String jsonResponse = createClub(newClub);
        assertJsonResponse(VolleyballService.JSON_STATUS_FAILURE,
            newClub, NameInvalidCharactersException.ERROR_MESSAGE,
            jsonResponse);
        assertClubCount(clubCount);
    }

    /**
     * @see ClubService#updateClubByForm(int,String,HttpServletResponse)
     * @verifies update club
     */
    /*@Test*/
    public void updateClubByForm_shouldUpdateClub() throws Exception {
        long clubCount = readClubCount();
        String oldClubName = "Club 02";
        String newClubName = "Club1234 02";
        long oldId = getClubIdByName(oldClubName);
        String jsonResponse = updateClub(oldId, newClubName);

        assertClubNameById(oldId, newClubName);
        assertJsonResponse(VolleyballService.JSON_STATUS_SUCCESS,
            ClubService.JSON_MESSAGE_SUCCESS_UPDATED, jsonResponse);
        assertClubCount(clubCount);
    }

    /**
     * @see ClubService#updateClubByForm(int,String,HttpServletResponse)
     * @verifies update club with all valid characters
     */
    /*@Test*/
    public void updateClubByForm_shouldUpdateClubWithAllValidCharacters() throws Exception {
        long clubCount = readClubCount();
        String oldClubName = "Club 03";
        String newClubName = "Men's Club1234 03 - (North)";
        long oldId = getClubIdByName(oldClubName);
        String jsonResponse = updateClub(oldId, newClubName);

        assertClubNameById(oldId, newClubName);
        assertJsonResponse(VolleyballService.JSON_STATUS_SUCCESS,
            ClubService.JSON_MESSAGE_SUCCESS_UPDATED, jsonResponse);
        assertClubCount(clubCount);
    }

    /**
     * @see ClubService#updateClubByForm(int,String,HttpServletResponse)
     * @verifies update club with different case
     */
    /*@Test*/
    public void updateClubByForm_shouldUpdateClubWithDifferentCase() throws Exception {
        long clubCount = readClubCount();
        String oldClubName = "Club 04";
        String newClubName = "CLUB 04";
        long oldId = getClubIdByName(oldClubName);
        String jsonResponse = updateClub(oldId, newClubName);

        assertClubNameById(oldId, newClubName);
        assertJsonResponse(VolleyballService.JSON_STATUS_SUCCESS,
            ClubService.JSON_MESSAGE_SUCCESS_UPDATED, jsonResponse);
        assertClubCount(clubCount);
    }

    /**
     * @see ClubService#updateClubByForm(int,String,HttpServletResponse)
     * @verifies not update if club already exists
     */
    /*@Test*/
    public void updateClubByForm_shouldNotUpdateIfClubAlreadyExists() throws Exception {
        long clubCount = readClubCount();
        String oldClubName = "Club 05";
        String originalClubName = "Men's Club1234 03 - (South)";
        String newClubName = "Men's CLUB1234 03 - (SOUTH)";
        long oldId = getClubIdByName(oldClubName);
        String jsonResponse = updateClub(oldId, newClubName);

        assertClubNameById(oldId, oldClubName);
        assertJsonResponse(VolleyballService.JSON_STATUS_FAILURE,
            originalClubName, NameAlreadyExistsException.ERROR_MESSAGE,
            jsonResponse);
        assertClubCount(clubCount);
    }

    /**
     * @see ClubService#updateClubByForm(int,String,HttpServletResponse)
     * @verifies not update given invalid character
     */
    /*@Test*/
    public void updateClubByForm_shouldNotUpdateGivenInvalidCharacter() throws Exception {
        long clubCount = readClubCount();
        String oldClubName = "Club 06";
        String newClubName = "Men's Club1234 04@ - (North)";
        long oldId = getClubIdByName(oldClubName);
        String jsonResponse = updateClub(oldId, newClubName);

        assertClubNameById(oldId, oldClubName);
        assertJsonResponse(VolleyballService.JSON_STATUS_FAILURE,
            newClubName, NameInvalidCharactersException.ERROR_MESSAGE,
            jsonResponse);
        assertClubCount(clubCount);
    }

    /**
     * @see ClubService#updateClubByForm(int,String,HttpServletResponse)
     * @verifies not update given empty string
     */
    /*@Test*/
    public void updateClubByForm_shouldNotUpdateGivenEmptyString() throws Exception {
        long clubCount = readClubCount();
        String oldClubName = "Club 07";
        String newClubName = EMPTY_STRING;
        long oldId = getClubIdByName(oldClubName);
        String jsonResponse = updateClub(oldId, newClubName);

        assertClubNameById(oldId, oldClubName);
        assertJsonResponse(VolleyballService.JSON_STATUS_FAILURE,
            EMPTY_STRING, NameInvalidCharactersException.ERROR_MESSAGE,
            jsonResponse);
        assertClubCount(clubCount);
    }

    /**
     * @see ClubService#updateClubByForm(int,String,HttpServletResponse)
     * @verifies not update given club name too long
     */
    /*@Test*/
    public void updateClubByForm_shouldNotUpdateGivenClubNameTooLong() throws Exception {
        long clubCount = readClubCount();
        String oldClubName = "Club 08";
        String newClubName = "Men's Club1234 06 - (North - South)";
        long oldId = getClubIdByName(oldClubName);
        String jsonResponse = updateClub(oldId, newClubName);

        assertClubNameById(oldId, oldClubName);
        assertJsonResponse(VolleyballService.JSON_STATUS_FAILURE,
            newClubName, NameLengthException.ERROR_MESSAGE + newClubName.length() + ".",
            jsonResponse);
        assertClubCount(clubCount);
    }

    /**
     * @see ClubService#updateClubByForm(int,String,HttpServletResponse)
     * @verifies not update given not begin with uppercase
     */
    /*@Test*/
    public void updateClubByForm_shouldNotUpdateGivenNotBeginWithUppercase() throws Exception {
        long clubCount = readClubCount();
        String oldClubName = "Club 09";
        String newClubName = "men's Club1234 07";
        long oldId = getClubIdByName(oldClubName);
        String jsonResponse = updateClub(oldId, newClubName);

        assertClubNameById(oldId, oldClubName);
        assertJsonResponse(VolleyballService.JSON_STATUS_FAILURE,
            newClubName, NameInvalidCharactersException.ERROR_MESSAGE,
            jsonResponse);
        assertClubCount(clubCount);
    }

    /**
     * @see ClubService#deleteClub(long)
     * @verifies delete club
     */
    /*@Test*/
    public void deleteClub_shouldDeleteClub() throws Exception {
        long clubCount = readClubCount();
        String clubName = "Club 10";
        long clubId = getClubIdByName(clubName);
        String jsonResponse = deleteClub(clubId);

        assertClubDeleted(clubId, clubName);
        assertJsonResponse(VolleyballService.JSON_STATUS_SUCCESS,
            ClubService.JSON_MESSAGE_SUCCESS_DELETED, jsonResponse);
        assertClubCount(clubCount - 1);
    }

    /* Private Methods. */

    /**
     * Asserts the expected club count on the database
     */
    private void assertClubCount(long expectedClubCount) {
        long actualClubCount = readClubCount();
        Assert.assertEquals("Incorrect Club Count", expectedClubCount,
            actualClubCount);
    }

    /**
     * Asserts a new club with the expected club name has been created on the
     * database
     */
    private void assertNewClub(String expectedClubName) {
        Club actualClub = clubDbService.findFirstByIdOrderByIdDesc();

        if (actualClub == null) {
           Assert.fail("Club " + expectedClubName + " not created");
        }
        Assert.assertEquals("Club not created", expectedClubName,
            actualClub.getName());
    }

    /**
     * Asserts a list of actual clubs with an array of expected clubs.
     */
    private void assertClubs(String[] expectedClubs, List<Club> actualClubs) {
        if (actualClubs.isEmpty()) {
            Assert.fail("No Clubs Found.");
        }

        int loopCount = 0;
        int expectedClubsCount = expectedClubs.length;

        for (Club club : actualClubs) {
            if (loopCount >= expectedClubsCount) {
                Assert.fail("Too Many Clubs Found.");
            }
            Assert.assertEquals("Incorrect Club.", expectedClubs[loopCount],
                club.getName());
            loopCount++;
        }

        if (loopCount != expectedClubsCount) {
            Assert.fail("Too Few Clubs Found.");
        }
    }

    /**
     * Gets a club id for a given club name.
     */
    private long getClubIdByName(String clubName) {
        List<Club> clubs = clubDbService.findByNameIgnoreCase(clubName);
        if (clubs.size() != 1) {
            Assert.fail("Incorrect No. of clubs returned. " + clubs.size() +
                " for club name: " + clubName);
        }
        return clubs.get(0).getId();
    }

    /**
     * Asserts the club with the id 'clubId' has the name clubName.
     */
    private void assertClubNameById(long clubId, String clubName) {
        Optional<Club> clubOptional = clubDbService.getById(clubId);
        Club club;
        
        if (clubOptional.isPresent()) {
            club = clubOptional.get();
            Assert.assertEquals("Incorrect club name for id: " + clubId,
                clubName, club.getName());
        } else {
            Assert.fail("No club found for id: " + clubId);
        }
    }

    private void assertClubDeleted(long clubId, String clubName) {
        Optional<Club> clubOptional = clubDbService.getById(clubId);
        if (clubOptional.isPresent()) {
            Club club = clubOptional.get();
            Assert.fail("Should not find club id: " + club.getId() +
                " club name: " + club.getName());
        }

        List<Club> clubs = clubDbService.findByNameIgnoreCase(clubName);
        if (!clubs.isEmpty()) {
            Assert.fail("Should not find club " + clubs.get(0).getName() +
                " club id: " + clubs.get(0).getId());
        }
    }

    private long readClubCount() {
        return clubDbService.count();
    }

    /* Server Methods. */

    /**
     * Uses the Jersey Web Service Client to test the web create services.
     * Calls the get service on the server
     */
    private List<Club> getClubs() {
        GenericType<List<Club>> list = new GenericType<List<Club>>() {};
        List<Club> clubs = null;

        try {
            clubs = client
                .target(CLUB_SERVICE_URL)
                .request(MediaType.APPLICATION_JSON)
                .get(list);
        } catch (Exception e) {
            System.out.println("Read request cannot connect to the Server");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return clubs;
    }
    
    /**
     * Creates a form with the given club name, and calls the 
     * create club service on the server
     */
     private String createClub(String club) {
        Form form = new Form();
        form.param("new-club-name", club);

        String response = null;
        try {
            response = client
               .target(CLUB_SERVICE_URL)
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
     * Creates a form with the given club id and name, and calls the 
     * update club service on the server
     */
    private String updateClub(long clubId, String clubName) {
        Form form = new Form();
        form.param("club-id", String.valueOf(clubId));
        form.param("new-club-name", clubName);

        String response = null;
        try {
            response = client
               .target(CLUB_SERVICE_URL)
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
     * Deletes the club with the given id.
     */
    private String deleteClub(long clubId) {
        String response = null;
        try {
            response = client
                .target(CLUB_SERVICE_URL)
                .path("/{clubid}")
                .resolveTemplate("clubid", clubId)
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
