package com.volleyballlondon.dev;

public abstract class VolleyballService {

    /** JSON status property, name and values */
    public static final String JSON_STATUS = "status";
    public static final Boolean JSON_STATUS_SUCCESS = new Boolean(true);
    public static final Boolean JSON_STATUS_FAILURE = new Boolean(false);

    /** JSON message property, name and values */
    public static final String JSON_MESSAGE = "message";
    public static final String JSON_MESSAGE_UNAVAILABLE = "Volleyball server is unavailable.";

    protected void logException(Exception e) {
            System.out.println();
            System.out.println("Message:");
            System.out.println(e.getMessage());
            System.out.println();
            System.out.println("Stack Trace:");
            e.printStackTrace();
    }
}
