package com.github.frapontillo.pulse.crowd.tag.opencalais.rest;

import com.github.frapontillo.pulse.crowd.tag.opencalais.exception
        .OpenCalaisAPILimitReachedException;
import com.github.frapontillo.pulse.crowd.tag.opencalais.exception.OpenCalaisUnsupportedLanguageException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit.ErrorHandler;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.ConversionException;
import retrofit.converter.GsonConverter;

import java.util.Arrays;
import java.util.List;

/**
 * Custom OpenCalais error handler.
 *
 * @author Francesco Pontillo
 */
public class OpenCalaisErrorHandler implements ErrorHandler {
    private final List<String> FLOW_THROUGH_LANG_ERRORS = Arrays.asList("Unsupported-Language", "Unrecognized-Language");

    @Override public Throwable handleError(RetrofitError cause) {
        if (cause.getResponse() != null && cause.getResponse().getStatus() == 429) {
            return new OpenCalaisAPILimitReachedException(cause);
        }

        Response r = cause.getResponse();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(OpenCalaisError.class, new OpenCalaisErrorAdapter())
                .create();
        GsonConverter converter = new GsonConverter(gson);

        try {
            OpenCalaisError error = (OpenCalaisError) converter.fromBody(r.getBody(), OpenCalaisError.class);
            if (FLOW_THROUGH_LANG_ERRORS.contains(error.getError())) {
                return new OpenCalaisUnsupportedLanguageException(cause);
            }
        } catch (ConversionException ignored) {
        }

        return cause;
    }
}
