package com.github.frapontillo.pulse.crowd.tag.opencalais.rest;

import java.util.ArrayList;
import java.util.List;

/**
 * Generic OpenCalais response (entities only).
 *
 * @author Francesco Pontillo
 */
public class OpenCalaisResponse {
    private List<String> entities;

    public OpenCalaisResponse() {
        entities = new ArrayList<>();
    }

    public List<String> getEntities() {
        return entities;
    }

    public void setEntities(List<String> entities) {
        this.entities = entities;
    }
}
