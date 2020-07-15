package com.volleyballlondon.dev.validator;

import com.volleyballlondon.exceptions.NameInvalidCharactersException;
import com.volleyballlondon.exceptions.NameLengthException;

public class Validator {

    /**
     * Regular expression defining valid characters for a league name
     * Valid characters are upper/lower case letters, numbers, spaces,
     * apostrophes, brackets and dashes.
     */
    public static final String VALID_LEAGUE_CHARACTERS = "[a-zA-Z0-9 '()-]+";

    /**
     * Regular expression matching the first character of a league name
     */
    public static final String VALID_LEAGUE_FIRST_CHAR = "[A-Z]";

    /**
     * Checks if new name contains only valid characters. Matches the
     * name to a regular expression
     * 
     * @param newName
     *            - the new name
     * @throws NameInvalidCharactersException
     *             Name contains invalid characters.
     */
    protected void validateNameCharacters(String newName)
        throws NameInvalidCharactersException {
        if ("".equals(newName)) {
            throw new NameInvalidCharactersException(newName);
        }

        String firstChar = newName.substring(0, 1);

        if (!firstChar.matches(VALID_LEAGUE_FIRST_CHAR)) {
            throw new NameInvalidCharactersException(newName);
        }
        
        if (!newName.matches(VALID_LEAGUE_CHARACTERS)) {
            throw new NameInvalidCharactersException(newName);
        }
    }

    /**
     * Checks if new name length is too long.
     * The name length can only be up to 30 characters
     * 
     * @param newName
     *            - the new name
     * @throws NameLengthException
     *             Name is too long
     */
    protected void validateNameLength(String newName) throws NameLengthException {
        int newNameLength = newName.length();

        if (newNameLength > NameLengthException.MAX_NAME_LENGTH) {
            throw new NameLengthException(newName, newNameLength);
        }
    }
}
