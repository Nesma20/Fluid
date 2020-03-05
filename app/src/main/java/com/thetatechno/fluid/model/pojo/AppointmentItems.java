package com.thetatechno.fluid.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AppointmentItems {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("items")
    @Expose
    private List<Appointement> items = null;

    public List<Appointement> getItems() {
        return items;
    }

    public void setItems(List<Appointement> items) {
        this.items = items;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
