
/**
 * @author 14walkerg
 * @date 3 Jan 2021
 * @time 23:37:13
 */
package LiveWeather.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import LiveWeather.LiveWeatherUtil;
import LiveWeather.WeatherPreference;

public class LiveWeather implements CommandExecutor
{
	@EventHandler
	public void onCmd(PlayerCommandPreprocessEvent e)
	{
		if (!(e.getPlayer().hasPermission("Liveweather.weather.toggleSelf") || e.getPlayer().hasPermission("Liveweather.weather.toggleOthers")))
		{
			e.setCancelled(true);
			e.getPlayer().sendMessage(ChatColor.RED +"You do not have permission to use this command");
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		//Check is command sender is a player
		if (!(sender instanceof Player))
		{
			sender.sendMessage("&cYou cannot give a non player weather!");
			return true;
		}

		//Convert sender to player
		Player p = (Player) sender;

		//Initiate player preferences
		WeatherPreference wp = new WeatherPreference();

		//Used for toggling live weather on and off
		if (args.length == 0)
		{
			if (!p.hasPermission("Liveweather.weather.toggleSelf"))
			{
				p.sendMessage(ChatColor.RED +"You do not have permission to toggle your live weather");
				return true;
			}
			//Sets the UUID into the wp class
			wp.setUUID(p.getUniqueId().toString());

			//Gets weather preferences
			wp.fetchFromUUID();

			//Fog is turned off within function if weather is turned off
			boolean newState = wp.toogleWeather();
			if (newState)
			{
				p.sendMessage(ChatColor.GOLD + "Live weather enabled.");
				LiveWeatherUtil LWU = new LiveWeatherUtil(p);
				//Updates whether
				LWU.call(true, false, true);
				p.sendMessage(ChatColor.GOLD + "Weather set to "+ChatColor.RED +LWU.szWeather +ChatColor.GOLD+" at "+ChatColor.RED +LWU.szLocation);
			}
			else
			{
				p.sendMessage(ChatColor.GOLD + "Live weather disabled");
				p.resetPlayerWeather();
			}
			return true;
		}

		//For fog or player
		if (args.length == 1)
		{
			//Attempt to get player
			if (p.hasPermission("Liveweather.weather.toggleOthers"))
			{
				//-----------Forked from Elgamer-----------
				Player user = Bukkit.getPlayer(args[0]);

				if (user == null)
				{
					p.sendMessage(ChatColor.RED + args[0] + " is not online!");
					return true;
				}

				//Sets the UUID into the wp class
				wp.setUUID(user.getUniqueId().toString());

				//Gets weather preferences
				wp.fetchFromUUID();

				//Fog is turned off within function if weather is turned off
				boolean newState = wp.toogleWeather();
				if (newState)
				{
					p.sendMessage(ChatColor.GOLD + "Live weather enabled for "+user.getName());
					user.sendMessage(ChatColor.GOLD +p.getName() +" enabled your live weather");
					LiveWeatherUtil LWU = new LiveWeatherUtil(user);
					//Updates whether
					LWU.call(true, false, true);
					user.sendMessage(ChatColor.GOLD + "Weather set to "+ChatColor.RED +LWU.szWeather +ChatColor.GOLD+" at "+ChatColor.RED +LWU.szLocation);
				}
				else
				{
					p.sendMessage(ChatColor.GOLD + "Live weather disabled for "+user.getName());
					user.sendMessage(ChatColor.GOLD +p.getName() +" disabled your live weather");
					user.resetPlayerWeather();
				}
				return true;
			} else {
				p.sendMessage(ChatColor.RED + "You do not have permission to toggle liveweather of others!");
			}

		}

		return true;
	}
} //End Class

//Created by Bluecarpet in London