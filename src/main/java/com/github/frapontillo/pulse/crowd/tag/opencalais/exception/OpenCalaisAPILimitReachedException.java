package com.github.frapontillo.pulse.crowd.tag.opencalais.exception;

/**
 * Exception thrown when the OpenCalais API limit has been reached.
 *
 * @author Francesco Pontillo
 */
public class OpenCalaisAPILimitReachedException extends Exception {
    public OpenCalaisAPILimitReachedException(Throwable throwable) {
        super(throwable);
    }
}
