package LiveWeather;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import LiveWeather.Commands.LiveTime;
import LiveWeather.Commands.LiveWeather;
import LiveWeather.listeners.JoinEvent;


/**
 * @author 14walkerg
 * @date 3 Jan 2021
 * @time 23:34:41
 */

public class Main extends JavaPlugin
{
	public void main(String[] args)
	{
		onEnable();
	}
	
	//MySQL
	protected DBConnection dbConnection = new DBConnection();	

	String sql;
	
	Statement SQL = null; 
	ResultSet resultSet = null;

	static Main instance;
	static FileConfiguration config;
	
	public FileConfiguration getConfig()
	{
		return config;
	}

	@Override
	public void onEnable()
	{
		//Config Setup
		Main.instance = this;
		Main.config = this.getConfig();

		boolean bSuccess;
		dbConnection.mysqlSetup();
		bSuccess = dbConnection.connect();

		if (bSuccess)
		{
			Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "MYSQL CONNECTED");
		}

		//Creates the mysql table if not already exists
		createWeatherPrefsTable();
		
		//Listeners
		new JoinEvent(this);
		
		//Commands
		getCommand("liveweather").setExecutor(new LiveWeather());
		getCommand("livetime").setExecutor(new LiveTime());
	}
	
	@Override
	public void onDisable()
	{
		dbConnection.disconnect();
	}
	
	public static long updateTime(Player player)
	{
		long lCurrentTime;
		lCurrentTime = LocalTime.now().getHour()*100 + LocalTime.now().getMinute();
		player.setPlayerTime(lCurrentTime, false);
		return lCurrentTime;
	}
	
	public boolean createWeatherPrefsTable()
	{
		boolean bSuccess = false;
		int iCount = -1;

		try
		{
			//Adds a weather pref table
			sql = "CREATE TABLE IF NOT EXISTS `"+dbConnection.Database+"`.`"+dbConnection.WeatherPreferences+"` (\n" + 
					"  `UUID` VARCHAR(36) NOT NULL,\n" + 
					"  `LiveWeather` TINYINT NOT NULL DEFAULT 0,\n" + 
					"  `LiveFog` TINYINT NOT NULL DEFAULT 0,\n" + 
					"  `LiveTime` TINYINT NOT NULL DEFAULT 0,\n" + 
					"  UNIQUE INDEX `UUID_UNIQUE` (`UUID` ASC));\n" + 
					"";
			SQL = dbConnection.connection.createStatement();

			//Executes the update and returns how many rows were changed
			iCount = SQL.executeUpdate(sql);

			//If only 1 record was changed, success is set to true
			if (iCount == 1)
			{
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
		return (bSuccess);
	}
}
//End Class

//Created by Bluecarpet in London