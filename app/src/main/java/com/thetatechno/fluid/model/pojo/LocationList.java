package com.thetatechno.fluid.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LocationList {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("items")
    @Expose
    private ArrayList<Location> items = null;

    public ArrayList<Location> getItems() {
        return items;
    }

    public void setItems(ArrayList<Location> locations) {
        this.items = locations;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
