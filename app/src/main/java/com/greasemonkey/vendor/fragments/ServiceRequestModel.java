package com.greasemonkey.vendor.fragments;

/**
 * Created by dell on 12/15/2019.
 */

public class ServiceRequestModel {
    private String orderId,userId,serviceType,pickupAndDrop, amcType, orderDate,orderTime, userName, orderStatus, gmOrderId;

    public ServiceRequestModel(String orderId, String userId, String serviceType, String pickupAndDrop, String amcType, String orderDate, String orderTime, String userName, String orderStatus, String gmOrderId) {
        this.orderId = orderId;
        this.userId = userId;
        this.serviceType = serviceType;
        this.pickupAndDrop = pickupAndDrop;
        this.amcType = amcType;
        this.orderDate = orderDate;
        this.orderTime = orderTime;
        this.userName = userName;
        this.orderStatus = orderStatus;
        this.gmOrderId = gmOrderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getPickupAndDrop() {
        return pickupAndDrop;
    }

    public void setPickupAndDrop(String pickupAndDrop) {
        this.pickupAndDrop = pickupAndDrop;
    }

    public String getAmcType() {
        return amcType;
    }

    public void setAmcType(String amcType) {
        this.amcType = amcType;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getGmOrderId() {
        return gmOrderId;
    }

    public void setGmOrderId(String gmOrderId) {
        this.gmOrderId = gmOrderId;
    }
}
