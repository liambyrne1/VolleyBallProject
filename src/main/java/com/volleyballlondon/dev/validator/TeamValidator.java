package com.volleyballlondon.dev.validator;

import java.util.List;

import com.volleyballlondon.exceptions.NameAlreadyExistsException;
import com.volleyballlondon.exceptions.NameInvalidCharactersException;
import com.volleyballlondon.exceptions.NameLengthException;
import com.volleyballlondon.exceptions.VolleyballException;
import com.volleyballlondon.persistence.model.Team;
import com.volleyballlondon.persistence.services.TeamDbService;

public class TeamValidator extends Validator {

    public TeamValidator(TeamDbService teamDbService) {
        this.teamDbService = teamDbService;
    }

    /** Team database service */
    private TeamDbService teamDbService;

    /**
     * Validates a new team name for a create request.
     * @param newTeam name to be validated
     */
    public void validateTeamNameCreate(String newTeam) throws VolleyballException {
        validateNameLength(newTeam);
        validateNameCharacters(newTeam);
        validateTeamAlreadyExists(newTeam);
    }

    /**
     * Validates a team name for a update request.
     * User is allowed to change the case of the name.
     * @param teamId 
     * @param newTeam name to be validated
     */
    public void validateTeamNameUpdate(int teamId, String newTeam) throws VolleyballException {
        validateNameLength(newTeam);
        validateNameCharacters(newTeam);
        validateTeamAlreadyExists(teamId, newTeam);
    }

    /**
     * Checks if new team name already exists. Performs a case insensitive search
     * for the team name on the database
     * 
     * @param newTeam
     *            - the new team name
     * @throws NameAlreadyExistsException
     *             if team name already exists
     */
    private void validateTeamAlreadyExists(String newTeam) throws NameAlreadyExistsException {
        List<Team> teams = teamDbService.findByNameIgnoreCase(newTeam);
        if (!teams.isEmpty()) {
            throw new NameAlreadyExistsException(teams.get(0).getName());
        }
    }

    /**
     * Checks if new team name already exists. Performs a case insensitive search
     * for the team name on the database. Allows the case of the updated
     * team name to change.
     * 
     * @param newTeam
     *            - the new team name
     * @throws NameAlreadyExistsException
     *             if team name already exists
     */
    private void validateTeamAlreadyExists(int teamId, String newTeam) throws NameAlreadyExistsException {
        List<Team> teams = teamDbService.findByNameIgnoreCase(newTeam);
        if (!teams.isEmpty()) {
            if (teams.size() > 1) {
                throw new NameAlreadyExistsException(teams.get(0).getName());
            }
            // Only one team found.
            // Check if it is the team being updated.
            if (teams.get(0).getId() != teamId) {
                throw new NameAlreadyExistsException(teams.get(0).getName());
            }
        }
    }
}
