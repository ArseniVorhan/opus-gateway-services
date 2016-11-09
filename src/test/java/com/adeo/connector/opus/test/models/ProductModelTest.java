package com.adeo.connector.opus.test.models;

import com.adeo.connector.opus.annotations.Field;
import com.adeo.connector.opus.annotations.Identifier;
import com.adeo.connector.opus.annotations.Mask;
import com.adeo.connector.opus.annotations.Multivalue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stievena on 28/10/16.
 */
public class ProductModelTest {
    @Identifier
    private String id;
    @Mask
    @Field("title")
    private String title;
    @Mask
    @Field("carac")
    @Multivalue
    private List characteristic = new ArrayList();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List getCharacteristic() {
        return characteristic;
    }

    public void setCharacteristic(List characteristic) {
        this.characteristic = characteristic;
    }
}
