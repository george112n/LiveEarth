
/**
 * @author 14walkerg
 * @date 30 Jan 2021
 * @time 11:18:21
 */

package LiveWeather.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import LiveWeather.Main;
import LiveWeather.UpdateCall;

public class BigMoveEvent implements Listener
{
	private final Main plugin;
	
	public BigMoveEvent(Main plugin)
	{
		Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "BigMoveEvent loaded");
		this.plugin = plugin;
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "BigMoveEvent ready");
	}
	
	@EventHandler
	public void TeleportEvent(PlayerTeleportEvent event)
	{ 
		//Convert sender to player
		Player player = event.getPlayer();
		
		if (event.getCause() == TeleportCause.COMMAND || event.getCause() == TeleportCause.PLUGIN)
		{
			double FromX = event.getFrom().getX();
			double FromZ = event.getFrom().getZ();
			double ToX = event.getTo().getX();
			double ToZ = event.getTo().getZ();
						
			//Checks whether it is outside a 2km radius
			if (Math.sqrt( (FromX - ToX)*(FromX - ToX) + (FromZ - ToZ)*(FromZ - ToZ) ) > 2000)
			{
				UpdateCall up = new UpdateCall(plugin, player);
				up.locationMoveRun(event.getTo());
			}
		}
	}
} //End Class

//Created by Bluecarpet in London