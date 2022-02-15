package com.volleyballlondon.dev;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

import com.volleyballlondon.dev.validator.LeagueValidator;
import com.volleyballlondon.exceptions.VolleyballException;
import com.volleyballlondon.persistence.model.League;
import com.volleyballlondon.persistence.services.LeagueDbService;

/**
 * Provides the CRUD services for a league Returns a JSON response which has two
 * properties:- status message
 *
 * The status property has the following boolean values:- true the service is
 * successful false the service fails
 *
 * The message is a string property which gives a description of the failure.
 */
@Controller
public class LeagueService extends VolleyballService {

	public LeagueService() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(LeagueDbService.class);
        ctx.refresh();
        leagueDbService = (LeagueDbService) ctx.getBean("leagueDbBean");
        leagueValidator = new LeagueValidator(leagueDbService);
	}

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    /** League database service */
    private LeagueDbService leagueDbService;

    /** League validators */
    private LeagueValidator leagueValidator;

    public static final String JSON_MESSAGE_SUCCESS_CREATED
        = "New League has been created.";

    public static final String JSON_MESSAGE_SUCCESS_UPDATED
        = "League has been updated.";

    public static final String JSON_MESSAGE_SUCCESS_DELETED
        = " has been deleted.";

    /**
     * Returns the Maintain League form.
     */
    @GetMapping("/leaguemaintenance")
    public String showLeagueMaintenanceForm() {
        LOGGER.debug("Rendering league maintenance page.");
        System.out.println("*** Running leaguemaintenance ***");
        return "maintainLeague";
    }

    /**
     * Returns all the leagues from the database in alphabetical order.
     * Do not allow Eclipse to generate this test case. Remove '@'
     * should get all leagues
     */
    @GetMapping(path = "/leagues", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<League> getLeagues(){
        List<League> leagues = new ArrayList<>();
        System.out.println("*** Running getLeagues ***");
        try {
            leagues = leagueDbService.findByOrderByName();
        } catch (Exception e) {
            logException(e);
        }
        return leagues;
    }

    /**
     * Creates a new league on the database.
     * 
     * @param newLeague
     *            - the new league name
     * @return returns the following responses:- success New League has been
     *         created failure League Name contains Invalid Characters failure
     *         League already exists failure Volleyball server is unavailable
     *
     * @should create new league
     * @should create new league with all valid characters
     * @should fail if league already exists
     * @should fail given invalid character
     * @should fail given empty string
     * @should fail given league name too long
     * @should fail given not begin with uppercase
     */
    @PostMapping(path = "/leagues", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String createLeagueByForm(@RequestParam("new-league-name") String newLeague)
        throws IOException {

        JSONObject response = new JSONObject();
        response.put(JSON_STATUS, JSON_STATUS_FAILURE);

        try {
            leagueValidator.validateLeagueNameCreate(newLeague);
            leagueDbService.addLeague(newLeague);
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
     * Updates an existing league on the database. Allow user to change
     * the case of the league name being updated.
     * 
     * @param leagueId Id of the league that is being updated.
     * @param newLeagueName
     *            - the new league name
     * @return returns the following responses:- success New League has been
     *         updated failure League Name contains Invalid Characters failure
     *         League already exists failure Volleyball server is unavailable
     *
     * @should update league
     * @should update league with all valid characters
     * @should update league with different case
     * @should not update if league already exists
     * @should not update given invalid character
     * @should not update given empty string
     * @should not update given league name too long
     * @should not update given not begin with uppercase
     */
    @PutMapping(path = "/leagues", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String updateLeagueByForm(
        @RequestBody MultiValueMap<String, String> updatedLeague)
        throws IOException {

        System.out.println("Entering updateLeagueByForm");
        System.out.println(updatedLeague);
        int leagueId = Integer.parseInt(updatedLeague.getFirst("league-id"));
        String newLeagueName = updatedLeague.getFirst("new-league-name");
        JSONObject response = new JSONObject();
        response.put(JSON_STATUS, JSON_STATUS_FAILURE);

        try {
            leagueValidator.validateLeagueNameUpdate(leagueId, newLeagueName);
            leagueDbService.updateLeagueName((long) leagueId, newLeagueName);
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
     * Deletes an existing league on the database.
     * 
     * @param leagueId Id of the league that is being deleted.
     *
     * @return returns the following responses:- success League has been deleted
     *         failure Volleyball server is unavailable
     *
     * @should delete league
     */    
    @DeleteMapping(path = "/leagues/{leagueid}",
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String deleteLeague(@PathVariable("leagueid") long leagueId){
        JSONObject response = new JSONObject();
        try {
            leagueDbService.deleteLeague(leagueId);
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
