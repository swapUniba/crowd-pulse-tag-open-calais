package com.github.frapontillo.pulse.crowd.tag.opencalais.rest;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * JSON converter from OpenCalais error to a {@link OpenCalaisError} class.
 *
 * @author Francesco Pontillo
 */
public class OpenCalaisErrorAdapter implements JsonDeserializer<OpenCalaisError> {
    @Override public OpenCalaisError deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        OpenCalaisError error = new OpenCalaisError();

        if (json.getAsJsonObject().get("error") != null) {
            error.setError(json.getAsJsonObject()
                    .getAsJsonObject("error").getAsJsonObject("status").get("errorCode").getAsString());
        }

        return error;
    }
}
