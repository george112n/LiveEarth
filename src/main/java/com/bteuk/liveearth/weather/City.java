package com.bteuk.liveearth.weather;

import java.time.LocalDateTime;

public class City {
    private int id;
    private String szName;
    private int CoordLong;
    private int CoordLat;
    private String szCountry;
    private int Timezone;
    private LocalDateTime sunrise;
    private LocalDateTime sunset;

    public City() {
        reset();
    }

    public void reset() {
        this.sunrise = LocalDateTime.parse("2021-01-01T06:00:00");
        this.sunset = LocalDateTime.parse("2021-01-01T18:00:00");
    }

    //Getters
    public LocalDateTime getSunrise() {
        return this.sunrise;
    }

    //Setters
    public void setSunrise(LocalDateTime dSunrise) {
        this.sunrise = dSunrise;
    }

    public LocalDateTime getSunset() {
        return this.sunset;
    }

    public void setSunset(LocalDateTime dSunset) {
        this.sunset = dSunset;
    }

    public String getName() {
        return this.szName;
    }

    public void setName(String szName) {
        this.szName = szName;
    }

    public String getCountry() {
        return this.szCountry;
    }

    public void setCountry(String szCountry) {
        this.szCountry = szCountry;
    }
}
