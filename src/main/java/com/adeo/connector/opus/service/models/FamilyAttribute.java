package com.adeo.connector.opus.service.models;

/**
 * Created by arseni.vorhan on 06.02.2017.
 */
public class FamilyAttribute {

    private String name;
    private FamilyAttributeValue values;

    public FamilyAttribute(String name, FamilyAttributeValue values) {
        this.name = name;
        this.values = values;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FamilyAttributeValue getValues() {
        return values;
    }

    public void setValues(FamilyAttributeValue values) {
        this.values = values;
    }
}
