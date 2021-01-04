package LiveWeather.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import LiveWeather.WeatherPreference;
import LiveWeather.Main;

/**
 * @author 14walkerg
 * @date 3 Jan 2021
 * @time 14:08:09
 */

public class LiveTime implements CommandExecutor
{
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
		
		//Initiate player preferences
		WeatherPreference wp = new WeatherPreference();

		//Sets the UUID into the wp class
		wp.setUUID(p.getUniqueId().toString());
		
		//Gets weather preferences
		wp.fetchFromUUID();
		
		boolean newState = wp.toogleTime();
		if (newState)
		{
			p.sendMessage(ChatColor.GOLD + "Live time enabled");
			long lCurrentTime = Main.updateTime(p);
			p.sendMessage(ChatColor.GOLD + "Time set to "+ChatColor.RED +lCurrentTime);
		}
		else
		{
			p.sendMessage(ChatColor.GOLD + "Live time disabled");
			p.resetPlayerTime();
		}
		return true;
	}
}
//End Class

//Created by Bluecarpet in London