package com.greasemonkey.vendor.notification;

/**
 * Created by dell on 10/5/2019.
 */

public class NotificationModel {
    private String orderId,userId,serviceType, orderDate, estimateAmount, orderStatus;

    public NotificationModel(String orderId, String userId, String serviceType, String orderDate, String estimateAmount, String orderStatus) {
        this.orderId = orderId;
        this.userId = userId;
        this.serviceType = serviceType;
        this.orderDate = orderDate;
        this.estimateAmount = estimateAmount;
        this.orderStatus = orderStatus;
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

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getEstimateAmount() {
        return estimateAmount;
    }

    public void setEstimateAmount(String estimateAmount) {
        this.estimateAmount = estimateAmount;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
