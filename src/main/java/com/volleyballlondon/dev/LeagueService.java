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
        System.out.println("Load context");
        leagueDbService = (LeagueDbService) ctx.getBean("mainBean");
	}

    /** JSON status property, name and values */
    public static final String C_JSON_STATUS = "status";
    public static final Boolean C_JSON_STATUS_SUCCESS = new Boolean(true);
    public static final Boolean C_JSON_STATUS_FAILURE = new Boolean(false);

    /** JSON message property, name and values */
    public static final String C_JSON_MESSAGE = "message";
    public static final String C_JSON_MESSAGE_SUCCESS = "New League has been created.";
    public static final String C_JSON_MESSAGE_UNAVAILABLE = "Volleyball server is unavailable.";

    /**
     * Regular expression defining valid characters for a league name
     * Valid characters are upper/lower case letters, numbers, spaces,
     * apostrophes, brackets and dashes.
     */
    public static final String C_VALID_LEAGUE_CHARACTERS = "[a-zA-Z0-9 '()-]+";

    /**
     * Regular expression matching the first character of a league name
     */
    public static final String C_VALID_LEAGUE_FIRST_CHAR = "[A-Z]";

    /** League database service */
    private LeagueDbService leagueDbService;

    /**
     * Creates a new league on the database.
     * 
     * @param p_league
     *            - the league name
     * @return returns the following responses:- success New League has been
     *         created failure League Name contains Invalid Characters failure
     *         League already exists failure Volleyball server is unavailable
     */
    @POST
    @Path("/leagues")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String createLeagueByForm(@FormParam("league") String p_league, @Context HttpServletResponse servletResponse)
            throws IOException {
        JSONObject l_response = new JSONObject();
        l_response.put(C_JSON_STATUS, C_JSON_STATUS_FAILURE);

        try {
            validateLeagueLength(p_league);
            validateLeagueCharacters(p_league);
            validateLeagueAlreadyExists(p_league);
            leagueDbService.addLeague(p_league);
            l_response.put(C_JSON_STATUS, C_JSON_STATUS_SUCCESS);
            l_response.put(C_JSON_MESSAGE, C_JSON_MESSAGE_SUCCESS);
        } catch (VolleyballException l_e) {
            l_response.put(C_JSON_MESSAGE, l_e.getMessage());
        } /*catch (Exception l_e) {
            l_response.put(C_JSON_MESSAGE, C_JSON_MESSAGE_UNAVAILABLE);
        }*/
        return l_response.toString();
    }

    /**
     * Checks if league name already exists. Performs a case insensitive search
     * for the league name on the database
     * 
     * @param p_league
     *            - the league name
     * @throws LeagueAlreadyExistsException
     *             if league name already exists
     */
    private void validateLeagueAlreadyExists(String p_league) throws LeagueAlreadyExistsException {
        List<League> l_leagues = leagueDbService.findByNameIgnoreCase(p_league);
        if (!l_leagues.isEmpty()) {
            throw new LeagueAlreadyExistsException(l_leagues.get(0).getName());
        }
    }

    /**
     * Checks if league name contains only valid characters. Matches the league
     * name to a regular expression
     * 
     * @param p_league
     *            - the league name
     * @throws LeagueInvalidCharactersException
     *             League name contains invalid characters.
     */
    private void validateLeagueCharacters(String p_league) throws LeagueInvalidCharactersException {
        if (p_league.equals("")) {
            throw new LeagueInvalidCharactersException();
        }

        String l_firstChar = p_league.substring(0, 1);

        if (!l_firstChar.matches(C_VALID_LEAGUE_FIRST_CHAR)) {
            throw new LeagueInvalidCharactersException();
        }
        if (!p_league.matches(C_VALID_LEAGUE_CHARACTERS)) {
            throw new LeagueInvalidCharactersException();
        }
    }

    /**
     * Checks if league length is too long.
     * The league length can only be up to 30 characters
     * 
     * @param p_league
     *            - the league name
     * @throws LeagueLengthException
     *             League name is too long
     */
    private void validateLeagueLength(String p_league) throws LeagueLengthException {
        int l_leagueLength = p_league.length();

        if (l_leagueLength > LeagueLengthException.C_MAX_LEAGUE_LENGTH) {
            throw new LeagueLengthException(l_leagueLength);
        }
    }

}
