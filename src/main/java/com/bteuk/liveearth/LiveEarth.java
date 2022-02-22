package com.bteuk.liveearth;

import com.bteuk.liveearth.commands.LiveCommand;
import com.bteuk.liveearth.commands.LiveTimeCommand;
import com.bteuk.liveearth.commands.LiveWeatherCommand;
import com.bteuk.liveearth.listeners.JoinEvent;
import com.bteuk.liveearth.listeners.PlayerTimeEvent;
import com.bteuk.liveearth.listeners.TeleportEvent;
import com.bteuk.liveearth.storage.PreferencesStore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class LiveEarth extends JavaPlugin {
    public static String prefix = ChatColor.GREEN + "" + ChatColor.BOLD + "LiveEarth >> " + ChatColor.RESET;
    private static PreferencesStore preferencesStore;
    public FileConfiguration config;

    @Override
    public void onEnable() {
        //Config
        this.config = this.getConfig();
        saveDefaultConfig();

        preferencesStore = new PreferencesStore(this);

        //Listeners
        PlayerTimeEvent playerTimeEvent = new PlayerTimeEvent(this);
        Bukkit.getPluginManager().registerEvents(playerTimeEvent, this);

        JoinEvent joinEvent = new JoinEvent(this);
        Bukkit.getPluginManager().registerEvents(joinEvent, this);

        TeleportEvent teleportEvent = new TeleportEvent(this);
        Bukkit.getPluginManager().registerEvents(teleportEvent, this);

        this.getLogger().info("Event listeners Loaded!");

        //Commands
        getCommand("liveweather").setExecutor(new LiveWeatherCommand(this));
        getCommand("livetime").setExecutor(new LiveTimeCommand(this));
        getCommand("live").setExecutor(new LiveCommand(this));

        this.getLogger().info("Commands Loaded!");

        int minute = (int) 1200L;

        this.getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
            UpdateCall up = new UpdateCall(this);
            up.run();
        }, 0L, (long) minute * config.getInt("timerInterval"));
    }

    @Override
    public void onDisable() {
        try {
            preferencesStore.savePreferences();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PreferencesStore getPreferencesStore() {
        return preferencesStore;
    }


    public long updateTimeSeasonal(Player player, LocalDateTime sunrise, LocalDateTime sunset) {
        long lSeasonalTime;
        long lSeasonalNightTime;
        long militaryTime;
        final float mcSunLight = 14076F;
        final float mcSunRise = 22967F;
        float fractionOfDaylightComplete;
        float fractionOfNightlightComplete;
        float fSunset = sunset.getHour() * 60 + sunset.getMinute();
        float fSunrise = sunrise.getHour() * 60 + sunrise.getMinute();

        //Daylight in minutes
        float fDaylight = 60 * (sunset.getHour() - sunrise.getHour()) + sunset.getMinute() - sunrise.getMinute();

        float fMinutesAfterSunrise = ((LocalTime.now().getHour() - sunrise.getHour()) * 60 + (LocalTime.now().getMinute()) - sunrise.getMinute());

        //Work out the minecraft ticks count
        fractionOfDaylightComplete = fMinutesAfterSunrise / fDaylight;
        lSeasonalTime = (long) (mcSunRise + fractionOfDaylightComplete * mcSunLight);

        if (fMinutesAfterSunrise < 0) //Sun not risen
        {
            if (lSeasonalTime < 22000) {
                float fMinutesOfNightAlg = fSunrise - 937 * (fDaylight / mcSunLight);
                fractionOfNightlightComplete = ((float) (LocalTime.now().getHour() * 60 + LocalTime.now().getMinute()) / (fMinutesOfNightAlg));

                lSeasonalNightTime = (long) (18000 + fractionOfNightlightComplete * 4000);
                lSeasonalTime = lSeasonalNightTime;
            }
        } else //Sun has set but is before midnight
        {
            lSeasonalTime = lSeasonalTime - 24000;

            //If the seasonal time goes over 14000, use night algorithm
            if (!(lSeasonalTime < 14000)) {
                //Minutes of the day that the night algorithm begins
                float fMinutesOfNightAlg = fSunset + 957 * (fDaylight / mcSunLight);

                //Fration of night alg to midnight that is done
                fractionOfNightlightComplete = ((float) (LocalTime.now().getHour() * 60 + LocalTime.now().getMinute()) - (fMinutesOfNightAlg)) / (1440 - (fMinutesOfNightAlg));

                //Ticks
                lSeasonalNightTime = (long) (14000 + fractionOfNightlightComplete * 4000);
                lSeasonalTime = lSeasonalNightTime;
            }
        }

        lSeasonalTime = lSeasonalTime % 24000;

        player.setPlayerTime(lSeasonalTime, false);

        militaryTime = (LocalTime.now().getHour() + config.getLong("HourOffset")) * 100 + LocalTime.now().getMinute();
        return militaryTime;
    }

}
