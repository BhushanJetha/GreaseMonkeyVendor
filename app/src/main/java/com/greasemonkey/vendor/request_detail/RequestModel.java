package com.greasemonkey.vendor.request_detail;

/**
 * Created by dell on 9/29/2019.
 */

public class RequestModel {
    private String requestNumber, requestType, estimateDateTime, pickupStatus, paymentStatus;

    public RequestModel(String requestNumber, String requestType, String estimateDateTime, String pickupStatus, String paymentStatus) {
        this.requestNumber = requestNumber;
        this.requestType = requestType;
        this.estimateDateTime = estimateDateTime;
        this.pickupStatus = pickupStatus;
        this.paymentStatus = paymentStatus;
    }

    public String getRequestNumber() {
        return requestNumber;
    }

    public void setRequestNumber(String requestNumber) {
        this.requestNumber = requestNumber;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getEstimateDateTime() {
        return estimateDateTime;
    }

    public void setEstimateDateTime(String estimateDateTime) {
        this.estimateDateTime = estimateDateTime;
    }

    public String getPickupStatus() {
        return pickupStatus;
    }

    public void setPickupStatus(String pickupStatus) {
        this.pickupStatus = pickupStatus;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
