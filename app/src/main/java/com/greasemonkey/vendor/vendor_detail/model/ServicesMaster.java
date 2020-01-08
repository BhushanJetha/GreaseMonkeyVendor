package com.greasemonkey.vendor.vendor_detail.model;

/**
 * Created by dell on 10/19/2019.
 */

public class ServicesMaster {

    private String serviceId, serviceTypeId, serviceName, servicePrice;

    public ServicesMaster(String serviceId, String serviceTypeId, String serviceName, String servicePrice) {
        this.serviceId = serviceId;
        this.serviceTypeId = serviceTypeId;
        this.serviceName = serviceName;
        this.servicePrice = servicePrice;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(String serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(String servicePrice) {
        this.servicePrice = servicePrice;
    }

    @Override
    public String toString() {
        return serviceTypeId;
    }
}
