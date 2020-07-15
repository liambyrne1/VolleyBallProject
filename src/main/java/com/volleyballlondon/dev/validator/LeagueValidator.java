package com.volleyballlondon.dev.validator;

import java.util.List;

import com.volleyballlondon.exceptions.NameAlreadyExistsException;
import com.volleyballlondon.exceptions.NameInvalidCharactersException;
import com.volleyballlondon.exceptions.NameLengthException;
import com.volleyballlondon.exceptions.VolleyballException;
import com.volleyballlondon.persistence.model.League;
import com.volleyballlondon.persistence.services.LeagueDbService;

public class LeagueValidator extends Validator {

    public LeagueValidator(LeagueDbService leagueDbService) {
        this.leagueDbService = leagueDbService;
    }

    /** League database service */
    private LeagueDbService leagueDbService;

    /**
     * Validates a new league name for a create request.
     * @param newLeague name to be validated
     */
    public void validateLeagueNameCreate(String newLeague) throws VolleyballException {
        validateNameLength(newLeague);
        validateNameCharacters(newLeague);
        validateLeagueAlreadyExists(newLeague);
    }

    /**
     * Validates a league name for a update request.
     * User is allowed to change the case of the name.
     * @param leagueId 
     * @param newLeague name to be validated
     */
    public void validateLeagueNameUpdate(int leagueId, String newLeague) throws VolleyballException {
        validateNameLength(newLeague);
        validateNameCharacters(newLeague);
        validateLeagueAlreadyExists(leagueId, newLeague);
    }

    /**
     * Checks if new league name already exists. Performs a case insensitive search
     * for the league name on the database
     * 
     * @param newLeague
     *            - the new league name
     * @throws NameAlreadyExistsException
     *             if league name already exists
     */
    private void validateLeagueAlreadyExists(String newLeague) throws NameAlreadyExistsException {
        List<League> leagues = leagueDbService.findByNameIgnoreCase(newLeague);
        if (!leagues.isEmpty()) {
            throw new NameAlreadyExistsException(leagues.get(0).getName());
        }
    }

    /**
     * Checks if new league name already exists. Performs a case insensitive search
     * for the league name on the database. Allows the case of the updated
     * league name to change.
     * 
     * @param newLeague
     *            - the new league name
     * @throws NameAlreadyExistsException
     *             if league name already exists
     */
    private void validateLeagueAlreadyExists(int leagueId, String newLeague) throws NameAlreadyExistsException {
        List<League> leagues = leagueDbService.findByNameIgnoreCase(newLeague);
        if (!leagues.isEmpty()) {
            if (leagues.size() > 1) {
                throw new NameAlreadyExistsException(leagues.get(0).getName());
            }
            // Only one league found.
            // Check if it is the league being updated.
            if (leagues.get(0).getId() != leagueId) {
                throw new NameAlreadyExistsException(leagues.get(0).getName());
            }
        }
    }
}
