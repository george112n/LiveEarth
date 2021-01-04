
/**
 * @author 14walkerg
 * @date 3 Jan 2021
 * @time 18:45:10
 */
package OpenWeatherMap;

public class Precipitation
{
	private float value;
	private String mode;
	
	//Setters
	public void setValue(float value)
	{
		this.value = value;
	}
	public void setMode(String mode)
	{
		this.mode = mode;
	}
	
	//Getters
	public float getValue()
	{
		return this.value;
	}
	public String getMode()
	{
		return this.mode;
	}
	
	public Precipitation()
	{
		reset();
	}
	private void reset()
	{
		//Declare Variables
		this.mode = "";
		this.value = -1;
	}
}
//End Class

//Created by Bluecarpet in London