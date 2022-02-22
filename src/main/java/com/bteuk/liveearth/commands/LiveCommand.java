package com.bteuk.liveearth.commands;

import com.bteuk.liveearth.LiveEarth;
import com.bteuk.liveearth.UpdateCall;
import net.buildtheearth.terraminusminus.projection.OutOfProjectionBoundsException;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LiveCommand implements CommandExecutor {

    private final LiveEarth plugin;

    public LiveCommand(LiveEarth plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender.hasPermission("liveearth.live.toggleSelf") || sender.hasPermission("liveearth.live.toggleOthers"))) {
            sender.sendMessage(LiveEarth.prefix + ChatColor.RED + "You do not have permission to use this command");
        }
        //Check is command sender is a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(LiveEarth.prefix + ChatColor.RED + "You cannot add a player to a region!");
            return true;
        }

        //Convert sender to player
        Player p = (Player) sender;

        //Gets the list of players with live

        if (plugin.getPreferencesStore().getLive(p.getUniqueId())) {
            plugin.getPreferencesStore().setLive(p.getUniqueId(), false);
            p.resetPlayerWeather();
            p.resetPlayerTime();
            p.setGameMode(GameMode.CREATIVE);
            p.setWalkSpeed((float) 0.2);
            p.kickPlayer(LiveEarth.prefix + ChatColor.RED + "" + ChatColor.BOLD + "Please rejoin to disable Live Mode!");
            return true;
        }

        //Only reaches this if the player isn't in the /live list
        //Adds player to the live list
        plugin.getPreferencesStore().setLive(p.getUniqueId(), true);
        try {
            UpdateCall.InitialLiveRun(p, plugin);
        } catch (OutOfProjectionBoundsException e) {
            sender.sendMessage(LiveEarth.prefix + ChatColor.RED + "You are outside of a valid area! Please enter a valid area and live mode will enable!");
        }
        return true;
    }
}
