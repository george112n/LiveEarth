package com.bteuk.liveearth.weather;

import java.time.LocalDateTime;

public class Current {
    public Precipitation prec;
    private City city;
    private Temperature temp;
    private int Visibility;
    private Weather weather;
    private LocalDateTime lastUpdate;

    public Current() {
        reset();
    }

    private void reset() {
        this.city = new City();
        this.prec = new Precipitation();
        this.Visibility = -1;
        this.weather = new Weather();
    }

    public Weather getWeather() {
        return this.weather;
    }

    public City getCity() {
        return this.city;
    }

    public void setNumber(int iNum) {
        this.weather.setNumber(iNum);
    }

    public void setSunrise(LocalDateTime dDateTime) {
        this.city.setSunrise(dDateTime);
    }

    public void setSunset(LocalDateTime dDateTime) {
        this.city.setSunset(dDateTime);
    }

    public void setCityName(String szName) {
        this.city.setName(szName);
    }

    public void setCountry(String szCountry) {
        this.city.setCountry(szCountry);
    }
}
