package com.adeo.connector.opus.test.models;

import com.adeo.connector.opus.annotations.Field;
import com.adeo.connector.opus.annotations.Identifier;
import com.adeo.connector.opus.annotations.Mask;
import com.adeo.connector.opus.annotations.ModelType;

import java.util.List;

/**
 * Created by stievena on 14/10/16.
 */
public class CriterionModelTest {

    @Identifier
    private String id;

    @Mask
    @Field("Principal")
    private String name;

    @ModelType(modelClass = SegmentModelTest.class, path = "segments")
    private List<SegmentModelTest> segments;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SegmentModelTest> getSegments() {
        return segments;
    }

    public void setSegments(List<SegmentModelTest> segments) {
        this.segments = segments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
