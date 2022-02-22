package com.bteuk.liveearth.weather;

import com.bteuk.liveearth.LiveEarth;
import net.buildtheearth.terraminusminus.generator.EarthGeneratorSettings;
import net.buildtheearth.terraminusminus.projection.GeographicProjection;
import net.buildtheearth.terraminusminus.projection.OutOfProjectionBoundsException;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.WeatherType;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;

public class WeatherUtils {

    static String szURL;
    static double[] coords;
    public String szLocation;
    public long lTime;
    public String szWeather;
    LiveEarth plugin;
    String apiKey;
    Player p;
    Location pLocation;

    public WeatherUtils(LiveEarth plugin) {
        this.plugin = plugin;
    }

    public WeatherUtils(Player player, LiveEarth plugin) {
        reset();
        this.plugin = plugin;
        this.p = player;
        this.pLocation = player.getLocation();
        this.apiKey = plugin.getConfig().getString("apiKey");
        this.lTime = 0L;
    }

    public WeatherUtils(Player player, LiveEarth plugin, Location loc) {
        reset();
        this.plugin = plugin;
        this.p = player;
        this.pLocation = loc;
        this.apiKey = plugin.getConfig().getString("apiKey");
        this.lTime = 0L;
    }

    public static double[] getCoords(double x, double z) throws OutOfProjectionBoundsException {
        EarthGeneratorSettings bteSettings = EarthGeneratorSettings.parse(EarthGeneratorSettings.BTE_DEFAULT_SETTINGS);
        GeographicProjection projection = bteSettings.projection();
        return projection.toGeo(x, z);

    }

    public static void compileSourceURL(double[] coords, LiveEarth plugin) {
        szURL = "http://api.openweathermap.org/data/2.5/weather?lat=" + coords[1] + "&lon=" + coords[0] + "&appid=" + plugin.getConfig().getString("apiKey") + "&mode=xml";
    }

    private void reset() {
        szURL = "";
        pLocation = null;
        coords = new double[2];
        szLocation = "";
        lTime = -1;
        szWeather = "";
    }

    public void call(boolean weather, boolean seasonalTime, boolean bLocation) throws OutOfProjectionBoundsException {
        int iWeatherCode;
        LocalDateTime dSunrise;
        LocalDateTime dSunset;
        long currentTime;

        Current current;

        int x = pLocation.getBlockX();
        int z = pLocation.getBlockZ();
        coords = getCoords(x, z);

        if (Double.isNaN(coords[0])) {
            return;
        }

        //Compiles the request URL
        compileSourceURL(coords, plugin);

        //Gets the weather
        current = WeatherGetter.entry(szURL);

        iWeatherCode = current.getWeather().getNumber();

        if (weather) {
            //https://openweathermap.org/weather-conditions#Weather-Condition-Codes-2
            if (iWeatherCode >= 800) {
                p.setPlayerWeather(WeatherType.CLEAR);
                szWeather = WeatherType.CLEAR.toString();
            } else if (iWeatherCode >= 701 && iWeatherCode <= 781) {
                p.setPlayerWeather(WeatherType.CLEAR);
                szWeather = WeatherType.CLEAR.toString();
            } else if (iWeatherCode >= 200 && iWeatherCode <= 622) {
                p.setPlayerWeather(WeatherType.DOWNFALL);
                szWeather = WeatherType.DOWNFALL.toString();
            }
            if (iWeatherCode >= 200 && iWeatherCode <= 232) {
                p.setPlayerWeather(WeatherType.DOWNFALL);
                p.playSound(p.getLocation(), Sound.ENTITY_LIGHTNING_IMPACT, 100, 5); //just a nice lil scare
                szWeather = WeatherType.DOWNFALL.toString();
            }
        }

        if (seasonalTime) {
            dSunrise = current.getCity().getSunrise();
            dSunset = current.getCity().getSunset();
            currentTime = plugin.updateTimeSeasonal(p, dSunrise, dSunset);
            lTime = currentTime;
        }
        if (bLocation) {
            szLocation = current.getCity().getName();
        }

    }
}
