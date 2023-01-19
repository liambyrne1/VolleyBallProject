package com.volleyballlondon.exceptions;

/**
 * User already exists on the database
 */
public class UserAlreadyExistsException extends VolleyballException {

    public static final String ERROR_MESSAGE1 = "Email  ";
    public static final String ERROR_MESSAGE2 = "  already exists.";

    /**
     * Creates the error message in the exception
     * @param message The name already on the database.
     * This could have different case letters. Must be displayed to user
    */
    public UserAlreadyExistsException(String message) {
        super(ERROR_MESSAGE1 + message + ERROR_MESSAGE2);
    }
}
