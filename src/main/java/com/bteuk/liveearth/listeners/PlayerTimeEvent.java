package com.bteuk.liveearth.listeners;

import com.bteuk.liveearth.LiveEarth;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerTimeEvent implements Listener {

    private final LiveEarth plugin;

    public PlayerTimeEvent(LiveEarth plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void playerTimeCalled(PlayerCommandPreprocessEvent event) {
        if (event.getMessage().toLowerCase().startsWith(("/ptime"))) {
            //Convert sender to player
            Player p = event.getPlayer();

            if (plugin.getPreferencesStore().getTime(p.getUniqueId())) {
                plugin.getPreferencesStore().setTime(p.getUniqueId(), false);
                p.sendMessage(LiveEarth.prefix + ChatColor.GOLD + "Live time disabled");
            }
        } else if (event.getMessage().toLowerCase().startsWith(("/pweather"))) {
            //Convert sender to player
            Player p = event.getPlayer();

            if (plugin.getPreferencesStore().getWeather(p.getUniqueId())) {
                plugin.getPreferencesStore().setWeather(p.getUniqueId(), false);
                p.sendMessage(LiveEarth.prefix + ChatColor.GOLD + "Live time disabled");
            }
        }
    }

}
