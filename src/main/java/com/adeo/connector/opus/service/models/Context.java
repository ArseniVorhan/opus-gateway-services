package com.adeo.connector.opus.service.models;

/**
 * Created by stievena on 01/03/2017.
 */
public class Context {
    private String name;
    private String value;

    public Context(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
