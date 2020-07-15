package com.volleyballlondon.dev.validator;

import java.util.List;

import com.volleyballlondon.exceptions.NameAlreadyExistsException;
import com.volleyballlondon.exceptions.NameInvalidCharactersException;
import com.volleyballlondon.exceptions.NameLengthException;
import com.volleyballlondon.exceptions.VolleyballException;
import com.volleyballlondon.persistence.model.Club;
import com.volleyballlondon.persistence.services.ClubDbService;

public class ClubValidator extends Validator {

    public ClubValidator(ClubDbService clubDbService) {
        this.clubDbService = clubDbService;
    }

    /** Club database service */
    private ClubDbService clubDbService;

    /**
     * Validates a new club name for a create request.
     * @param newClub name to be validated
     */
    public void validateClubNameCreate(String newClub) throws VolleyballException {
        validateNameLength(newClub);
        validateNameCharacters(newClub);
        validateClubAlreadyExists(newClub);
    }

    /**
     * Validates a club name for a update request.
     * User is allowed to change the case of the name.
     * @param clubId 
     * @param newClub name to be validated
     */
    public void validateClubNameUpdate(int clubId, String newClub) throws VolleyballException {
        validateNameLength(newClub);
        validateNameCharacters(newClub);
        validateClubAlreadyExists(clubId, newClub);
    }

    /**
     * Checks if new club name already exists. Performs a case insensitive search
     * for the club name on the database
     * 
     * @param newClub
     *            - the new club name
     * @throws NameAlreadyExistsException
     *             if club name already exists
     */
    private void validateClubAlreadyExists(String newClub) throws NameAlreadyExistsException {
        List<Club> clubs = clubDbService.findByNameIgnoreCase(newClub);
        if (!clubs.isEmpty()) {
            throw new NameAlreadyExistsException(clubs.get(0).getName());
        }
    }

    /**
     * Checks if new club name already exists. Performs a case insensitive search
     * for the club name on the database. Allows the case of the updated
     * club name to change.
     * 
     * @param newClub
     *            - the new club name
     * @throws NameAlreadyExistsException
     *             if club name already exists
     */
    private void validateClubAlreadyExists(int clubId, String newClub) throws NameAlreadyExistsException {
        List<Club> clubs = clubDbService.findByNameIgnoreCase(newClub);
        if (!clubs.isEmpty()) {
            if (clubs.size() > 1) {
                throw new NameAlreadyExistsException(clubs.get(0).getName());
            }
            // Only one club found.
            // Check if it is the club being updated.
            if (clubs.get(0).getId() != clubId) {
                throw new NameAlreadyExistsException(clubs.get(0).getName());
            }
        }
    }
}
