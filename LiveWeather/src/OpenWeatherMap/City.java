
/**
 * @author 14walkerg
 * @date 3 Jan 2021
 * @time 18:42:06
 */
package OpenWeatherMap;

import java.time.LocalDateTime;

public class City
{
	private int id;
	private String szName;
	private int CoordLong;
	private int CoordLat;
	private String szCountry;
	private int Timezone;
	private LocalDateTime sunrise;
	private LocalDateTime sunset;
	
	public City()
	{
		reset();
	}
	public void reset()
	{
		this.sunrise = LocalDateTime.parse("2021-01-01T06:00:00");
		this.sunset = LocalDateTime.parse("2021-01-01T18:00:00");
	}
	
	//Getters
	public LocalDateTime getSunrise()
	{
		return this.sunrise;
	}
	
	public LocalDateTime getSunset()
	{
		return this.sunset;
	}
	
	public String getName()
	{
		return this.szName;
	}
	
	public String getCountry()
	{
		return this.szCountry;
	}
	
	//Setters
	public void setSunrise(LocalDateTime dSunrise)
	{
		this.sunrise = dSunrise;
	}
	
	public void setSunset(LocalDateTime dSunset)
	{
		this.sunset = dSunset;
	}
	
	public void setName(String szName)
	{
		this.szName = szName;
	}
	
	public void setCountry(String szCountry)
	{
		this.szCountry = szCountry;
	}
} //End Class

//Created by Bluecarpet in London