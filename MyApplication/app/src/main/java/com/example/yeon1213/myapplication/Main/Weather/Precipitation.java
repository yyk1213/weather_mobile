package com.example.yeon1213.myapplication.Main.Weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Precipitation {
    @SerializedName("sinceOntime")
    @Expose
    private String sinceOntime;
    @SerializedName("type")
    @Expose
    private String type;

    public String getSinceOntime() {
        return sinceOntime;
    }

    public void setSinceOntime(String sinceOntime) {
        this.sinceOntime = sinceOntime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
