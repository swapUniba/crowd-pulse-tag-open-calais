package com.github.frapontillo.pulse.crowd.tag.opencalais.rest;

import com.github.frapontillo.pulse.util.ConfigUtil;
import retrofit.RequestInterceptor;

import java.util.Properties;

/**
 * @author Francesco Pontillo
 */
public class OpenCalaisInterceptor implements RequestInterceptor {
    private static final String PROP_API_KEY = "opencalais.key";
    private String API_KEY;

    private String getApiKey() {
        if (API_KEY == null) {
            Properties props = ConfigUtil.getPropertyFile(this.getClass(), "opencalais.properties");
            API_KEY = props.getProperty(PROP_API_KEY, "");
        }
        return API_KEY;
    }

    @Override public void intercept(RequestFacade request) {
        request.addHeader("x-ag-access-token", getApiKey());
    }
}
