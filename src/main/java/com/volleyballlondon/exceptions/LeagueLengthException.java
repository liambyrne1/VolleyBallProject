package com.volleyballlondon.exceptions;

/**
 * League name is too long
 * The league length can only be up to 30 characters
 */
public class LeagueLengthException extends VolleyballException {

    /** Maximum length of a league name */
    public static final int C_MAX_LEAGUE_LENGTH = 30;

    public static final String C_ERROR_MESSAGE =
        "League can only be " + C_MAX_LEAGUE_LENGTH + " characters or less," +
        " input is ";

    /**
     * Creates the error message in the exception
     */
    public LeagueLengthException(int p_leagueLength) {
        super(C_ERROR_MESSAGE + p_leagueLength + ".");
    }
}
