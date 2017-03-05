package com.adeo.connector.opus.service.models;

/**
 * Created by stievena on 27/02/2017.
 */
public abstract class FamilyAttributeValue {

    protected String[] values;

    public FamilyAttributeValue(String... values) {
        this.values = values;
    }

    public abstract String formatValues();
}
