
/**
 * @author 14walkerg
 * @date 4 Jan 2021
 * @time 17:55:24
 */
package LiveWeather.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitScheduler;

import LiveWeather.Main;
import LiveWeather.UpdateCall;

public class JoinEvent implements Listener
{
	private final Main plugin;
	
	public JoinEvent(Main plugin)
	{
		this.plugin = plugin;
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void playerJoin(PlayerJoinEvent event)
	{		
		int minute = (int) 1200L;
		
		UpdateCall up = new UpdateCall(plugin, event);
		up.initialRun();
		
		BukkitScheduler scheduler = plugin.getServer().getScheduler();
		scheduler.scheduleSyncRepeatingTask(plugin, new UpdateCall(plugin, event) ,
				minute * plugin.getConfig().getInt("timerInterval"),
				minute * plugin.getConfig().getInt("timerInterval"));
	}
}
//End Class

//Created by Bluecarpet in London











