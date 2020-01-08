package com.greasemonkey.vendor.vendor_detail.model;

/**
 * Created by dell on 10/19/2019.
 */

public class ManufactureModel {
    private String manufactureId, manufactureName;

    public ManufactureModel(String manufactureId, String manufactureName) {
        this.manufactureId = manufactureId;
        this.manufactureName = manufactureName;
    }

    public String getManufactureId() {
        return manufactureId;
    }

    public void setManufactureId(String manufactureId) {
        this.manufactureId = manufactureId;
    }

    public String getManufactureName() {
        return manufactureName;
    }

    public void setManufactureName(String manufactureName) {
        this.manufactureName = manufactureName;
    }

    @Override
    public String toString() {
        return manufactureName;
    }
}
