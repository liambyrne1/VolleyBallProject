package com.volleyballlondon.dev.validator;

import java.util.List;

import com.volleyballlondon.exceptions.LeagueAlreadyExistsException;
import com.volleyballlondon.exceptions.LeagueInvalidCharactersException;
import com.volleyballlondon.exceptions.LeagueLengthException;
import com.volleyballlondon.exceptions.VolleyballException;
import com.volleyballlondon.persistence.model.League;
import com.volleyballlondon.persistence.services.LeagueDbService;

public class LeagueValidator {

    public LeagueValidator(LeagueDbService leagueDbService) {
        this.leagueDbService = leagueDbService;
    }

    /** League database service */
    private LeagueDbService leagueDbService;

    /**
     * Regular expression defining valid characters for a league name
     * Valid characters are upper/lower case letters, numbers, spaces,
     * apostrophes, brackets and dashes.
     */
    public static final String VALID_LEAGUE_CHARACTERS = "[a-zA-Z0-9 '()-]+";

    /**
     * Regular expression matching the first character of a league name
     */
    public static final String VALID_LEAGUE_FIRST_CHAR = "[A-Z]";

    /**
     * Validates a new league name for a create request.
     * @param newLeague name to be validated
     */
    public void validateLeagueNameCreate(String newLeague) throws VolleyballException {
        validateLeagueLength(newLeague);
        validateLeagueCharacters(newLeague);
        validateLeagueAlreadyExists(newLeague);
    }

    /**
     * Validates a league name for a update request. Do not check if league already
     * exists. User is allowed to change the case of the name.
     * @param leagueId 
     * @param newLeague name to be validated
     */
    public void validateLeagueNameUpdate(int leagueId, String newLeague) throws VolleyballException {
        validateLeagueLength(newLeague);
        validateLeagueCharacters(newLeague);
        validateLeagueAlreadyExists(leagueId, newLeague);
    }

    /**
     * Checks if new league name already exists. Performs a case insensitive search
     * for the league name on the database
     * 
     * @param newLeague
     *            - the new league name
     * @throws LeagueAlreadyExistsException
     *             if league name already exists
     */
    private void validateLeagueAlreadyExists(String newLeague) throws LeagueAlreadyExistsException {
        List<League> leagues = leagueDbService.findByNameIgnoreCase(newLeague);
        if (!leagues.isEmpty()) {
            throw new LeagueAlreadyExistsException(leagues.get(0).getName());
        }
    }

    /**
     * Checks if new league name already exists. Performs a case insensitive search
     * for the league name on the database. Allows the case of the updated
     * league name to change.
     * 
     * @param newLeague
     *            - the new league name
     * @throws LeagueAlreadyExistsException
     *             if league name already exists
     */
    private void validateLeagueAlreadyExists(int leagueId, String newLeague) throws LeagueAlreadyExistsException {
        List<League> leagues = leagueDbService.findByNameIgnoreCase(newLeague);
        if (!leagues.isEmpty()) {
            if (leagues.size() > 1) {
                throw new LeagueAlreadyExistsException(leagues.get(0).getName());
            }
            // Only one league found.
            // Check if it is the league being updated.
            if (leagues.get(0).getId() != leagueId) {
                throw new LeagueAlreadyExistsException(leagues.get(0).getName());
            }
        }
    }

    /**
     * Checks if new league name contains only valid characters. Matches the league
     * name to a regular expression
     * 
     * @param newLeague
     *            - the new league name
     * @throws LeagueInvalidCharactersException
     *             League name contains invalid characters.
     */
    private void validateLeagueCharacters(String newLeague) throws LeagueInvalidCharactersException {
        if ("".equals(newLeague)) {
            throw new LeagueInvalidCharactersException();
        }

        String firstChar = newLeague.substring(0, 1);

        if (!firstChar.matches(VALID_LEAGUE_FIRST_CHAR)) {
            throw new LeagueInvalidCharactersException();
        }
        if (!newLeague.matches(VALID_LEAGUE_CHARACTERS)) {
            throw new LeagueInvalidCharactersException();
        }
    }

    /**
     * Checks if new league length is too long.
     * The league length can only be up to 30 characters
     * 
     * @param newLeague
     *            - the new league name
     * @throws LeagueLengthException
     *             League name is too long
     */
    private void validateLeagueLength(String newLeague) throws LeagueLengthException {
        int newLeagueLength = newLeague.length();

        if (newLeagueLength > LeagueLengthException.MAX_LEAGUE_LENGTH) {
            throw new LeagueLengthException(newLeagueLength);
        }
    }
}
