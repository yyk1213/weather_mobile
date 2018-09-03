package com.example.yeon1213.myapplication.Main.WeatherData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pm10 {

    @SerializedName("grade")
    @Expose
    private String grade;
    @SerializedName("value")
    @Expose
    private String value;

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
