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

import com.volleyballlondon.dev.validator.TeamValidator;
import com.volleyballlondon.exceptions.VolleyballException;
import com.volleyballlondon.persistence.model.Team;
import com.volleyballlondon.persistence.services.TeamDbService;

/**
 * Provides the CRUD services for a team Returns a JSON response which has two
 * properties:- status message
 *
 * The status property has the following boolean values:- true the service is
 * successful false the service fails
 *
 * The message is a string property which gives a description of the failure.
 */
@Controller
public class TeamService extends VolleyballService {

	public TeamService() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(TeamDbService.class);
        ctx.refresh();
        teamDbService = (TeamDbService) ctx.getBean("teamDbBean");
        teamValidator = new TeamValidator(teamDbService);
	}

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    /** Team database service */
    private TeamDbService teamDbService;

    /** Team validators */
    private TeamValidator teamValidator;

    public static final String JSON_MESSAGE_SUCCESS_CREATED
        = "New Team has been created.";

    public static final String JSON_MESSAGE_SUCCESS_UPDATED
        = "Team has been updated.";

    public static final String JSON_MESSAGE_SUCCESS_DELETED
        = " has been deleted.";

    /**
     * Returns all the teams of a given club from the database in alphabetical order.
     * Do not allow Eclipse to generate this test case. Remove '@'
     * should get all team of a given club
     */
    @GetMapping(path = "/club/{clubid}/teams",
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Team> getTeamsOfClub(@PathVariable("clubid") long clubId){
        List<Team> teams = new ArrayList<>();
        try {
            teams = teamDbService.findByClubIdOrderByName(clubId);
        } catch (Exception e) {
            logException(e);
        }
        return teams;
    }

    /**
     * Creates a new team of a club on the database.
     * 
     * @param newTeam
     *            - the new team name
     * @param  clubId
     *            - the club id of the team.
     * @return returns the following responses:- success New Team has been
     *         created failure Team Name contains Invalid Characters failure
     *         Team already exists failure Volleyball server is unavailable
     *
     * @should create new team
     * @should create new team with all valid characters
     * @should fail if team already exists
     * @should fail given invalid character
     * @should fail given empty string
     * @should fail given team name too long
     * @should fail given not begin with uppercase
     */
    @PostMapping(path = "/club/{clubid}/teams",
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String createTeamByForm(@RequestParam("new-team-name") String newTeam,
        @PathVariable("clubid") long clubId)
        throws IOException {

        JSONObject response = new JSONObject();
        response.put(JSON_STATUS, JSON_STATUS_FAILURE);

        try {
            teamValidator.validateTeamNameCreate(newTeam);
            teamDbService.addTeam(newTeam, clubId);
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
     * Updates an existing team on the database. Allow user to change
     * the case of the team name being updated.
     * 
     * @param teamId Id of the team that is being updated.
     * @param newTeamName
     *            - the new team name
     * @return returns the following responses:- success New Team has been
     *         updated failure Team Name contains Invalid Characters failure
     *         Team already exists failure Volleyball server is unavailable
     *
     * @should update team
     * @should update team with all valid characters
     * @should update team with different case
     * @should not update if team already exists
     * @should not update given invalid character
     * @should not update given empty string
     * @should not update given team name too long
     * @should not update given not begin with uppercase
     */
    @PutMapping(path = "/club/{clubid}/teams",
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String updateTeamByForm(
        @RequestBody MultiValueMap<String, String> updatedTeam)
        throws IOException {

        System.out.println("Entering updateTeamByForm");
        System.out.println(updatedTeam);
        int teamId = Integer.parseInt(updatedTeam.getFirst("team-id"));
        String newTeamName = updatedTeam.getFirst("new-team-name");
        JSONObject response = new JSONObject();
        response.put(JSON_STATUS, JSON_STATUS_FAILURE);

        try {
            teamValidator.validateTeamNameUpdate(teamId, newTeamName);
            teamDbService.updateTeamName((long) teamId, newTeamName);
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
     * Deletes an existing team on the database.
     * 
     * @param teamId Id of the team that is being deleted.
     *
     * @return returns the following responses:- success Team has been deleted
     *         failure Volleyball server is unavailable
     *
     * @should delete team
     */    
    @DeleteMapping(path = "/club/{clubid}/teams/{teamid}",
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String deleteTeam(@PathVariable("teamid") long teamId){
        JSONObject response = new JSONObject();
        try {
            teamDbService.deleteTeam(teamId);
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
