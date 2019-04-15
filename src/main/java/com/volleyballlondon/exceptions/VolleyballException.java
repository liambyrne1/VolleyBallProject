package com.volleyballlondon.exceptions;

/**
 * Super class of all Volleyballlondon exceptions
 */
public class VolleyballException extends Exception {

    /** Error message in exception */
	private String i_message;

    public VolleyballException(String p_message)
    {
        i_message = p_message;
    }

	public String getMessage() {
		return i_message;
	}

	public void printErrorMessage() {
		System.out.println("VolleyballException: " + i_message);
	}
}
