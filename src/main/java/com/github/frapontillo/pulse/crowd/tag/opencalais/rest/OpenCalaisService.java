package com.github.frapontillo.pulse.crowd.tag.opencalais.rest;

import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;

/**
 * @author Francesco Pontillo
 */
public interface OpenCalaisService {
    @POST("/permid/calais")
    @Headers({
            "content-type: text/raw",
            "outputFormat: application/json"
    })
    OpenCalaisResponse tag(@Body String text, @Header("x-calais-language") String language);
}
