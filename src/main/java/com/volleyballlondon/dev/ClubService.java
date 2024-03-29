package com.volleyballlondon.dev;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.volleyballlondon.dev.validator.ClubValidator;
import com.volleyballlondon.exceptions.VolleyballException;
import com.volleyballlondon.persistence.model.Club;
import com.volleyballlondon.persistence.services.ClubDbService;

/**
 * Provides the CRUD services for a club Returns a JSON response which has two
 * properties:- status message
 *
 * The status property has the following boolean values:- true the service is
 * successful false the service fails
 *
 * The message is a string property which gives a description of the failure.
 */
@Controller
public class ClubService extends VolleyballService {

	public ClubService() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(ClubDbService.class);
        ctx.refresh();
        clubDbService = (ClubDbService) ctx.getBean("clubDbBean");
        clubValidator = new ClubValidator(clubDbService);
	}

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    /** Club database service */
    private ClubDbService clubDbService;

    /** Club validators */
    private ClubValidator clubValidator;

    public static final String JSON_MESSAGE_SUCCESS_CREATED
        = "New Club has been created.";

    public static final String JSON_MESSAGE_SUCCESS_UPDATED
        = "Club has been updated.";

    public static final String JSON_MESSAGE_SUCCESS_DELETED
        = " has been deleted.";

    /**
     * Returns the Maintain Club form.
     */
    @GetMapping("/clubmaintenance")
    public String showClubMaintenanceForm() {
        LOGGER.debug("Rendering club maintenance page.");
        System.out.println("*** Running clubmaintenance ***");
        return "maintainClub";
    }

    /**
     * Returns all the clubs from the database in alphabetical order.
     * Do not allow Eclipse to generate this test case. Remove '@'
     * should get all clubs
     */
    @GetMapping(path = "/clubs", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Club> getClubs(){
        List<Club> clubs = new ArrayList<>();
        try {
            clubs = clubDbService.findByOrderByName();
        } catch (Exception e) {
            logException(e);
        }
        return clubs;
    }

    /**
     * Creates a new club on the database.
     * 
     * @param newClub
     *            - the new club name
     * @return returns the following responses:- success New Club has been
     *         created failure Club Name contains Invalid Characters failure
     *         Club already exists failure Volleyball server is unavailable
     *
     * @should create new club
     * @should create new club with all valid characters
     * @should fail if club already exists
     * @should fail given invalid character
     * @should fail given empty string
     * @should fail given club name too long
     * @should fail given not begin with uppercase
     */
    @PostMapping(path = "/clubs", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String createClubByForm(@RequestParam("new-club-name") String newClub)
        throws IOException {

        JSONObject response = new JSONObject();
        response.put(JSON_STATUS, JSON_STATUS_FAILURE);

        try {
            clubValidator.validateClubNameCreate(newClub);
            clubDbService.addClub(newClub);
            response.put(JSON_STATUS, JSON_STATUS_SUCCESS);
            response.put(JSON_MESSAGE, JSON_MESSAGE_SUCCESS_CREATED);
        } catch (VolleyballException e) {
            response.put(JSON_MESSAGE, e.getMessage());
        } catch (Exception e) {
            logException(e);
            response.put(JSON_MESSAGE, JSON_MESSAGE_UNAVAILABLE);
        }
        return response.toString();
    }

    /**
     * Updates an existing club on the database. Allow user to change
     * the case of the club name being updated.
     * 
     * @param clubId Id of the club that is being updated.
     * @param newClubName
     *            - the new club name
     * @return returns the following responses:- success New Club has been
     *         updated failure Club Name contains Invalid Characters failure
     *         Club already exists failure Volleyball server is unavailable
     *
     * @should update club
     * @should update club with all valid characters
     * @should update club with different case
     * @should not update if club already exists
     * @should not update given invalid character
     * @should not update given empty string
     * @should not update given club name too long
     * @should not update given not begin with uppercase
     */
    @PutMapping(path = "/clubs", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String updateClubByForm(
        @RequestBody MultiValueMap<String, String> updatedClub)
        throws IOException {

        System.out.println("Entering updateClubByForm");
        System.out.println(updatedClub);
        int clubId = Integer.parseInt(updatedClub.getFirst("club-id"));
        String newClubName = updatedClub.getFirst("new-club-name");
        JSONObject response = new JSONObject();
        response.put(JSON_STATUS, JSON_STATUS_FAILURE);

        try {
            clubValidator.validateClubNameUpdate(clubId, newClubName);
            clubDbService.updateClubName((long) clubId, newClubName);
            response.put(JSON_STATUS, JSON_STATUS_SUCCESS);
            response.put(JSON_MESSAGE, JSON_MESSAGE_SUCCESS_UPDATED);
        } catch (VolleyballException e) {
            response.put(JSON_MESSAGE, e.getMessage());
        } catch (Exception e) {
            logException(e);
            response.put(JSON_MESSAGE, JSON_MESSAGE_UNAVAILABLE);
        }
        return response.toString();
    }

    /**
     * Deletes an existing club on the database.
     * 
     * @param clubId Id of the club that is being deleted.
     *
     * @return returns the following responses:- success Club has been deleted
     *         failure Volleyball server is unavailable
     *
     * @should delete club
     */    
    @DeleteMapping(path = "/clubs/{clubid}",
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String deleteClub(@PathVariable("clubid") long clubId){
        JSONObject response = new JSONObject();
        try {
            clubDbService.deleteClub(clubId);
            response.put(JSON_STATUS, JSON_STATUS_SUCCESS);
            response.put(JSON_MESSAGE, JSON_MESSAGE_SUCCESS_DELETED);
        } catch (Exception e) {
            logException(e);
            response.put(JSON_STATUS, JSON_STATUS_FAILURE);
            response.put(JSON_MESSAGE, JSON_MESSAGE_UNAVAILABLE);
        }

        return response.toString();
    }
}
