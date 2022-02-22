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

public class LiveWeatherCommand implements CommandExecutor {
    private final LiveEarth plugin;

    public LiveWeatherCommand(LiveEarth plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender.hasPermission("liveearth.weather.toggleSelf") || sender.hasPermission("liveearth.weather.toggleOthers"))) {
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
            if (!p.hasPermission("liveearth.weather.toggleSelf")) {
                p.sendMessage(LiveEarth.prefix + ChatColor.RED + "You do not have permission to toggle your live weather");
                return true;
            }

        } else {
            if (!p.hasPermission("liveearth.weather.toggleOthers")) {
                if (!p.hasPermission("liveearth.weather.toggleSelf")) {
                    p.sendMessage(LiveEarth.prefix + ChatColor.RED + "You do not have permission to toggle your live weather");
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

                newState = plugin.getPreferencesStore().toggleWeather(user.getUniqueId());
                if (newState) {
                    p.sendMessage(LiveEarth.prefix + ChatColor.GOLD + "Live weather enabled for " + user.getName());
                    user.sendMessage(LiveEarth.prefix + ChatColor.GOLD + p.getName() + ", enabled your live weather");
                    WeatherUtils LWU = new WeatherUtils(p, plugin);
                    try {
                        LWU.call(true, false, false);
                        String szWeather = LWU.szWeather;
                        user.sendMessage(LiveEarth.prefix + ChatColor.GOLD + "Weather set to " + ChatColor.RED + szWeather);
                    } catch (OutOfProjectionBoundsException e) {
                        user.sendMessage(LiveEarth.prefix + ChatColor.RED + "weather will not be updated currently, you are outside of valid geographical bounds");
                    }

                } else {
                    p.sendMessage(LiveEarth.prefix + ChatColor.GOLD + "Live weather disabled for " + user.getName());
                    user.sendMessage(LiveEarth.prefix + ChatColor.GOLD + p.getName() + " disabled your live weather");
                    user.resetPlayerWeather();
                }
                return true;
            }
        }

        boolean newState;
        long lCurrentTime;

        newState = plugin.getPreferencesStore().toggleWeather(p.getUniqueId());
        if (newState) {
            p.sendMessage(LiveEarth.prefix + ChatColor.GOLD + "Live time enabled");
            WeatherUtils LWU = new WeatherUtils(p, plugin);
            try {
                LWU.call(true, false, false);
                String szWeather = LWU.szWeather;
                p.sendMessage(LiveEarth.prefix + ChatColor.GOLD + "Weather set to " + ChatColor.RED + szWeather);
            } catch (OutOfProjectionBoundsException e) {
                p.sendMessage(LiveEarth.prefix + ChatColor.RED + "Weather will not be updated currently, you are outside of valid geographical bounds");
            }

        } else {
            p.sendMessage(LiveEarth.prefix + ChatColor.GOLD + "Live weather disabled");
            p.resetPlayerWeather();
        }
        return true;
    }
}
