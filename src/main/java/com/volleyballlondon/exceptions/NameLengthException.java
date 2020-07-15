package com.volleyballlondon.exceptions;

/**
 * Name is too long
 * The league length can only be up to MAX_NAME_LENGTH characters
 */
public class NameLengthException extends VolleyballException {

    /** Maximum length of a name */
    public static final int MAX_NAME_LENGTH = 30;

    public static final String ERROR_MESSAGE =
        " can only be " + MAX_NAME_LENGTH + " characters or less," +
        " input is ";

    /**
     * Creates the error message in the exception
     * @param message the name passed to the server.
     * @param nameLength the invalid length
     */
    public NameLengthException(String message, int nameLength) {
        super(message + ERROR_MESSAGE + nameLength + ".");
    }
}
