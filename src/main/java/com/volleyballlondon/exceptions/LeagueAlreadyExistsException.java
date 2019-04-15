package com.volleyballlondon.exceptions;

/**
 * League name already exists on the database
 */
public class LeagueAlreadyExistsException extends VolleyballException {

    public static final String C_ERROR_MESSAGE = " already exists.";

    /**
     * Creates the error message in the exception
     * @param p_message The league name already on the database.
     * This could have different case letters. Must be displayed to user
    */
    public LeagueAlreadyExistsException(String p_message) {
        super(p_message + C_ERROR_MESSAGE);
    }

}
