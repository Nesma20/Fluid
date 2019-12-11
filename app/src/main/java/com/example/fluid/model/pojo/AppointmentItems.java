package com.example.fluid.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AppointmentItems {


        @SerializedName("items")
        @Expose
        private List<Appointement> items = null;

        public List<Appointement> getItems() {
            return items;
        }

        public void setItems(List<Appointement> items) {
            this.items = items;
        }
}
