package LiveWeather.Commands;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import LiveWeather.LiveWeatherUtil;
import LiveWeather.Main;
import LiveWeather.UpdateCall;
import LiveWeather.WeatherPreference;

public class Live implements CommandExecutor
{
	@EventHandler
	public void onCmd(PlayerCommandPreprocessEvent e)
	{
		if (!(e.getPlayer().hasPermission("Liveweather.live.toggleSelf") || e.getPlayer().hasPermission("Liveweather.live.toggleOthers")))
		{
			e.setCancelled(true);
			e.getPlayer().sendMessage(ChatColor.RED +"You do not have permission to use this command");
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		//Check is command sender is a player
		if (!(sender instanceof Player))
		{
			sender.sendMessage("&cYou cannot add a player to a region!");
			return true;
		}

		//Convert sender to player
		Player p = (Player) sender;
		
		//Gets the list of players with live
		ArrayList<UUID> playersWithLive = Main.getInstance().getLiveList();
		for (UUID uuid : playersWithLive)
		{
			if (uuid.equals(p.getUniqueId()))
			{
				playersWithLive.remove(uuid);
				p.resetPlayerWeather();
				p.resetPlayerTime();
				p.setGameMode(GameMode.CREATIVE);
				p.setWalkSpeed((float) 0.2);
				
				//Now reenables liveweather or livetime
				
				//Initiate player preferences
				WeatherPreference wp = new WeatherPreference();

				//Sets the UUID into the wp class
				wp.setUUID(p.getUniqueId().toString());

				//Gets weather preferences
				wp.fetchFromUUID();
				
				if (wp.getWeather() || wp.getTime())
				{
					LiveWeatherUtil LWU = new LiveWeatherUtil(p);
					LWU.call(wp.getWeather(), wp.getTime(), true);
					long lCurrentTime = LWU.lTime;
					String szTime = String.format("%02d:%02d", lCurrentTime/100, lCurrentTime %100);
					
					if (wp.getTime())
					{
						p.sendMessage(ChatColor.GOLD + "Time set to "+ChatColor.RED +szTime);
					}
				}
				
				//Remove player from list
				playersWithLive.remove(p.getUniqueId());
				return true;
			}
		}
		//Only reaches this if the player isn't in the /live list
		//Adds player to the live list
		playersWithLive.add(p.getUniqueId());
		UpdateCall.InitialLiveRun(p);
		return true;
	}
}
