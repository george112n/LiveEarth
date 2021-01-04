package Metoffice;

/**
 * @author 14walkerg
 * @date 3 Jan 2021
 * @time 15:29:45
 * continent: This describes the continent in which the location is located
 * country: This describes the country in which the location is situated
 * name: This provides the name of the location
 * lon: This provides the longitude of the location
 * lat: This provides the latitude of the location
 * i: This provides the ID of the location
 * elevation: This provides the elevation of the location
 */

public class WeatherLocation
{
	private String Continent;
	private String Country;
	private String Name;
	private float Lon;
	private float Lat;
	private int i;
	private int Elevation;
	
	private Period[] periods;
	
	public String getContinent()
	{
		return Continent;
	}
	public String getCountry()
	{
		return Country;
	}
	public String getName()
	{
		return Name;
	}
	public float getLon()
	{
		return Lon;
	}
	public float getLat()
	{
		return Lat;
	}
	public int getID()
	{
		return i;
	}
	public float getElevation()
	{
		return Elevation;
	}

}

//End Class

//Created by Bluecarpet in London