package com.github.frapontillo.pulse.crowd.tag.opencalais.rest;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author Francesco Pontillo
 */
public class OpenCalaisResponseAdapter implements JsonDeserializer<OpenCalaisResponse> {
    @Override
    public OpenCalaisResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        OpenCalaisResponse response = new OpenCalaisResponse();

        for (Map.Entry<String, JsonElement> child : json.getAsJsonObject().entrySet()) {
            JsonObject jsonObject = child.getValue().getAsJsonObject();
            JsonElement type = jsonObject.get("_typeGroup");
            if (type != null && type.getAsString().equals("entities")) {
                response.getEntities().add(jsonObject.get("name").getAsString());
            }
        }

        return response;
    }
}
