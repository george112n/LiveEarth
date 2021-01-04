
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
	public Weather weather;
	private LocalDateTime lastUpdate;
	
	public Current()
	{
		reset();
	}

	private void reset()
	{
		this.prec = new Precipitation();
		this.Visibility = -1;
		this.weather = new Weather();
	}
}

//End Class

//Created by Bluecarpet in London