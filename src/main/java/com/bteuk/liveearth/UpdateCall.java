package com.bteuk.liveearth;

import com.bteuk.liveearth.storage.PreferencesStore;
import com.bteuk.liveearth.weather.WeatherUtils;
import net.buildtheearth.terraminusminus.projection.OutOfProjectionBoundsException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.UUID;

/**
 * @author 14walkerg
 * @date 4 Jan 2021
 * @time 18:20:43
 */

public class UpdateCall extends BukkitRunnable {
    private final LiveEarth plugin;
    private Player player;

    public UpdateCall(LiveEarth plugin, PlayerJoinEvent event) {
        this.plugin = plugin;
        this.player = event.getPlayer();
    }

    public UpdateCall(LiveEarth plugin) {
        this.plugin = plugin;
    }

    public UpdateCall(LiveEarth plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    public static void InitialLiveRun(Player p, LiveEarth plugin) throws OutOfProjectionBoundsException {
        WeatherUtils LWU = new WeatherUtils(p, plugin);
        LWU.call(true, true, false);
        p.setGameMode(GameMode.ADVENTURE);
        p.setAllowFlight(false);
        p.setWalkSpeed((float) 0.14);
        p.setResourcePack(plugin.getConfig().getString("resourcePack"));
    }

    @Override
    public void run() {
        final Collection<? extends Player> players = Bukkit.getOnlinePlayers();

        plugin.getLogger().info("Updating live states!");

        //Gets the list of players with live
        PreferencesStore preferencesStore = plugin.getPreferencesStore();

        for (Player player : players) {
            //Checks whether they are in live first
            if (preferencesStore.getLive(player.getUniqueId())) {
                try {
                    LiveUpdateRun(player);
                } catch (OutOfProjectionBoundsException ignored) {
                }
            }

            boolean bSeasonalTime = preferencesStore.getTime(player.getUniqueId());

            //Avoids making sperate api request for weather and fog
            //Runs seasonal time update
            if (preferencesStore.getWeather(player.getUniqueId())) {
                WeatherUtils LWU = new WeatherUtils(player, plugin);
                try {
                    LWU.call(true, bSeasonalTime, false);
                } catch (OutOfProjectionBoundsException ignored) {
                }
            }
            //Checks whether time is on and makes the api request if it is not handled through the weather api request
            else if (bSeasonalTime) {
                WeatherUtils LWU = new WeatherUtils(player, plugin);
                try {
                    LWU.call(false, true, false);
                } catch (OutOfProjectionBoundsException ignored) {
                }
            }
        }
    }

    public void initialRun() throws OutOfProjectionBoundsException {
        Player player = this.player;

        boolean bSeasonalTime = false;
        boolean bWeather = false;

        long lCurrentTime;

        String szTime = "";

        //Gets UUID of player joined
        UUID uuid = player.getUniqueId();

        PreferencesStore preferencesStore = plugin.getPreferencesStore();


        //Gets weather preferences
        bSeasonalTime = preferencesStore.getTime(uuid);

        //Avoids making sperate api request for weather and fog
        //Time handled through here as well
        bWeather = preferencesStore.getWeather(uuid);


        WeatherUtils LWU = new WeatherUtils(player, plugin);
        LWU.call(bWeather, bSeasonalTime, true);
        lCurrentTime = LWU.lTime;
        szTime = String.format("%02d:%02d", lCurrentTime / 100, lCurrentTime % 100);

        if (bSeasonalTime) {
            player.sendMessage(LiveEarth.prefix + ChatColor.GOLD + "Time set to " + ChatColor.RED + szTime);
        }
    }

    public void locationMoveRun(Location locationTo) throws OutOfProjectionBoundsException {
        Player player = this.player;

        UUID uuid = player.getUniqueId();

        PreferencesStore preferencesStore = plugin.getPreferencesStore();

        //Checks whether they are in live first
        if (preferencesStore.getLive(player.getUniqueId())) {
            try {
                LiveUpdateRun(player);
                return;
            } catch (OutOfProjectionBoundsException ignored) {
                return;
            }
        }

        boolean bSeasonalTime = preferencesStore.getTime(uuid);

        //Avoids making sperate api request for weather and fog
        //Time handled through here as well
        boolean bWeather = preferencesStore.getWeather(uuid);

        //Avoids making sperate api request for weather and fog
        //Also, if time is seasonal, it will be run through here, to avoid 2 api requests
        if (bWeather) {
            WeatherUtils LWU = new WeatherUtils(player, plugin, locationTo);
            LWU.call(true, bSeasonalTime, true);
            player.sendMessage(LiveEarth.prefix + ChatColor.GOLD + "Weather set to " + ChatColor.RED + LWU.szWeather + ChatColor.GOLD + " at " + ChatColor.RED + LWU.szLocation);
        }
        //Checks whether time was set on and makes the api request if it is not handled through a weather api request
        else if (bSeasonalTime) {
            WeatherUtils LWU = new WeatherUtils(player, plugin, locationTo);
            LWU.call(false, bSeasonalTime, true);
        }
    }

    //Same as above for now but is here for future
    private void LiveUpdateRun(Player p) throws OutOfProjectionBoundsException {
        WeatherUtils LWU = new WeatherUtils(p, plugin);
        LWU.call(true, true, false);
        p.setGameMode(GameMode.ADVENTURE);
        p.setAllowFlight(false);
        p.setWalkSpeed((float) 0.14);
    }


}
