package com.adeo.connector.opus.test.models;

import com.adeo.connector.opus.annotations.Field;
import com.adeo.connector.opus.annotations.Mask;

/**
 * Created by arseni.vorhan on 16.01.2017.
 */
public class RegionTest {

    @Mask
    @Field("regionId")
    private String regionId;
    @Mask
    @Field("regionName")
    private String regionName;
    @Mask
    @Field("regionPriority")
    private Integer regionPriority;
    @Mask
    @Field("regionLatitude")
    private String regionLatitude;
    @Mask
    @Field("regionLongitude")
    private String regionLongitude;
    @Mask
    @Field("regionMapZoom")
    private String regionMapZoom;

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public Integer getRegionPriority() {
        return regionPriority;
    }

    public void setRegionPriority(Integer regionPriority) {
        this.regionPriority = regionPriority;
    }

    public String getRegionLatitude() {
        return regionLatitude;
    }

    public void setRegionLatitude(String regionLatitude) {
        this.regionLatitude = regionLatitude;
    }

    public String getRegionLongitude() {
        return regionLongitude;
    }

    public void setRegionLongitude(String regionLongitude) {
        this.regionLongitude = regionLongitude;
    }

    public String getRegionMapZoom() {
        return regionMapZoom;
    }

    public void setRegionMapZoom(String regionMapZoom) {
        this.regionMapZoom = regionMapZoom;
    }

    @Override
    public String toString() {
        return "RegionTest{"
                + "regionId='" + regionId + '\''
                + "regionName='" + regionName + '\''
                + "regionPriority='" + regionPriority + '\''
                + "regionLatitude='" + regionLatitude + '\''
                + "regionLongitude='" + regionLongitude + '\''
                + "regionMapZoom='" + regionMapZoom + '\''
                + '}';
    }
}
