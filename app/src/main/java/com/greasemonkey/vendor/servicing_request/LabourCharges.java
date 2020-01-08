package com.greasemonkey.vendor.servicing_request;

/**
 * Created by dell on 12/6/2019.
 */

public class LabourCharges {
    String labourChargesId, serviceName, bikecc1, bikecc2, bikecc3, bikecc4, bikecc5;

    public LabourCharges(String labourChargesId, String serviceName, String bikecc1, String bikecc2, String bikecc3, String bikecc4, String bikecc5) {
        this.labourChargesId = labourChargesId;
        this.serviceName = serviceName;
        this.bikecc1 = bikecc1;
        this.bikecc2 = bikecc2;
        this.bikecc3 = bikecc3;
        this.bikecc4 = bikecc4;
        this.bikecc5 = bikecc5;
    }

    public String getLabourChargesId() {
        return labourChargesId;
    }

    public void setLabourChargesId(String labourChargesId) {
        this.labourChargesId = labourChargesId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getBikecc1() {
        return bikecc1;
    }

    public void setBikecc1(String bikecc1) {
        this.bikecc1 = bikecc1;
    }

    public String getBikecc2() {
        return bikecc2;
    }

    public void setBikecc2(String bikecc2) {
        this.bikecc2 = bikecc2;
    }

    public String getBikecc3() {
        return bikecc3;
    }

    public void setBikecc3(String bikecc3) {
        this.bikecc3 = bikecc3;
    }

    public String getBikecc4() {
        return bikecc4;
    }

    public void setBikecc4(String bikecc4) {
        this.bikecc4 = bikecc4;
    }

    public String getBikecc5() {
        return bikecc5;
    }

    public void setBikecc5(String bikecc5) {
        this.bikecc5 = bikecc5;
    }

    @Override
    public String toString() {
        return serviceName;
    }
}
