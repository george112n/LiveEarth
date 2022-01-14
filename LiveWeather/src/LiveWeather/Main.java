package LiveWeather;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import LiveWeather.Commands.Live;
import LiveWeather.Commands.LiveTime;
import LiveWeather.Commands.LiveWeather;
import LiveWeather.listeners.BigMoveEvent;
import LiveWeather.listeners.JoinEvent;
import LiveWeather.listeners.pTimeEvent;


/**
 * @author 14walkerg
 * @date 3 Jan 2021
 * @time 23:34:41
 */

public class Main extends JavaPlugin
{	
	String sql;

	Statement SQL = null; 
	ResultSet resultSet = null;

	static Main instance;
	static FileConfiguration config;

	private String HOST;
	private int PORT;
	public String Database;
	private String USER;
	private String PASSWORD;
	public String WeatherPreferences;

	private Connection connection = null;

	boolean bIsConnected;

	private String DB_CON = "jdbc:mysql://LocalHost/LiveWeather";

	private ArrayList<UUID> playersWithLive;
	
	@Override
	public void onEnable()
	{
		//Config Setup
		Main.instance = this;
		Main.config = this.getConfig();
		saveDefaultConfig();

		//MySQL
		boolean bSuccess;
		mysqlSetup();
		bSuccess = connect();

		if (bSuccess)
		{
			Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[Liveweather] MySQL CONNECTED");
		}
	
		//Creates List
		playersWithLive = new ArrayList<UUID>();

		//Creates the mysql table if not already exists
		createWeatherPrefsTable();

		//Listeners
		new JoinEvent(this);
		new pTimeEvent(this);
		new BigMoveEvent(this);

		//Commands
		getCommand("liveweather").setExecutor(new LiveWeather());
		getCommand("livetime").setExecutor(new LiveTime());
		getCommand("live").setExecutor(new Live());

		int minute = (int) 1200L;

		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
		{
			public void run()
			{
				UpdateCall up = new UpdateCall(instance);
				up.run();
			}
		}, 0L, minute * config.getInt("timerInterval"));
	}

	@Override
	public void onDisable()
	{
		disconnect();
	}

	/*	public static long updateTime(Player player)
	{
		long lCurrentTime;
		long militaryTime;
		militaryTime = LocalTime.now().getHour()*100 + LocalTime.now().getMinute();
		lCurrentTime = (LocalTime.now().getHour()*1000-6000) + (LocalTime.now().getMinute()*10-60);
		player.setPlayerTime(lCurrentTime, false);
		return militaryTime;
	}
	 */
	public static long updateTimeSeasonal(Player player, LocalDateTime sunrise, LocalDateTime sunset)
	{
		long lSeasonalTime;
		long lSeasonalNightTime;
		long militaryTime;
		final float mcSunLight = 14076F;
		final float mcSunRise = 22967F;
		float fractionOfDaylightComplete;
		float fractionOfNightlightComplete;
		float fSunset = sunset.getHour()*60 + sunset.getMinute();
		float fSunrise = sunrise.getHour()*60 + sunrise.getMinute();

		//Daylight in minutes
		float fDaylight = 60*(sunset.getHour() - sunrise.getHour()) + sunset.getMinute() - sunrise.getMinute();

		float fMinutesAfterSunrise = ((LocalTime.now().getHour()-sunrise.getHour())*60 + (LocalTime.now().getMinute())-sunrise.getMinute());

		//Work out the minecraft ticks count
		fractionOfDaylightComplete = fMinutesAfterSunrise/fDaylight;
		lSeasonalTime = (long) (mcSunRise + fractionOfDaylightComplete * mcSunLight);

		if (fMinutesAfterSunrise < 0) //Sun not risen
		{
			if (lSeasonalTime < 22000)
			{
				float fMinutesOfNightAlg = fSunrise - 937 *(fDaylight/mcSunLight);
				//	System.out.println("Mins btween sunset + nghtalg: "+fMinutesOfNightAlg);
				fractionOfNightlightComplete = ((float)(LocalTime.now().getHour()*60 + LocalTime.now().getMinute())/ (fMinutesOfNightAlg));

				//	System.out.println(fractionOfNightlightComplete);
				lSeasonalNightTime = (long) (18000 + fractionOfNightlightComplete*4000);
				lSeasonalTime = lSeasonalNightTime;
			}
			else
			{

			}
		}
		else if (fMinutesAfterSunrise < fDaylight) //Sun still up
		{
			//Delt with as standard
		}

		else //Sun has set but is before midnight
		{
			lSeasonalTime = lSeasonalTime - 24000;

			//If the seasonal time goes over 14000, use night algorithm
			if (!(lSeasonalTime < 14000))
			{
				//Minutes of the day that the night algorithm begins
				float fMinutesOfNightAlg = fSunset + 957 *(fDaylight/mcSunLight);

				//Fration of night alg to midnight that is done
				fractionOfNightlightComplete = ((float)(LocalTime.now().getHour()*60 + LocalTime.now().getMinute())-(fMinutesOfNightAlg))/ (1440 - (fMinutesOfNightAlg));

				//Ticks
				lSeasonalNightTime = (long) (14000 + fractionOfNightlightComplete*4000);
				lSeasonalTime = lSeasonalNightTime;
			}
		}

		lSeasonalTime = lSeasonalTime % 24000;

		player.setPlayerTime(lSeasonalTime, false);

		militaryTime = (LocalTime.now().getHour()+config.getLong("HourOffset"))*100 + LocalTime.now().getMinute();
		return militaryTime;
	}

	public void mysqlSetup()
	{
		this.HOST = config.getString("MySQL_host");
		this.PORT = config.getInt("MySQL_port");
		this.Database = config.getString("MySQL_database");
		this.USER = config.getString("MySQL_username");
		this.PASSWORD = config.getString("MySQL_password");
		this.WeatherPreferences = config.getString("MySQL_weatherpreferences");

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
				//	e.printStackTrace();
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
		return;
	}

	public boolean createWeatherPrefsTable()
	{
		boolean bSuccess = false;
		int iCount = -1;

		try
		{
			//Adds a weather pref table
			sql = "CREATE TABLE IF NOT EXISTS `"+this.Database+"`.`"+this.WeatherPreferences+"` (\n" + 
					"  `UUID` VARCHAR(36) NOT NULL,\n" + 
					"  `LiveWeather` TINYINT NOT NULL DEFAULT 0,\n" + 
					"  `LiveFog` TINYINT NOT NULL DEFAULT 0,\n" + 
					"  `LiveTime` TINYINT NOT NULL DEFAULT 0,\n" + 
					"  `Seasonal` TINYINT NOT NULL DEFAULT 1,\n" + 
					"  UNIQUE INDEX `UUID_UNIQUE` (`UUID` ASC));";
			SQL = connection.createStatement();

			//Executes the update and returns how many rows were changed
			iCount = SQL.executeUpdate(sql);

			//If only 1 record was changed, success is set to true
			if (iCount == 1)
			{
				Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Created new liveweather preferences table");			
				bSuccess = true;
			}
		}
		catch(SQLException se)
		{
			se.printStackTrace();
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "SQL Error creating new liveweather preferences table");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error creating new liveweather preferences table");
		}
		return (bSuccess);
	}

	public static Main getInstance()
	{
		return instance;
	}

	public Connection getConnection()
	{
		try
		{
			if(connection == null)
			{
				connect();
			}
			else if(connection.isClosed())
			{
				connect();
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return connection;
	}
	
	public ArrayList<UUID> getLiveList()
	{
		return playersWithLive;
	}

} //End Class

//Created by Bluecarpet in London