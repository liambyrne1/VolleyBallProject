package com.volleyballlondon.dev;

import java.io.IOException;
import java.util.ArrayList;
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
@Path("/ClubService")
public class ClubService extends VolleyballService {

	public ClubService() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(ClubDbService.class);
        ctx.refresh();
        clubDbService = (ClubDbService) ctx.getBean("clubDbBean");
        clubValidator = new ClubValidator(clubDbService);
	}

    /** Club database service */
    private ClubDbService clubDbService;

    /** Club validators */
    private ClubValidator clubValidator;

    public static final String JSON_MESSAGE_SUCCESS = "New Club has been created.";
    /**
     * Returns all the clubs from the database in alphabetical order.
     * Do not allow Eclipse to generate this test case. Remove '@'
     * should get all clubs
     */
    @GET
    @Path("/clubs")
    @Produces(MediaType.APPLICATION_JSON)
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
    @POST
    @Path("/clubs")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String createClubByForm(@FormParam("new-club-name") String newClub,
        @Context HttpServletResponse servletResponse)
        throws IOException {

        JSONObject response = new JSONObject();
        response.put(JSON_STATUS, JSON_STATUS_FAILURE);

        try {
            clubValidator.validateClubNameCreate(newClub);
            clubDbService.addClub(newClub);
            response.put(JSON_STATUS, JSON_STATUS_SUCCESS);
            response.put(JSON_MESSAGE, JSON_MESSAGE_SUCCESS);
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
    @PUT
    @Path("/clubs")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String updateClubByForm(@FormParam("club-id") int clubId,
        @FormParam("new-club-name") String newClubName,
        @Context HttpServletResponse servletResponse)
        throws IOException {

        JSONObject response = new JSONObject();
        response.put(JSON_STATUS, JSON_STATUS_FAILURE);

        try {
            clubValidator.validateClubNameUpdate(clubId, newClubName);
            clubDbService.updateClubName((long) clubId, newClubName);
            response.put(JSON_STATUS, JSON_STATUS_SUCCESS);
            response.put(JSON_MESSAGE, JSON_MESSAGE_SUCCESS);
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
    @DELETE
    @Path("/clubs/{clubid}")
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteClub(@PathParam("clubid") long clubId){
        JSONObject response = new JSONObject();
        try {
            clubDbService.deleteClub(clubId);
            response.put(JSON_STATUS, JSON_STATUS_SUCCESS);
            response.put(JSON_MESSAGE, JSON_MESSAGE_SUCCESS);
        } catch (Exception e) {
            logException(e);
            response.put(JSON_STATUS, JSON_STATUS_FAILURE);
            response.put(JSON_MESSAGE, JSON_MESSAGE_UNAVAILABLE);
        }

        return response.toString();
    }
}
