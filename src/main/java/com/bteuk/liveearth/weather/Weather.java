package com.bteuk.liveearth.weather;

public class Weather {
    private int Number;
    private String Value;
    private String Icon;

    public Weather() {
        reset();
    }

    public int getNumber() {
        return Number;
    }

    public void setNumber(int Number) {
        this.Number = Number;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String Value) {
        this.Value = Value;
    }

    public String getIcon() {
        return Icon;
    }

    public void setIcon(String Icon) {
        this.Icon = Icon;
    }

    private void reset() {
        this.Icon = "";
        this.Number = 0;
        this.Value = "";
    }
}
