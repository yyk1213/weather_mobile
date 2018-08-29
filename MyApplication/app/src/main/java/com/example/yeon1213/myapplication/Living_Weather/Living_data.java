package com.example.yeon1213.myapplication.Living_Weather;

public class Living_data {
    private String living_name;
    private String living_explanation;

    public Living_data(){}

    public Living_data(String living_name, String living_explanation) {
        this.living_name = living_name;
        this.living_explanation=living_explanation;
    }

    public String getLiving_name() {
        return living_name;
    }

    public String getLiving_explanation() {
        return living_explanation;
    }

    public void setLiving_name(String living_name) {
        this.living_name = living_name;
    }

    public void setLiving_explanation(String living_explanation) {
        this.living_explanation = living_explanation;
    }
}
