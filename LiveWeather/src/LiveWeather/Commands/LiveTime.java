package LiveWeather.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import LiveWeather.WeatherPreference;
import LiveWeather.LiveWeatherUtil;

/**
 * @author 14walkerg
 * @date 3 Jan 2021
 * @time 14:08:09
 */

public class LiveTime implements CommandExecutor
{
	@EventHandler
	public void onCmd(PlayerCommandPreprocessEvent e)
	{
		if (!(e.getPlayer().hasPermission("Liveweather.time.toggleSelf") || e.getPlayer().hasPermission("Liveweather.time.toggleOthers")))
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
			sender.sendMessage("&cYou cannot add a player to a region!");
			return true;
		}

		//Convert sender to player
		Player p = (Player) sender;

		if (args.length == 0)
		{
			if (!p.hasPermission("Liveweather.time.toggleSelf"))
			{
				p.sendMessage(ChatColor.RED +"You do not have permission to toogle your live time on and off");
				return true;
			}
			//Initiate player preferences
			WeatherPreference wp = new WeatherPreference();

			//Sets the UUID into the wp class
			wp.setUUID(p.getUniqueId().toString());

			//Gets weather preferences
			wp.fetchFromUUID();

			boolean newState;
			long lCurrentTime;

			newState = wp.toogleTime();
			if (newState)
			{
				p.sendMessage(ChatColor.GOLD + "Live time enabled");
				LiveWeatherUtil LWU = new LiveWeatherUtil(p);
				LWU.call(false, true, false);
				lCurrentTime = LWU.lTime;
				String szTime = String.format("%02d:%02d", lCurrentTime/100, lCurrentTime %100);
				p.sendMessage(ChatColor.GOLD + "Time set to "+ChatColor.RED +szTime);
			}
			else
			{
				p.sendMessage(ChatColor.GOLD + "Live time disabled");
				p.resetPlayerTime();
			}
			return true;
		}
		else if (args.length > 0)
		{
			if (!p.hasPermission("Liveweather.time.toggleOthers"))
			{
				if (!p.hasPermission("Liveweather.time.toggleSelf"))
				{
					p.sendMessage(ChatColor.RED +"You do not have permission to toogle your live time on and off");
					return true;
				}
				//Initiate player preferences
				WeatherPreference wp = new WeatherPreference();

				//Sets the UUID into the wp class
				wp.setUUID(p.getUniqueId().toString());

				//Gets weather preferences
				wp.fetchFromUUID();

				boolean newState;
				long lCurrentTime;

				newState = wp.toogleTime();
				if (newState)
				{
					p.sendMessage(ChatColor.GOLD + "Live time enabled");
					LiveWeatherUtil LWU = new LiveWeatherUtil(p);
					LWU.call(false, true, false);
					lCurrentTime = LWU.lTime;
					String szTime = String.format("%02d:%02d", lCurrentTime/100, lCurrentTime %100);
					p.sendMessage(ChatColor.GOLD + "Time set to "+ChatColor.RED +szTime);
				}
				else
				{
					p.sendMessage(ChatColor.GOLD + "Live time disabled");
					p.resetPlayerTime();
				}
			}
			else
			{

				Player user = Bukkit.getPlayer(args[0]);


				if (user == null)
				{
					p.sendMessage(ChatColor.RED + args[0] + " is not online!");
					return true;
				}

				//Initiate player preferences
				WeatherPreference wp = new WeatherPreference();

				//Sets the UUID into the wp class
				wp.setUUID(user.getUniqueId().toString());

				//Gets weather preferences
				wp.fetchFromUUID();

				boolean newState;
				long lCurrentTime;

				newState = wp.toogleTime();
				if (newState)
				{
					p.sendMessage(ChatColor.GOLD + "Live time enabled for "+user.getName());
					user.sendMessage(ChatColor.GOLD +p.getName() +" enabled your live time");
					LiveWeatherUtil LWU = new LiveWeatherUtil(p);
					LWU.call(false, true, false);
					lCurrentTime = LWU.lTime;
					String szTime = String.format("%02d:%02d", lCurrentTime/100, lCurrentTime %100);
					user.sendMessage(ChatColor.GOLD + "Time set to "+ChatColor.RED +szTime);
				}
				else
				{
					p.sendMessage(ChatColor.GOLD + "Live time disabled for "+user.getName());
					user.sendMessage(ChatColor.GOLD +p.getName() +" disabled your live time");
					user.resetPlayerTime();
				}
				return true;
			}
		}

		return true;

		//Get player's preferences


		/*	if (args.length > 0)
		{
		/*	if (args[0].equalsIgnoreCase("Seasonal"))
			{
				newState = wp.toogleSeasonal();
				if (newState)
				{
					p.sendMessage(ChatColor.GOLD + "Seasonal time enabled");
					LiveWeatherUtil LWU = new LiveWeatherUtil(p);
					LWU.call(false, false, true, false);
					lCurrentTime = LWU.lTime;
					String szTime = String.format("%02d:%02d", lCurrentTime/100, lCurrentTime %100);
					p.sendMessage(ChatColor.GOLD + "Time updated for "+ChatColor.RED +szTime);
				}
				else
				{
					p.sendMessage(ChatColor.GOLD + "Seasonal time disabled");
					lCurrentTime = Main.updateTime(p);
					String szTime = String.format("%02d:%02d", lCurrentTime/100, lCurrentTime %100);
					p.sendMessage(ChatColor.GOLD + "Time updated for "+ChatColor.RED +szTime);
				}
				return true;
			}
		 */
		/*	else
			{
				p.sendMessage(ChatColor.RED + "/livetime [seasonal]");
				return true;
			}
		/*
		}
		 */
		/*	else
		{
			newState = wp.toogleTime();
			if (newState)
			{
				p.sendMessage(ChatColor.GOLD + "Live time enabled");
			//	if (wp.getSeasonal())
			//	{
					LiveWeatherUtil LWU = new LiveWeatherUtil(p);
					LWU.call(false, false, true, false);
					lCurrentTime = LWU.lTime;
					String szTime = String.format("%02d:%02d", lCurrentTime/100, lCurrentTime %100);
					p.sendMessage(ChatColor.GOLD + "Time set to "+ChatColor.RED +szTime);
			//	}
			/*	else
				{
					lCurrentTime = Main.updateTime(p);
					String szTime = String.format("%02d:%02d", lCurrentTime/100, lCurrentTime %100);
					p.sendMessage(ChatColor.GOLD + "Time set to non seasonal "+ChatColor.RED +szTime);
				}

			}
			else
			{
				p.sendMessage(ChatColor.GOLD + "Live time disabled");
				p.resetPlayerTime();
			}
		}
		return true;
		 */
	}
} //End Class

//Created by Bluecarpet in London