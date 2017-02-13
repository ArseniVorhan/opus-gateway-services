package com.adeo.connector.opus.service.models;

/**
 * Created by stievena on 27/01/2017.
 */
public class Attribute {
    private String href;
    private Object[] value;

    public Attribute() {
    }

    public Attribute(String href, Object... value) {
        this.href = href;
        this.value = value;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public Object[] getValue() {
        return value;
    }

    public void setValue(Object[] value) {
        this.value = value;
    }
}
