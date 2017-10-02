package com.github.frapontillo.pulse.crowd.tag.opencalais.rest;

import com.github.frapontillo.pulse.util.StringUtil;

/**
 * @author fra
 */
public class OpenCalaisError {
    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean hasErrored() {
        return !StringUtil.isNullOrEmpty(error);
    }
}
