
/**
 * @author 14walkerg
 * @date 3 Jan 2021
 * @time 23:37:13
 */
package LiveWeather.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import LiveWeather.LiveWeatherUtil;
import LiveWeather.WeatherPreference;

public class LiveWeather implements CommandExecutor
{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		//Check is command sender is a player
		if (!(sender instanceof Player)) {
			sender.sendMessage("&cYou cannot give a non player weather!");
			return true;
		}

		//Convert sender to player
		Player p = (Player) sender;
		
		//Initiate player preferences
		WeatherPreference wp = new WeatherPreference();
		
		if (args.length > 1)
		{
			p.sendMessage(ChatColor.RED + "/liveweather [fog]");
			return true;
		}
		
		//Get player's preferences
		else 
		{
			//Sets the UUID into the wp class
			wp.setUUID(p.getUniqueId().toString());
			
			//Gets weather preferences
			wp.fetchFromUUID();
		}
		
		//Used for toggling live weather on and off
		if (args.length == 0)
		{
			//Fog is turned off within function if weather is turned off
			boolean newState = wp.toogleWeather();
			if (newState)
			{
				p.sendMessage(ChatColor.GOLD + "Live weather enabled. Enable fog with /liveweather fog");
				LiveWeatherUtil LWU = new LiveWeatherUtil(p);
				//Updates whether
				LWU.call(true, false);
				p.sendMessage(ChatColor.GOLD + "Set weather to"+ChatColor.RED +p.getPlayerWeather());
			}
			else
			{
				p.sendMessage(ChatColor.GOLD + "Live weather and fog disabled");
				p.resetPlayerWeather();
			}
		}
		
		//Used for toggling live fog on and off
		else if (args[0].equalsIgnoreCase("fog"))
		{
			boolean newState = wp.toogleFog();
			if (newState)
			{
				p.sendMessage(ChatColor.GOLD + "Live fog enabled");
				LiveWeatherUtil LWU = new LiveWeatherUtil(p);
				//Updates fog
				LWU.call(false, true);
			//	p.sendMessage(ChatColor.GOLD + "Set fog to"+ChatColor.RED +p.getPlayerWeather());
			}
			else
			{
				p.sendMessage(ChatColor.GOLD + "Live fog disabled");
			//	p.resetPlayerWeather();
				//Perform fog reset
			}
		}
		return true;
	}
}
//End Class

//Created by Bluecarpet in London