package com.example.yeon1213.myapplication.Main.Weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CarWash {
    @SerializedName("locationName")
    @Expose
    private String locationName;
    @SerializedName("index")
    @Expose
    private String index;
    @SerializedName("comment")
    @Expose
    private String comment;

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
