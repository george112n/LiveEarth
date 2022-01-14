package LiveWeather;
import org.bukkit.entity.Player;

import OpenWeatherMap.Current;
import Projections.ModifiedAirocean;

import java.time.LocalDateTime;

import org.bukkit.Location;
import org.bukkit.WeatherType;

/**
 * @author 14walkerg
 * @date 3 Jan 2021
 * @time 12:11:00
 */

//resetPlayerWeather returns it back to server

public class LiveWeatherUtil
{
	String apiKey;
	static String szURL;
	Player p;
	Location pLocation;
	static double[] coords;
	public String szLocation;
	public long lTime;
	public String szWeather;
	
	public LiveWeatherUtil()
	{
		
	}
	public LiveWeatherUtil(Player player)
	{
		reset();
		this.p = player;
		this.pLocation = player.getLocation();
		this.apiKey = Main.getInstance().getConfig().getString("apiKey");
		this.lTime = 0L;
	}
	
	public LiveWeatherUtil(Player player, Location location)
	{
		reset();
		this.p = player;
		this.pLocation = location;
		this.apiKey = Main.getInstance().getConfig().getString("apiKey");
		this.lTime = 0L;
	}
	
	private void reset()
	{
		szURL = "";
		pLocation = null;
		coords = new double[2];
		szLocation = "";
		lTime = -1;
		szWeather = "";
	}

	public static void main(String[] args)
	{
		int iWeatherCode;
		
		Current current;
		
		double x;
		double z;

		x = 2709234;
		z = -5552662;
		coords = getCoords(x, z);

		if (coords[0] == Double.NaN)
		{
			return;
		}

		//Compiles the request URL
		compileSourceURL(coords);
		
		System.out.println(szURL);
		current = GetWeather.entry(szURL);
		
		iWeatherCode = current.getWeather().getNumber();
		
		//https://openweathermap.org/weather-conditions#Weather-Condition-Codes-2
		if (iWeatherCode >= 800)
		{
		//	p.setPlayerWeather(WeatherType.CLEAR);
			System.out.println("Clear");
		}
		else if (iWeatherCode >= 701 && iWeatherCode <= 781)
		{
		//	p.setPlayerWeather(WeatherType.CLEAR);
			System.out.println("Clear");
		}
		else if (iWeatherCode >= 200 && iWeatherCode <= 622)
		{
		//	p.setPlayerWeather(WeatherType.DOWNFALL);
			System.out.println("Rain");
		}
		if (iWeatherCode >= 200 && iWeatherCode <= 232)
		{
			System.out.println("Thunder");
		}
		System.out.println(current.getCity().getName());
		System.out.println(current.getCity().getCountry());
	}
	
	public void call(boolean weather, boolean seasonalTime, boolean bLocation)
	{
		int iWeatherCode;
		LocalDateTime dSunrise;
		LocalDateTime dSunset;
		long currentTime;
		
		Current current;
		
		double x;
		double z;
		
		x = pLocation.getX();
		z = pLocation.getZ();
		coords = getCoords(x, z);
		if (coords[0] == Double.NaN)
		{
			return;
		}
		
		//Compiles the request URL
		compileSourceURL(coords);
		
		//Gets the weather
		current = GetWeather.entry(szURL);
		
		iWeatherCode = current.getWeather().getNumber();
		
		if (weather)
		{
			//https://openweathermap.org/weather-conditions#Weather-Condition-Codes-2
			if (iWeatherCode >= 800)
			{
				p.setPlayerWeather(WeatherType.CLEAR);
				szWeather = WeatherType.CLEAR.toString();
			//	System.out.println("Clear");
			}
			else if (iWeatherCode >= 701 && iWeatherCode <= 781)
			{
				p.setPlayerWeather(WeatherType.CLEAR);
				szWeather = WeatherType.CLEAR.toString();
			//	System.out.println("Clear");
			}
			else if (iWeatherCode >= 200 && iWeatherCode <= 622)
			{
				p.setPlayerWeather(WeatherType.DOWNFALL);
				szWeather = WeatherType.DOWNFALL.toString();
			//	System.out.println("Rain");
			}
			if (iWeatherCode >= 200 && iWeatherCode <= 232)
			{
			//	System.out.println("Thunder");
			}
		}
		
		if (seasonalTime)
		{
			dSunrise = current.getCity().getSunrise();
			dSunset = current.getCity().getSunset();
			
			currentTime = Main.updateTimeSeasonal(p, dSunrise, dSunset);
			lTime = currentTime;
		}
		if (bLocation)
		{
			szLocation = current.getCity().getName();
		}
		
		//	iInputWeatherType = MetofficeWeatherType(iLocation);
		//	changeWeatherMetoffice(p, iInputWeatherType);
	}
	
	public static double[] getCoords(double x, double z)
	{
		ModifiedAirocean projection = new ModifiedAirocean();
		double[] latlong = projection.toGeo(x, z);
		return latlong;
	}
	
	public static void compileSourceURL(double[] coords)
	{
		szURL = "http://api.openweathermap.org/data/2.5/weather?lat=";
		szURL = szURL + coords[1];
		szURL = szURL + "&lon=";
		szURL = szURL + coords[0];
		szURL = szURL + "&appid=" + "ac594611afb90c97e2382439671e9112";//apiKey;
		szURL = szURL + "&mode=xml";
	}
}
//End Class

//Created by Bluecarpet in London