package LiveWeather;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author 14walkerg
 * @date 4 Jan 2021
 * @time 12:17:19
 */

public class WeatherPreference
{
	//Member variable
	private String UUID;
	private boolean LiveWeather;
	private boolean LiveFog;
	private boolean LiveTime;
//	private boolean Seasonal;
	
	//Contructor
	public WeatherPreference()
	{
		reset();
		return;
	}
	
	private void reset()
	{
		this.UUID = "";
		this.LiveFog = false;
		this.LiveWeather = false;
		this.LiveTime = false;
	//	this.Seasonal = false;
	}

	//Setters
	public void setUUID (String UUID)
	{
		this.UUID = UUID;
	}
	
	//Getters
	public boolean getWeather()
	{
		return this.LiveWeather;
	}
	public boolean getFog()
	{
		return this.LiveFog;
	}
	public boolean getTime()
	{
		return this.LiveTime;
	}
/*	public boolean getSeasonal()
	{
		return this.Seasonal;
	}
*/
	//Set by UUID
	public void fetchFromUUID()
	{
		boolean bSuccess = false;
		
		String sql;
		
		Statement SQL = null;
		ResultSet resultSet = null;
				
		try
		{
			//Collects all fields for the specified EID
			sql = "SELECT * FROM "+Main.getInstance().WeatherPreferences +" WHERE UUID = \""+this.UUID +"\"";
			
			SQL = Main.getInstance().connection.createStatement();
			resultSet = SQL.executeQuery(sql);
			//Moves the curser to the next line
			bSuccess = resultSet.next();
			
			//If no user is found, program will notify thing
			if (bSuccess == false)
			{
				System.out.println("Setting preferences from UUID: No user found with UUID "+this.UUID);
			}
			//Checks that there is only 1 record returned
			else if (resultSet.next() == false)
			{
				//Runs if there is no second record
				//Colects results again
				resultSet = SQL.executeQuery(sql);
				resultSet.next();
				
				//Stores results into the object
				this.LiveWeather = resultSet.getBoolean("LiveWeather");
				this.LiveFog = resultSet.getBoolean("LiveFog");
				this.LiveTime = resultSet.getBoolean("LiveTime");
			//	this.Seasonal = resultSet.getBoolean("Seasonal");
			}
			else
			{
				System.out.println("Setting preferences from UUID: More than one user found");
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	//Checks whether a record exists
	public boolean exists()
	{
		boolean bSuccess = false;
		
		String sql;
		
		Statement SQL = null;
		ResultSet resultSet = null;
				
		try
		{
			//Collects all fields for the specified EID
			sql = "SELECT * FROM "+Main.getInstance().WeatherPreferences +" WHERE UUID = \""+this.UUID +"\"";
			
			SQL = Main.getInstance().connection.createStatement();
			resultSet = SQL.executeQuery(sql);
			//Moves the curser to the next line
			bSuccess = resultSet.next();
			
			//If no user is found, program will return false
			if (bSuccess == false)
			{
				return false;
			}
			//Checks that there is only 1 record returned by moving line on again
			else if (resultSet.next() == false)
			{
				return true;
			}
			else
			{
				//Avoids creation of yet another preference
				System.out.println("Output to console here");
				return true;
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return true;
		}
	}
	
	//--------------------------
	//----Toggle preferences----
	//--------------------------
	
	public boolean toogleWeather()
	{
		//Puts current value through a NOT operation
		boolean newWeatherPref = !this.LiveWeather;
		
		int iCount = -1;
		String sql;
		
		Statement SQL = null; 
		
		try
		{
			if (newWeatherPref)
				sql = "UPDATE "+Main.getInstance().WeatherPreferences +" SET LiveWeather = 1 Where UUID = \""+this.UUID+"\";";
			else
			{
				sql = "UPDATE "+Main.getInstance().WeatherPreferences +" SET LiveWeather = 0 Where UUID = \""+this.UUID+"\";";
				//Turns fog off
				manualFog(0);
			}
			SQL = Main.getInstance().connection.createStatement();
			
			//Executes the update and returns how many rows were changed
			iCount = SQL.executeUpdate(sql);
		}
		catch (SQLException se)
		{
			se.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return newWeatherPref;
	}
	
	public void manualFog(int i)
	{
		int iCount = -1;
		String sql;
		
		Statement SQL = null; 
		
		try
		{
			sql = "UPDATE "+Main.getInstance().WeatherPreferences +" SET LiveFog = "+i +" Where UUID = \""+this.UUID+"\";";
			
			SQL = Main.getInstance().connection.createStatement();
			
			//Executes the update and returns how many rows were changed
			iCount = SQL.executeUpdate(sql);
		}
		catch (SQLException se)
		{
			se.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public boolean toogleFog()
	{
		//Puts current value through a NOT operation
		boolean newFogPref = !this.LiveFog;
		
		int iCount = -1;
		String sql;
		
		Statement SQL = null; 
		
		try
		{
			if (newFogPref)
				sql = "UPDATE "+Main.getInstance().WeatherPreferences +" SET LiveFog = 1 Where UUID = \""+this.UUID+"\";";
			else
			{
				sql = "UPDATE "+Main.getInstance().WeatherPreferences +" SET LiveFog = 0 Where UUID = \""+this.UUID+"\";";
			}
			SQL = Main.getInstance().connection.createStatement();
			
			//Executes the update and returns how many rows were changed
			iCount = SQL.executeUpdate(sql);
		}
		catch (SQLException se)
		{
			se.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return newFogPref;
	}
		
	public boolean toogleTime()
	{
		//Puts current value through a NOT operation
		boolean newTimePref = !this.LiveTime;
		
		int iCount = -1;
		String sql;
		
		Statement SQL = null; 
		
		try
		{
			if (newTimePref)
				sql = "UPDATE "+Main.getInstance().WeatherPreferences +" SET LiveTime = 1 Where UUID = \""+this.UUID+"\";";
			else
			{
				sql = "UPDATE "+Main.getInstance().WeatherPreferences +" SET LiveTime = 0 Where UUID = \""+this.UUID+"\";";
			}
			SQL = Main.getInstance().connection.createStatement();
			
			//Executes the update and returns how many rows were changed
			iCount = SQL.executeUpdate(sql);
		}
		catch (SQLException se)
		{
			se.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return newTimePref;
	}
	
/*	public boolean toogleSeasonal()
	{
		//Puts current value through a NOT operation
		boolean newSeasonalPref = !this.Seasonal;
		
		int iCount = -1;
		String sql;
		
		Statement SQL = null; 
		
		try
		{
			if (newSeasonalPref)
				sql = "UPDATE "+Main.getInstance().WeatherPreferences +" SET Seasonal = 1 Where UUID = \""+this.UUID+"\";";
			else
			{
				sql = "UPDATE "+Main.getInstance().WeatherPreferences +" SET Seasonal = 0 Where UUID = \""+this.UUID+"\";";
			}
			SQL = Main.getInstance().connection.createStatement();
			
			//Executes the update and returns how many rows were changed
			iCount = SQL.executeUpdate(sql);
		}
		catch (SQLException se)
		{
			se.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return newSeasonalPref;
	}
	*/
	
	//--------------------------------------
	//----Creating new preference record----
	//--------------------------------------
	//SQL Changes
	public boolean createNewPref()
	{		
		boolean bSuccess = false;
		int iCount = -1;
		String sql;
		
		Statement SQL = null;
		
		try
		{
			//Compiles the command to add the new user
			sql = "INSERT INTO "+Main.getInstance().WeatherPreferences +" (UUID)"
					+ "VALUES(\""
					+ this.UUID +"\");";
			SQL = Main.getInstance().connection.createStatement();
			
			//Executes the update and returns the amount of records updated
			iCount = SQL.executeUpdate(sql);
			
			//Checks whether only 1 record was updated
			if (iCount == 1)
			{
				//If so, bSuccess is set to true
				bSuccess = true;
			}
		}
		catch(SQLException se)
		{
			se.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return bSuccess;
	}
} //End Class

//Created by Bluecarpet in London