package com.bteuk.liveearth.listeners;

import com.bteuk.liveearth.LiveEarth;
import com.bteuk.liveearth.UpdateCall;
import net.buildtheearth.terraminusminus.projection.OutOfProjectionBoundsException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class TeleportEvent implements Listener {
    private final LiveEarth plugin;

    public TeleportEvent(LiveEarth plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void teleportEvent(PlayerTeleportEvent event) {
        //Convert sender to player
        Player player = event.getPlayer();

        if (event.getCause() == TeleportCause.COMMAND || event.getCause() == TeleportCause.PLUGIN) {
            double FromX = event.getFrom().getX();
            double FromZ = event.getFrom().getZ();
            double ToX = event.getTo().getX();
            double ToZ = event.getTo().getZ();

            //Checks whether it is outside a 2km radius
            if (Math.sqrt((FromX - ToX) * (FromX - ToX) + (FromZ - ToZ) * (FromZ - ToZ)) > 2000) {
                UpdateCall up = new UpdateCall(plugin, player);
                try {
                    up.locationMoveRun(event.getTo());
                } catch (OutOfProjectionBoundsException ignored) {
                }
            }
        }
    }
}
