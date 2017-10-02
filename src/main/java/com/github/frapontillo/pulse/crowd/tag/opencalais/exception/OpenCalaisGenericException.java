package com.github.frapontillo.pulse.crowd.tag.opencalais.exception;

/**
 * Exception thrown when a generic error happens while calling the OpenCalais API.
 *
 * @author Francesco Pontillo
 */
public class OpenCalaisGenericException extends Exception {
    public OpenCalaisGenericException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
