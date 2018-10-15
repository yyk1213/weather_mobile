package com.example.yeon1213.myapplication.Main.Weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UvIndex {
    @SerializedName("grid")
    @Expose
    private Grid grid;
    @SerializedName("day00")
    @Expose
    private Day00 day00;
    @SerializedName("day01")
    @Expose
    private Day01 day01;
    @SerializedName("day02")
    @Expose
    private Day02 day02;

    public Grid getGrid() {
        return grid;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    public Day00 getDay00() {
        return day00;
    }

    public void setDay00(Day00 day00) {
        this.day00 = day00;
    }

    public Day01 getDay01() {
        return day01;
    }

    public void setDay01(Day01 day01) {
        this.day01 = day01;
    }

    public Day02 getDay02() {
        return day02;
    }

    public void setDay02(Day02 day02) {
        this.day02 = day02;
    }

}
