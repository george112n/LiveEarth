package com.bteuk.liveearth.commands;

import com.bteuk.liveearth.LiveEarth;
import com.bteuk.liveearth.weather.WeatherUtils;
import net.buildtheearth.terraminusminus.projection.OutOfProjectionBoundsException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author 14walkerg
 * @date 3 Jan 2021
 * @time 14:08:09
 */

public class LiveTimeCommand implements CommandExecutor {

    private final LiveEarth plugin;

    public LiveTimeCommand(LiveEarth plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender.hasPermission("liveearth.time.toggleSelf") || sender.hasPermission("liveearth.time.toggleOthers"))) {
            sender.sendMessage(LiveEarth.prefix + ChatColor.RED + "You do not have permission to use this command");
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(LiveEarth.prefix + ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        //Convert sender to player
        Player p = (Player) sender;

        if (args.length == 0) {
            if (!p.hasPermission("liveearth.time.toggleSelf")) {
                p.sendMessage(LiveEarth.prefix + ChatColor.RED + "You do not have permission to toggle your live time");
                return true;
            }

        } else {
            if (!p.hasPermission("liveearth.time.toggleOthers")) {
                if (!p.hasPermission("liveearth.time.toggleSelf")) {
                    p.sendMessage(LiveEarth.prefix + ChatColor.RED + "You do not have permission to toggle your live time");
                    return true;
                }
            } else {

                Player user = Bukkit.getPlayer(args[0]);


                if (user == null) {
                    p.sendMessage(LiveEarth.prefix + ChatColor.RED + args[0] + " is not online!");
                    return true;
                }

                boolean newState;
                long lCurrentTime;

                newState = plugin.getPreferencesStore().toggleTime(user.getUniqueId());
                if (newState) {
                    p.sendMessage(LiveEarth.prefix + ChatColor.GOLD + "Live time enabled for " + user.getName());
                    user.sendMessage(LiveEarth.prefix + ChatColor.GOLD + p.getName() + " enabled your live time");
                    WeatherUtils LWU = new WeatherUtils(p, plugin);
                    try {
                        LWU.call(false, true, false);
                        lCurrentTime = LWU.lTime;
                        String szTime = String.format("%02d:%02d", lCurrentTime / 100, lCurrentTime % 100);
                        user.sendMessage(LiveEarth.prefix + ChatColor.GOLD + "Time set to " + ChatColor.RED + szTime);
                    } catch (OutOfProjectionBoundsException e) {
                        user.sendMessage(LiveEarth.prefix + ChatColor.RED + "Time will not be updated currently, you are outside of valid geographical bounds");
                    }

                } else {
                    p.sendMessage(LiveEarth.prefix + ChatColor.GOLD + "Live time disabled for " + user.getName());
                    user.sendMessage(LiveEarth.prefix + ChatColor.GOLD + p.getName() + " disabled your live time");
                    user.resetPlayerTime();
                }
                return true;
            }
        }

        boolean newState;
        long lCurrentTime;

        newState = plugin.getPreferencesStore().toggleTime(p.getUniqueId());
        if (newState) {
            p.sendMessage(LiveEarth.prefix + ChatColor.GOLD + "Live time enabled");
            WeatherUtils LWU = new WeatherUtils(p, plugin);
            try {
                LWU.call(false, true, false);
                lCurrentTime = LWU.lTime;
                String szTime = String.format("%02d:%02d", lCurrentTime / 100, lCurrentTime % 100);
                p.sendMessage(LiveEarth.prefix + ChatColor.GOLD + "Time set to " + ChatColor.RED + szTime);
            } catch (OutOfProjectionBoundsException e) {
                p.sendMessage(LiveEarth.prefix + ChatColor.RED + "Time will not be updated currently, you are outside of valid geographical bounds");
            }

        } else {
            p.sendMessage(LiveEarth.prefix + ChatColor.GOLD + "Live time disabled");
            p.resetPlayerTime();
        }
        return true;
    }
}
