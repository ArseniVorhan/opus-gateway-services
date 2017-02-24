package com.adeo.connector.opus.service.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stievena on 25/01/2017.
 */
public class OpusObject {
    private Metadata metadata = new Metadata();
    private Model model = new Model();
    private List<Attribute> attribute = new ArrayList<>();
    private String correlationId;

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public List<Attribute> getAttribute() {
        return attribute;
    }

    public void setAttribute(List<Attribute> attribute) {
        this.attribute = attribute;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

}
