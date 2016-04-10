package com.devacademy.tarbandrei.locationproject.pojos;

import com.google.gson.annotations.SerializedName;

public class Snow {
    @SerializedName("3h")
    private Double last3Hours;

    public Double getLast3Hours() {
        return last3Hours;
    }

    public void setLast3Hours(Double last3Hours) {
        this.last3Hours = last3Hours;
    }
}
