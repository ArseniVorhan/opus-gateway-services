package com.adeo.connector.opus.test.models;

import com.adeo.connector.opus.annotations.Field;
import com.adeo.connector.opus.annotations.Identifier;
import com.adeo.connector.opus.annotations.Mask;

/**
 * Created by arseni.vorhan on 31.01.2017.
 */
public class FamilyTest {

    @Identifier
    private String id;
    @Mask
    @Field("name")
    private String name;
    @Mask
    @Field("linkedHowTos")
    private String linkedHowTos;
    @Mask
    @Field("linkedServices")
    private String linkedServices;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLinkedHowTos() {
        return linkedHowTos;
    }

    public void setLinkedHowTos(String linkedHowTos) {
        this.linkedHowTos = linkedHowTos;
    }

    public String getLinkedServices() {
        return linkedServices;
    }

    public void setLinkedServices(String linkedServices) {
        this.linkedServices = linkedServices;
    }
}
