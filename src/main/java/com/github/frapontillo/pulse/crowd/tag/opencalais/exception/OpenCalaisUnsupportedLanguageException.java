package com.github.frapontillo.pulse.crowd.tag.opencalais.exception;

/**
 * Exception thrown when an unsupported language is submitted to the OpenCalais Web Service.
 *
 * @author Francesco Pontillo
 */
public class OpenCalaisUnsupportedLanguageException extends Exception {
    public OpenCalaisUnsupportedLanguageException(Throwable throwable) {
        super(throwable);
    }
}
