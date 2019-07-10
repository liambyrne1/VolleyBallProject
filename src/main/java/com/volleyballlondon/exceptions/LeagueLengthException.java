package com.volleyballlondon.exceptions;

/**
 * League name is too long
 * The league length can only be up to 30 characters
 */
public class LeagueLengthException extends VolleyballException {

    /** Maximum length of a league name */
    public static final int MAX_LEAGUE_LENGTH = 30;

    public static final String ERROR_MESSAGE =
        "League can only be " + MAX_LEAGUE_LENGTH + " characters or less," +
        " input is ";

    /**
     * Creates the error message in the exception
     * @param leagueLength the invalid length
     */
    public LeagueLengthException(int leagueLength) {
        super(ERROR_MESSAGE + leagueLength + ".");
    }
}
