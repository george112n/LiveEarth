package LiveWeather;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author 14walkerg
 * @date 4 Jan 2021
 * @time 18:20:43
 */

public class UpdateCall extends BukkitRunnable
{
//	private final JavaPlugin plugin;
	private Player player;
	
	public UpdateCall(JavaPlugin plugin, PlayerJoinEvent event)
	{
	//	this.plugin = plugin;
		this.player = event.getPlayer();
	}
	
	public UpdateCall(JavaPlugin plugin)
	{
	//	this.plugin = plugin;
	}
	
	public UpdateCall(JavaPlugin plugin, Player player)
	{
	//	this.plugin = plugin;
		this.player = player;
	}

	@Override
	public void run()
	{
		final Collection<? extends Player> players = Bukkit.getOnlinePlayers();

		//Initiates wp
		WeatherPreference wp = new WeatherPreference();
		
		Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[LiveWeather] Running liveweather and time update");
		
		//Gets the list of players with live
		ArrayList<UUID> playersWithLive = Main.getInstance().getLiveList();
		
		for (Player player: players)
		{
			//Checks whether they are in live first
			for (UUID uuid : playersWithLive)
			{
				if (uuid.equals(player.getUniqueId()))
				{
					LiveUpdateRun(player);
					continue;
				}
			}
			
			boolean bSeasonalTime = false;
			
			//Gets UUID of player
			String UUID = player.getUniqueId().toString();
			
			//Sets UUID into wp
			wp.setUUID(UUID);

			//If there isn't a wp record for this UUID, create one
			if (!wp.exists())
			{
				wp.createNewPref();
			}
			//If this didn't work, skip them
			if (!wp.exists())
			{
				continue;
			}
			//Load preferences into wp
			wp.fetchFromUUID();
			
			//Updates time
			bSeasonalTime = wp.getTime();
			
			//Avoids making sperate api request for weather and fog
			//Runs seasonal time update
			if (wp.getWeather() && wp.getFog())
			{
				LiveWeatherUtil LWU = new LiveWeatherUtil(player);
				LWU.call(true, bSeasonalTime, false);
			}
			else if (wp.getWeather())
			{
				LiveWeatherUtil LWU = new LiveWeatherUtil(player);
				LWU.call(true, bSeasonalTime, false);
			}
			else if (wp.getFog())
			{
				LiveWeatherUtil LWU = new LiveWeatherUtil(player);
				LWU.call(false, bSeasonalTime, false);
			}
			//Checks whether time is on and makes the api request if it is not handled through the weather api request
			else if (bSeasonalTime)
			{
				LiveWeatherUtil LWU = new LiveWeatherUtil(player);
				LWU.call(false, bSeasonalTime, false);
			}
		}
	}
	
	public void initialRun()
	{
		Player player = this.player;
		
		boolean bSeasonalTime = false;
		boolean bWeather = false;
		
		long lCurrentTime;
		
		String szTime = "";
		
		//Gets UUID of player joined
		String UUID = player.getUniqueId().toString();
		
		//Initiates wp
		WeatherPreference wp = new WeatherPreference();
		
		//Sets UUID into wp
		wp.setUUID(UUID);
		
		//If there isn't a wp record for this UUID, create one
		if (!wp.exists())
		{
			Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "No weather preference record exists so creating one for "+player.getUniqueId().toString());
			boolean bCreated;
			bCreated = wp.createNewPref();
			if (bCreated)
				Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Created new liveweather preference record for "+player.getUniqueId().toString());
			else
			{
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Unable to create new liveweather preference record for "+player.getUniqueId().toString());
			}
		}
		
		//Gets weather preferences
		wp.fetchFromUUID();
		
		//Updates time
		bSeasonalTime = wp.getTime();
		
	/*	if (wp.getTime())
		{
			//Gets whether seasonal or not
			bSeasonalTime = wp.getSeasonal();
			
			if (!bSeasonalTime) //If not seasonal, update time
			{
				lCurrentTime = Main.updateTime(player);
				szTime = String.format("%02d:%02d", lCurrentTime/100, lCurrentTime %100);
				player.sendMessage(ChatColor.GOLD + "Time set to non seasonal "+ChatColor.RED +szTime);
			}
		}
	*/	
		//Avoids making sperate api request for weather and fog
		//Time handled through here as well
		if (wp.getWeather())
		{
			bWeather = true;
		}


		LiveWeatherUtil LWU = new LiveWeatherUtil(player);
		LWU.call(bWeather, bSeasonalTime, true);
		lCurrentTime = LWU.lTime;
		szTime = String.format("%02d:%02d", lCurrentTime/100, lCurrentTime %100);
		
		if (bSeasonalTime)
		{
			player.sendMessage(ChatColor.GOLD + "Time set to "+ChatColor.RED +szTime);
		}
	}
	
	public void locationMoveRun(Location locationTo)
	{
		Player player = this.player;
		
		//Gets the list of players with live
		ArrayList<UUID> playersWithLive = Main.getInstance().getLiveList();
		
		//Checks whether they are in live first
		for (UUID uuid : playersWithLive)
		{
			if (uuid.equals(player.getUniqueId()))
			{
				LiveUpdateRun(player);
				return;
			}
		}
			
		boolean bSeasonalTime = false;
				
		//Gets UUID of player joined
		String UUID = player.getUniqueId().toString();
		
		//Initiates wp
		WeatherPreference wp = new WeatherPreference();
		
		//Sets UUID into wp
		wp.setUUID(UUID);
		
		//If there isn't a wp record for this UUID, create one
		if (!wp.exists())
		{
			Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[Liveweather] No weather preference record exists so creating one for "+player.getUniqueId().toString());
			boolean bCreated;
			bCreated = wp.createNewPref();
			if (bCreated)
				Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[Liveweather] Created new liveweather preference record for "+player.getUniqueId().toString());
			else
			{
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[Liveweather] Unable to create new live weather preference record for "+player.getUniqueId().toString());
				return;
			}
		}
		
		//Gets weather preferences
		wp.fetchFromUUID();
		
		//Updates time
		bSeasonalTime = wp.getTime();
		
	/*	if (wp.getTime())
		{
			//Gets whether seasonal or not
			bSeasonalTime = wp.getSeasonal();
			
			if (!bSeasonalTime) //If not seasonal, update time
			{
				Main.updateTime(player);
			}
		}
	*/	
		//Avoids making sperate api request for weather and fog
		//Also, if time is seasonal, it will be run through here, to avoid 2 api requests
		if (wp.getWeather())
		{
			LiveWeatherUtil LWU = new LiveWeatherUtil(player, locationTo);
			LWU.call(true, bSeasonalTime, true);
			player.sendMessage(ChatColor.GOLD + "Weather set to "+ChatColor.RED +LWU.szWeather +ChatColor.GOLD+" at "+ChatColor.RED +LWU.szLocation);
		}
		//Checks whether time was set on and makes the api request if it is not handled through a weather api request
		else if (bSeasonalTime)
		{
			LiveWeatherUtil LWU = new LiveWeatherUtil(player, locationTo);
			LWU.call(true, bSeasonalTime, true);
		}
	}
	
	public static void InitialLiveRun(Player p)
	{
		LiveWeatherUtil LWU = new LiveWeatherUtil(p);
		LWU.call(true, true, false);
		p.setGameMode(GameMode.ADVENTURE);
		p.setAllowFlight(false);
		p.setWalkSpeed((float) 0.14);
	}
	
	//Same as above for now but is here for future
	private void LiveUpdateRun(Player p)
	{
		LiveWeatherUtil LWU = new LiveWeatherUtil(p);
		LWU.call(true, true, false);
		p.setGameMode(GameMode.ADVENTURE);
		p.setAllowFlight(false);
		p.setWalkSpeed((float) 0.14);
	}
	
	
} //End Class

//Created by Bluecarpet in London