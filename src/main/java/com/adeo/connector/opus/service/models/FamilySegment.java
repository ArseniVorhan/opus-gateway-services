package com.adeo.connector.opus.service.models;

public class FamilySegment {
    private String id;
    private boolean enabled;

    public FamilySegment(String id, boolean enabled) {
        this.id = id;
        this.enabled = enabled;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}