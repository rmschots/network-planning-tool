package com.ugent.networkplanningtool.io;

/**
 * Exception thrown by a AbstractASyncTask task
 */
public class ASyncTaskException extends Exception {
    /**
     * Constructor that accepts a message
     * @param message the message of the exception
     */
    public ASyncTaskException(String message)
    {
        super(message);
    }
}
