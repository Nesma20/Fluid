package com.example.fluid.model.pojo;

import android.content.ClipData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LocationList {
    @SerializedName("items")
    @Expose
    private List<Location> items = null;

    public List<Location> getItems() {
        return items;
    }

    public void setItems(List<Location> locations) {
        this.items = locations;
    }
}
