package com.greasemonkey.vendor.vendor_detail.model;

/**
 * Created by dell on 10/19/2019.
 */

public class StateMaster {

    private String stateId,StateName;

    public StateMaster(String stateId, String stateName) {
        this.stateId = stateId;
        StateName = stateName;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getStateName() {
        return StateName;
    }

    public void setStateName(String stateName) {
        StateName = stateName;
    }

    @Override
    public String toString() {
        return StateName;
    }
}
