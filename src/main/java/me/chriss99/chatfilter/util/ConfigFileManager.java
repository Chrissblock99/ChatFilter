package me.chriss99.chatfilter.util;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class ConfigFileManager {
    private final JavaPlugin plugin;
    private final String fileName;
    private FileConfiguration config = null;
    private File configFile = null;

    public ConfigFileManager(@NotNull JavaPlugin plugin, @NotNull String fileName){
        this.plugin = plugin;
        this.fileName = fileName + ".yml";

        saveDefaultConfig();
    }

    /**
     * Reloads the config from File
     * and creates the configFile if it doesn't exist yet
     */
    public void reloadConfig() {
        if (configFile == null)
            configFile = new File(plugin.getDataFolder(), fileName);

        config = YamlConfiguration.loadConfiguration(configFile);

        InputStream defaultStream = plugin.getResource(fileName);
        if (defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            config.setDefaults(defaultConfig);
        }
    }

    /**
     * Gets the config and reloads it if not existent
     * @return The FileConfiguration
     */
    public @NotNull FileConfiguration getConfig() {
        if (config == null)
            reloadConfig();

        return config;
    }

    /**
     * Saves changes config to the File
     *
     * WARNING: this will get rid of comments in the file
     */
    public void saveConfig() {
        if (config == null || configFile == null)
            return;

        try {
            config.save(configFile);
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, e);
        }
    }

    /**
     * Creates an empty config File if not already existent
     */
    public void saveDefaultConfig() {
        if (configFile == null)
            configFile = new File(plugin.getDataFolder(), fileName);

        if (!configFile.exists()) {
           plugin.saveResource(fileName, false);
        }
    }
}
