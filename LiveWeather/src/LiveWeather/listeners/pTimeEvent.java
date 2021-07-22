
/**
 * @author 14walkerg
 * @date 9 Jan 2021
 * @time 20:46:36
 */
package LiveWeather.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import LiveWeather.Main;
import LiveWeather.WeatherPreference;

public class pTimeEvent implements Listener
{	
	public pTimeEvent(Main plugin)
	{
		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[Liveweather] pTimeEvent loaded");
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void ptimeCalled(PlayerCommandPreprocessEvent event)
	{
		if (event.getMessage().toLowerCase().startsWith(("/ptime")))
		{
			//Convert sender to player
			Player p = event.getPlayer();
					
			//Get player's preferences
			
			//Initiate player preferences
			WeatherPreference wp = new WeatherPreference();

			//Sets the UUID into the wp class
			wp.setUUID(p.getUniqueId().toString());
			
			//Gets weather preferences
			wp.fetchFromUUID();
			
			if (wp.getTime())
			{
				wp.toogleTime();
				p.sendMessage(ChatColor.GOLD + "Live time disabled");
			}
		}
		else if (event.getMessage().toLowerCase().startsWith(("/pweather")))
		{
			//Convert sender to player
			Player p = event.getPlayer();
					
			//Get player's preferences
			
			//Initiate player preferences
			WeatherPreference wp = new WeatherPreference();

			//Sets the UUID into the wp class
			wp.setUUID(p.getUniqueId().toString());
			
			//Gets weather preferences
			wp.fetchFromUUID();
			
			if (wp.getWeather())
			{
				wp.toogleWeather();
				p.sendMessage(ChatColor.GOLD + "Live weather disabled");
			}
		}
	}

}
//End Class

//Created by Bluecarpet in London