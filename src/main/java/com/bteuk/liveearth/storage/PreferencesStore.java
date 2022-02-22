package com.bteuk.liveearth.storage;

import com.bteuk.liveearth.LiveEarth;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PreferencesStore {
    private final File configFile;
    private final YamlConfiguration config;

    public PreferencesStore(LiveEarth plugin) {
        this.configFile = new File(plugin.getDataFolder(), "preferences.yml");
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            plugin.saveResource("preferences.yml", false);
        }

        this.config = new YamlConfiguration();
        try {
            this.config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public boolean toggleWeather(UUID uuid) {
        boolean currentVal = config.getBoolean(String.format("preferences.%s.weather", uuid.toString()), false);
        config.set(String.format("preferences.%s.weather", uuid), !currentVal);
        return !currentVal;
    }

    public boolean toggleTime(UUID uuid) {
        boolean currentVal = config.getBoolean(String.format("preferences.%s.time", uuid.toString()), false);
        config.set(String.format("preferences.%s.time", uuid), !currentVal);
        return !currentVal;
    }

    public boolean getWeather(UUID uuid) {
        return config.getBoolean(String.format("preferences.%s.weather", uuid.toString()), false);
    }

    public boolean getTime(UUID uuid) {
        return config.getBoolean(String.format("preferences.%s.time", uuid.toString()), false);
    }

    public void setWeather(UUID uuid, boolean value) {
        config.set(String.format("preferences.%s.weather", uuid.toString()), value);
    }

    public void setTime(UUID uuid, boolean value) {
        config.set(String.format("preferences.%s.time", uuid.toString()), value);
    }

    public boolean toggleLive(UUID uuid) {
        boolean currentVal = config.getBoolean(String.format("preferences.%s.live", uuid.toString()), false);
        config.set(String.format("preferences.%s.live", uuid), !currentVal);
        return !currentVal;
    }

    public void setLive(UUID uuid, boolean value) {
        config.set(String.format("preferences.%s.live", uuid.toString()), value);
    }

    public boolean getLive(UUID uuid) {
        return config.getBoolean(String.format("preferences.%s.live", uuid.toString()), false);
    }

    public boolean getAnyLive(UUID uuid) {
        return getLive(uuid) || getTime(uuid) || getWeather(uuid);
    }

    public void savePreferences() throws IOException {
        config.save(configFile);
    }
}
