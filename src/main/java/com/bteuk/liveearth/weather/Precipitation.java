package com.bteuk.liveearth.weather;

public class Precipitation {
    private float value;
    private String mode;

    public Precipitation() {
        reset();
    }

    //Getters
    public float getValue() {
        return this.value;
    }

    //Setters
    public void setValue(float value) {
        this.value = value;
    }

    public String getMode() {
        return this.mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    private void reset() {
        //Declare Variables
        this.mode = "";
        this.value = -1;
    }
}
