package com.volleyballlondon.exceptions;

/**
 * Name contains invalid characters
 * Valid characters are upper/lower case letters, numbers, spaces, apostrophes,
 * brackets and dashes.
 */
public class NameInvalidCharactersException extends VolleyballException {

    public static final String ERROR_MESSAGE = " contains Invalid Characters";

    /**
     * Creates the error message in the exception
     * @param message The name passed to the server.
     */
    public NameInvalidCharactersException(String message) {
        super(message + ERROR_MESSAGE);
    }
}
