package me.darksoul.wit.misc;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.darksoul.wit.WIT;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class ConfigUtils {
    private static final File LANG_FOLDER = new File(WIT.getInstance().getDataFolder(), "lang");
    private static final File VALUES_FILE = new File(LANG_FOLDER, "values.yml");
    private static final File CONFIG_FILE = new File(WIT.getInstance().getDataFolder(), "config.yml");

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
            try (InputStream resourceStream = WIT.getInstance().getResource(resourceName)) {
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
        return YamlConfiguration.loadConfiguration(CONFIG_FILE);
    }
    public static YamlConfiguration loadValuesFIle() {
        return YamlConfiguration.loadConfiguration(VALUES_FILE);
    }
}