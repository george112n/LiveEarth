package LiveWeather;
import org.bukkit.entity.Player;

import OpenWeatherMap.Current;
import Projections.ModifiedAirocean;

import org.bukkit.Location;
import org.bukkit.WeatherType;

/**
 * @author 14walkerg
 * @date 3 Jan 2021
 * @time 12:11:00
 */

//resetPlayerWeather returns it back to server

public class LiveWeatherUtil extends Main
{
	String apiKey;
	static String szURL;
	Player p;
	Location pLocation;
	static double[] coords;
	
	public LiveWeatherUtil(Player player)
	{
		reset();
		this.p = player;
		this.pLocation = player.getLocation();
		this.apiKey = config.getString("apiKey");
	}
	
	private void reset()
	{
		szURL = "";
		pLocation = null;
		coords = new double[2];
	}

	public void main(String[] args)
	{
		int iWeatherCode;
		
		Current current;
		
		double x;
		double z;

		x = 2805306;
		z = -5387192;
		coords = getCoords(x, z);
		
		//Compiles the request URL
		compileSourceURL(coords);
		
		System.out.println(szURL);
		current = GetWeather.entry(szURL);
		
		iWeatherCode = current.weather.getNumber();
		
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
	}
	
	public void call(boolean weather, boolean fog)
	{
		int iWeatherCode;
		
		Current current;
		
		double x;
		double z;
		
		x = pLocation.getX();
		z = pLocation.getZ();
		coords = getCoords(x, z);
		
		//Compiles the request URL
		compileSourceURL(coords);
		
		System.out.println(szURL);
		current = GetWeather.entry(szURL);
		
		iWeatherCode = current.weather.getNumber();

		if (weather)
		{
			
			//https://openweathermap.org/weather-conditions#Weather-Condition-Codes-2
			if (iWeatherCode >= 800)
			{
				p.setPlayerWeather(WeatherType.CLEAR);
				System.out.println("Clear");
			}
			else if (iWeatherCode >= 701 && iWeatherCode <= 781)
			{
				p.setPlayerWeather(WeatherType.CLEAR);
				System.out.println("Clear");
			}
			else if (iWeatherCode >= 200 && iWeatherCode <= 622)
			{
				p.setPlayerWeather(WeatherType.DOWNFALL);
				System.out.println("Rain");
			}
			if (iWeatherCode >= 200 && iWeatherCode <= 232)
			{
				System.out.println("Thunder");
			}
		}
		
		if (fog)
		{			
			//https://openweathermap.org/weather-conditions#Weather-Condition-Codes-2
			if (iWeatherCode >= 701 && iWeatherCode <= 781)
			{
				//FOG COMMAND - NOT IMPLEMENTED
				System.out.println("Clear");
			}			
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
	
	public void compileSourceURL(double[] coords)
	{
		szURL = "http://api.openweathermap.org/data/2.5/weather?lat=";
		szURL = szURL + coords[1];
		szURL = szURL + "&lon=";
		szURL = szURL + coords[0];
		szURL = szURL + "&appid=" + apiKey;
		szURL = szURL + "&mode=xml";
	}
	
	public int MetofficeWeatherType(int iLocation)
	{
		return 0;
	}
		
	public void changeWeatherMetoffice(Player p, int iInputWeatherType)
	{
		switch (iInputWeatherType)
		{
		case 0:
			//Clear night
			p.setPlayerWeather(WeatherType.CLEAR);
			break;
		case 1:
			//Sunny day
			break;
		case 2:
			//Partly cloudy (night)
			break;
		case 3:
			//Partly cloudy (day)
			break;
		case 4:
			//Not used
			break;
		case 5:
			//Mist
			break;
		case 6:
			//Fog
			break;
		case 7:
			//Cloudy
			break;
		case 8:
			//Overcast
			break;
		case 9:
			//Light rain shower (night)
			break;
		case 10:
			//Light rain shower (day)
			break;
		case 11:
			//Drizzle
			break;
		case 12:
			//Light rain
			break;
		case 13:
			//Heavy rain shower (night)
			break;
		case 14:
			//Heavy rain shower (day)
			break;
		case 15:
			//Heavy rain
			break;
		case 16:
			//Sleet shower (night)
			break;
		case 17:
			//Sleet shower (day)
			break;
		case 18:
			//Sleet
			break;
		case 19:
			//Hail shower (night)
			break;
		case 20:
			//Hail shower (day)
			break;
		case 21:
			//Hail
			break;
		case 22:
			//Light snow shower (night)
			break;
		case 23:
			//Light snow shower (day)
			break;
		case 24:
			//Light snow
			break;
		case 25:
			//Heavy snow shower (night)
			break;
		case 26:
			//Heavy snow shower (day)
			break;
		case 27:
			//Heavy snow
		case 28:
			//Thunder shower (night)
			break;
		case 29:
			//Thunder shower (day)
			break;
		case 30:
			//Thunder
			break;
		default:
			break;
		}
		p.setPlayerWeather(null);
	}
}
//End Class

//Created by Bluecarpet in London