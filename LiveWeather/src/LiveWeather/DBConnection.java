package LiveWeather;
import java.sql.*;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

/**
 * @Name George
 * @CandidateNumber 5371
 * @date 10 Aug 2020
 * @time 11:41:25
 */

public class DBConnection extends Main
{
	private String DB_CON = "jdbc:mysql://LocalHost/LiveWeather";
	
	private String HOST;
	private int PORT;
	public String Database;
	private String USER;
	private String PASSWORD;
	public String WeatherPreferences;

    Connection connection = null;
    
	boolean bIsConnected;
	
	public void main(String[] args)
	{
		DBConnection test1 = new DBConnection();
		test1.connect();
	}
	
	// ----------------------------------------------------------------------
	// Constructor
	// ----------------------------------------------------------------------
	public DBConnection()
	{
		reset() ;
		return ;
	}

	//Setters
	private void reset()
	{
		this.bIsConnected = false ;
		return ;
	}
	
	public void mysqlSetup()
	{
		this.HOST = getConfig().getString("MySQL_host");
		this.PORT = getConfig().getInt("MySQL_port");
		this.Database = getConfig().getString("MySQL_database");
		this.USER = getConfig().getString("MySQL_username");
		this.PASSWORD = getConfig().getString("MySQL_password");
		this.WeatherPreferences = getConfig().getString("MySQL_weatherpreferences");
		
		this.DB_CON = "jdbc:mysql://" + this.HOST + ":" 
				+ this.PORT + "/" + this.Database + "?&useSSL=false&";
	}
	
	public boolean connect() 
	{
		try
		{
		//	System.out.println(this.getClass().getName() +" : Connecting la la la");
			DriverManager.getDriver(DB_CON);
			connection = DriverManager.getConnection(DB_CON, USER, PASSWORD);
			if (this.connection != null)
				return true;
			else
				return false;
		}
		catch (SQLException e)
		{
			if (e.toString().contains("Access denied"))
			{
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "LiveWeather.DBConnection - Access denied");			
				System.out.println("Access denied");
			}
			else if (e.toString().contains("Communications link failure"))
			{
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "LiveWeather.DBConnection - Communications link failure");			
				System.out.println("Communications link failure");
			}
			else
			{
				e.printStackTrace();
				System.out.println(e.toString());
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "LiveWeather.DBConnection - Other SQLException - "+e.getMessage());			
			}
			return false;
		}
		catch (Exception e)
		{
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "LiveWeather.DBConnection - Other Exception - "+e.getMessage());			
			return false;
		}
		finally
		{
			
		}
	}
	public void disconnect()
	{
		try
		{
			this.connection.close() ;
			this.bIsConnected = false ;
		//	System.err.println( this.getClass().getName() + ":: disconnected." ) ;
		}
		catch ( SQLException se )
		{
			System.err.println( this.getClass().getName() + ":: SQL error " + se ) ;
			se.printStackTrace() ;
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "LiveWeather.DBConnection - SQLException");			
		}
		catch ( Exception e )
		{
			System.err.println( this.getClass().getName() + ":: error " + e ) ;
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "LiveWeather.DBConnection - Exception");			
			e.printStackTrace() ;
		}
		finally
		{
		}
		return ;
	}


}
//End Class

//Created by Mr Singh
//modified by Bluecarpet in London