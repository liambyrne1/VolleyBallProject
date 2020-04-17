package com.volleyballlondon.dev;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONObject;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

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
@Path("/LeagueService")
public class LeagueService {

	public LeagueService() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(LeagueDbService.class);
        ctx.refresh();
        leagueDbService = (LeagueDbService) ctx.getBean("mainBean");
        leagueValidator = new LeagueValidator(leagueDbService);
	}

    /** League database service */
    private LeagueDbService leagueDbService;

    /** League validators */
    private LeagueValidator leagueValidator;

    /** JSON status property, name and values */
    public static final String JSON_STATUS = "status";
    public static final Boolean JSON_STATUS_SUCCESS = new Boolean(true);
    public static final Boolean JSON_STATUS_FAILURE = new Boolean(false);

    /** JSON message property, name and values */
    public static final String JSON_MESSAGE = "message";
    public static final String JSON_MESSAGE_SUCCESS = "New League has been created.";
    public static final String JSON_MESSAGE_UNAVAILABLE = "Volleyball server is unavailable.";

    /**
     * Returns all the leagues from the database in alphabetical order.
     * Do not allow Eclipse to generate this test case. Remove '@'
     * should get all leagues
     */
    @GET
    @Path("/leagues")
    @Produces(MediaType.APPLICATION_JSON)
    public List<League> getLeagues(){
        return leagueDbService.findByOrderByName();
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
    @POST
    @Path("/leagues")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String createLeagueByForm(@FormParam("new-league-name") String newLeague,
        @Context HttpServletResponse servletResponse)
        throws IOException {

        JSONObject response = new JSONObject();
        response.put(JSON_STATUS, JSON_STATUS_FAILURE);

        try {
            leagueValidator.validateLeagueNameCreate(newLeague);
            leagueDbService.addLeague(newLeague);
            response.put(JSON_STATUS, JSON_STATUS_SUCCESS);
            response.put(JSON_MESSAGE, JSON_MESSAGE_SUCCESS);
        } catch (VolleyballException e) {
            response.put(JSON_MESSAGE, e.getMessage());
        }
        /*
        catch (Exception e) {
            response.put(JSON_MESSAGE, JSON_MESSAGE_UNAVAILABLE);
        }
        */
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
    @PUT
    @Path("/leagues")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String updateLeagueByForm(@FormParam("league-id") int leagueId,
        @FormParam("new-league-name") String newLeagueName,
        @Context HttpServletResponse servletResponse)
        throws IOException {

        JSONObject response = new JSONObject();
        response.put(JSON_STATUS, JSON_STATUS_FAILURE);

        try {
            leagueValidator.validateLeagueNameUpdate(leagueId, newLeagueName);
            leagueDbService.updateLeagueName((long) leagueId, newLeagueName);
            response.put(JSON_STATUS, JSON_STATUS_SUCCESS);
            response.put(JSON_MESSAGE, JSON_MESSAGE_SUCCESS);
        } catch (VolleyballException e) {
            response.put(JSON_MESSAGE, e.getMessage());
        } catch (Exception e) {
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
    @DELETE
    @Path("/leagues/{leagueid}")
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteLeague(@PathParam("leagueid") long leagueId){
        JSONObject response = new JSONObject();
        try {
            leagueDbService.deleteLeague(leagueId);
            response.put(JSON_STATUS, JSON_STATUS_SUCCESS);
            response.put(JSON_MESSAGE, JSON_MESSAGE_SUCCESS);
        } catch (Exception e) {
            response.put(JSON_STATUS, JSON_STATUS_FAILURE);
            response.put(JSON_MESSAGE, JSON_MESSAGE_UNAVAILABLE);
        }

        return response.toString();
    }
}
