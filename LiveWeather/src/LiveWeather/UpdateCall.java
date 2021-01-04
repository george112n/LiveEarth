package LiveWeather;

import org.bukkit.ChatColor;
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
	private final JavaPlugin plugin;
	private PlayerJoinEvent event;
	
	public UpdateCall(JavaPlugin plugin, PlayerJoinEvent event)
	{
		this.plugin = plugin;
		this.event = event;
	}
	
	@Override
	public void run()
	{
		Player player = event.getPlayer();
		
		//Gets UUID of player
		String UUID = player.getUniqueId().toString();

		//Initiates wp
		WeatherPreference wp = new WeatherPreference();

		//Sets UUID into wp
		wp.setUUID(UUID);

		//If there isn't a wp record for this UUID, create one
		if (!wp.exists())
		{
			wp.createNewPref();
		}
		//Load preferences into wp
		wp.fetchFromUUID();

		//Avoids making sperate api request for weather and fog
		if (wp.getWeather() && wp.getFog())
		{
			LiveWeatherUtil LWU = new LiveWeatherUtil(player);
			LWU.call(true, true);
		}
		else if (wp.getWeather())
		{
			LiveWeatherUtil LWU = new LiveWeatherUtil(player);
			LWU.call(true, false);
		}
		else if (wp.getFog())
		{
			LiveWeatherUtil LWU = new LiveWeatherUtil(player);
			LWU.call(false, true);
		}

		//Updates time
		if (wp.getTime())
			Main.updateTime(player);
	}
	
	public void initialRun()
	{
		Player player = event.getPlayer();
		
		//Gets UUID of player joined
		String UUID = player.getUniqueId().toString();

		//Initiates wp
		WeatherPreference wp = new WeatherPreference();

		//Sets UUID into wp
		wp.setUUID(UUID);

		//If there isn't a wp record for this UUID, create one
		if (!wp.exists())
		{
			wp.createNewPref();
		}
		
		//Load preferences into wp
		wp.fetchFromUUID();

		//Avoids making sperate api request for weather and fog
		if (wp.getWeather() && wp.getFog())
		{
			LiveWeatherUtil LWU = new LiveWeatherUtil(player);
			LWU.call(true, true);
		}
		else if (wp.getWeather())
		{
			LiveWeatherUtil LWU = new LiveWeatherUtil(player);
			LWU.call(true, false);
		}
		else if (wp.getFog())
		{
			LiveWeatherUtil LWU = new LiveWeatherUtil(player);
			LWU.call(false, true);
		}

		//Updates time
		if (wp.getTime())
		{
		//	player.sendMessage(ChatColor.GOLD + "Live time enabled");
			long lCurrentTime = Main.updateTime(player);
			player.sendMessage(ChatColor.GOLD + "Time set to "+ChatColor.RED +lCurrentTime);
		}
	}
}
//End Class

//Created by Bluecarpet in London