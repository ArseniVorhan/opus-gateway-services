package com.adeo.connector.opus.test.models;

import com.adeo.connector.opus.annotations.Field;
import com.adeo.connector.opus.annotations.Identifier;
import com.adeo.connector.opus.annotations.Mask;

/**
 * Created by stievena on 14/10/16.
 */
public class SegmentModelTest {

    @Identifier
    private String id;

    @Mask
    @Field("Principal")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
