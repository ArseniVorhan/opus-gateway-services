package com.adeo.connector.opus.service.models;

/**
 * Created by arseni.vorhan on 06.02.2017.
 */
public class FamilyAttribute {

    private String name;
    private String[] values;

    public FamilyAttribute(String name, String[] values) {
        this.name = name;
        this.values = values;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getValues() {
        return values;
    }

    public void setValues(String[] values) {
        this.values = values;
    }
}
