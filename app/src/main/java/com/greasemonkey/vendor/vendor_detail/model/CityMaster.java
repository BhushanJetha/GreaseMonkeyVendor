package com.greasemonkey.vendor.vendor_detail.model;

/**
 * Created by dell on 10/19/2019.
 */

public class CityMaster {

    private String stateId, cityId, cityName;

    public CityMaster(String stateId, String cityId, String cityName) {
        this.stateId = stateId;
        this.cityId = cityId;
        this.cityName = cityName;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @Override
    public String toString() {
        return cityName;
    }
}
