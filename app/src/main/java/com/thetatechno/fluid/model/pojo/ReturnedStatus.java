package com.thetatechno.fluid.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReturnedStatus {
    @SerializedName("status")
    @Expose
    private Integer status;

    public Integer getReturnStatus() {
        return status;
    }

    public void setReturnStatus(Integer returnStatus) {
        this.status = returnStatus;
    }
}
