package com.bteuk.liveearth.listeners;

import com.bteuk.liveearth.LiveEarth;
import com.bteuk.liveearth.UpdateCall;
import net.buildtheearth.terraminusminus.projection.OutOfProjectionBoundsException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinEvent implements Listener {
    private final LiveEarth plugin;

    public JoinEvent(LiveEarth plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent event) {
        plugin.getLogger().info("Running update call!");
        UpdateCall up = new UpdateCall(plugin, event);
        Bukkit.getScheduler().runTask(plugin, () -> {
            try {
                up.initialRun();
            } catch (OutOfProjectionBoundsException e) {
                event.getPlayer().sendMessage(LiveEarth.prefix + ChatColor.GREEN + "You are not in a geographical location, cancelling initial run!");
            }
        });

    }

    @EventHandler
    public void playerLeave(PlayerQuitEvent event) {
        Player p = event.getPlayer();

        //Gets the list of players with live
        if (plugin.getPreferencesStore().getLive(p.getUniqueId())) {
            plugin.getPreferencesStore().setLive(p.getUniqueId(), false);
        }
    }
}

