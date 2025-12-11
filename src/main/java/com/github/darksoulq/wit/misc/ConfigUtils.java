package com.github.darksoulq.wit.misc;

import com.github.darksoulq.wit.WIT;
import com.github.darksoulq.wit.WITListener;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ConfigUtils {
    private static final File LANG_FOLDER = new File(WIT.instance().getDataFolder(), "lang");
    private static final File VALUES_FILE = new File(LANG_FOLDER, "values.yml");
    private static final File CONFIG_FILE = new File(WIT.instance().getDataFolder(), "config.yml");

    static {
        if (!LANG_FOLDER.exists()) {
            LANG_FOLDER.mkdirs();
        }
        copyTemplate("config.yml", CONFIG_FILE);
        copyTemplate("values.yml", VALUES_FILE);
    }
    /**
     * Copies a template file from the plugin's resources to disk if it doesn't exist already.
     *
     * @param resourceName The name of the resource template file.
     * @param destination  The destination file on disk.
     */
    private static void copyTemplate(String resourceName, File destination) {
        if (!destination.exists()) {
            try (InputStream resourceStream = WIT.instance().getResource(resourceName)) {
                if (resourceStream != null) {
                    Files.copy(resourceStream, destination.toPath());
                } else {
                    destination.createNewFile();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Loads the config file into a YamlConfiguration object
     *
     * @return YamlConfiguration for the config file
     */
    public static YamlConfiguration loadConfig() {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(CONFIG_FILE);
        WITListener.DISABLED_WORLDS.clear();
        config.getStringList("world-blacklist").forEach(wn -> {
            World world = Bukkit.getWorld(wn);
            if (world == null) return;
            WITListener.DISABLED_WORLDS.add(world);
        });
        return config;
    }
    public static YamlConfiguration loadValuesFIle() {
        return YamlConfiguration.loadConfiguration(VALUES_FILE);
    }
    public static String toProperCase(String enumName) {
        return Arrays.stream(enumName.split("_"))
                .map(s -> s.substring(0,1).toUpperCase() + s.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }
}