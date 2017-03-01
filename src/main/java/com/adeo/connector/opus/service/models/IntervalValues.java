package com.adeo.connector.opus.service.models;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by stievena on 27/02/2017.
 */
public class IntervalValues extends FamilyAttributeValue {

    public IntervalValues(String... values) {
        super(values);
    }

    @Override
    public String formatValues() {
        if (this.values.length == 2) {
            return Stream.of(this.values).collect(Collectors.joining("%20TO%20", "[", "]"));
        }
        return "";
    }
}
