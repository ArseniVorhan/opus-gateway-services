package com.adeo.connector.opus.service.models;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by stievena on 27/02/2017.
 */
public class FixedValues extends FamilyAttributeValue {

    public FixedValues(String... values) {
        super(values);
    }

    @Override
    public String formatValues() {
        return Stream.of(this.values).collect(Collectors.joining("%20OR%20", "(", ")"));
    }
}
