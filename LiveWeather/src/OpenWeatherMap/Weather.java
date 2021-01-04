
/**
 * @author 14walkerg
 * @date 3 Jan 2021
 * @time 18:45:19
 */
package OpenWeatherMap;

public class Weather
{
	private int Number;
	private String Value;
	private String Icon;
	
	public int getNumber()
	{
		return Number;
	}
	public String getValue()
	{
		return Value;
	}
	public String getIcon()
	{
		return Icon;
	}
	
	public void setNumber(int Number)
	{
		this.Number = Number;
	}
	public void setValue(String Value)
	{
		this.Value = Value;
	}
	public void setIcon(String Icon)
	{
		this.Icon = Icon;
	}
	
	public Weather()
	{
		reset();
	}
	private void reset()
	{
		this.Icon = "";
		this.Number = 0;
		this.Value = "";
	}
}
//End Class

//Created by Bluecarpet in London