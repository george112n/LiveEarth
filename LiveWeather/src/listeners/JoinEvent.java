
/**
 * @author 14walkerg
 * @date 4 Jan 2021
 * @time 17:55:24
 */
package LiveWeather.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import LiveWeather.Main;
import LiveWeather.UpdateCall;

public class JoinEvent implements Listener
{
	private final Main plugin;
	
	public JoinEvent(Main plugin)
	{
		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[Liveweather] JoinEvent loaded");
		this.plugin = plugin;
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void playerJoin(PlayerJoinEvent event)
	{
		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[Liveweather] Player joined, calling update call");
		UpdateCall up = new UpdateCall(plugin, event);
		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[Liveweather] Running initial run");
		up.initialRun();	
	}
}
//End Class

//Created by Bluecarpet in London