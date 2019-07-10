package com.volleyballlondon.exceptions;

/**
 * Super class of all Volleyballlondon exceptions
 */
public class VolleyballException extends Exception {

    /** Error message in exception */
	private String message;

    public VolleyballException(String message)
    {
        this.message = message;
    }

	public String getMessage() {
		return message;
	}

	public void printErrorMessage() {
		System.out.println("VolleyballException: " + message);
	}
}
