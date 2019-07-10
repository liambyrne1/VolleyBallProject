package com.volleyballlondon.exceptions;

/**
 * League name contains invalid characters
 * Valid characters are upper/lower case letters, numbers, spaces, apostrophes,
 * brackets and dashes.
 */
public class LeagueInvalidCharactersException extends VolleyballException {

    public static final String ERROR_MESSAGE = "Name contains Invalid Characters";

    /**
     * Creates the error message in the exception
     */
    public LeagueInvalidCharactersException() {
        super(ERROR_MESSAGE);
    }
}
