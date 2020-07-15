package com.volleyballlondon.exceptions;

/**
 * Name already exists on the database
 */
public class NameAlreadyExistsException extends VolleyballException {

    public static final String ERROR_MESSAGE = " already exists.";

    /**
     * Creates the error message in the exception
     * @param message The name already on the database.
     * This could have different case letters. Must be displayed to user
    */
    public NameAlreadyExistsException(String message) {
        super(message + ERROR_MESSAGE);
    }
}
