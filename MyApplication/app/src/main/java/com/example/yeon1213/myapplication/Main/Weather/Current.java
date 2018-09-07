package com.example.yeon1213.myapplication.Main.Weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Current {
    @SerializedName("timeRelease")
    @Expose
    private String timeRelease;
    @SerializedName("index")
    @Expose
    private String index;

    public String getTimeRelease() {
        return timeRelease;
    }

    public void setTimeRelease(String timeRelease) {
        this.timeRelease = timeRelease;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }
}
