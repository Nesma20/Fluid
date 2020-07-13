package com.thetatechno.fluid.ui.listeners;

public class OnUpdateDataEvent {
    private String action;
    private String facilityCode;
    private String sessionId;

    public OnUpdateDataEvent() {
    }

    public OnUpdateDataEvent(String action, String facilityName) {
        this.action = action;
        this.facilityCode = facilityName;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getFacilityCode() {
        return facilityCode;
    }

    public void setFacilityCode(String facilityCode) {
        this.facilityCode = facilityCode;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
