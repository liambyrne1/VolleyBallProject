package com.volleyballlondon.dev;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONObject;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.volleyballlondon.dev.validator.LeagueValidator;
import com.volleyballlondon.exceptions.LeagueAlreadyExistsException;
import com.volleyballlondon.exceptions.LeagueInvalidCharactersException;
import com.volleyballlondon.exceptions.LeagueLengthException;
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
    public String createLeagueByForm(@FormParam("league") String newLeague, @Context HttpServletResponse servletResponse)
            throws IOException {
        JSONObject response = new JSONObject();
        response.put(JSON_STATUS, JSON_STATUS_FAILURE);

        try {
            leagueValidator.validateLeagueName(newLeague);
            leagueDbService.addLeague(newLeague);
            response.put(JSON_STATUS, JSON_STATUS_SUCCESS);
            response.put(JSON_MESSAGE, JSON_MESSAGE_SUCCESS);
        } catch (VolleyballException e) {
            response.put(JSON_MESSAGE, e.getMessage());
        } catch (Exception e) {
            response.put(JSON_MESSAGE, JSON_MESSAGE_UNAVAILABLE);
        }
        return response.toString();
    }

}
