
/**
 * @author 14walkerg
 * @date 3 Jan 2021
 * @time 18:37:28
 */
package OpenWeatherMap;

import java.time.LocalDateTime;

public class Current
{
	private City city;
	private Temperature temp;
	private FeelsLike feels;
	private Humidity humid;
	private Pressure press;
	private Wind wind;
	private Clouds cloud;
	private int Visibility;
	public Precipitation prec;
	private Weather weather;
	private LocalDateTime lastUpdate;
	
	public Current()
	{
		reset();
	}

	private void reset()
	{
		this.city = new City();
		this.prec = new Precipitation();
		this.Visibility = -1;
		this.weather = new Weather();
	}
	
	public Weather getWeather()
	{
		return this.weather;
	}
	
	public City getCity()
	{
		return this.city;
	}
	
	public void setNumber(int iNum)
	{
		this.weather.setNumber(iNum);
	}
	
	public void setSunrise(LocalDateTime dDateTime)
	{
		this.city.setSunrise(dDateTime);
	}
	
	public void setSunset(LocalDateTime dDateTime)
	{
		this.city.setSunset(dDateTime);
	}
	
	public void setCityName(String szName)
	{
		this.city.setName(szName);
	}
	
	public void setCountry(String szCountry)
	{
		this.city.setCountry(szCountry);
	}
}

//End Class

//Created by Bluecarpet in London