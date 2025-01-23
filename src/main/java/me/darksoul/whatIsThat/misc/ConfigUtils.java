package me.darksoul.whatIsThat.misc;

import com.google.gson.*;
import me.darksoul.whatIsThat.WhatIsThat;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.file.Files;

public class ConfigUtils {
    private static final File LANG_FOLDER = new File(WhatIsThat.getInstance().getDataFolder(), "lang");

    private static final File VANILLA_FILE_EN = new File(LANG_FOLDER, "vanilla_en_us.json");
    private static final File VALUES_FILE = new File(LANG_FOLDER, "values.yml");
    private static final File CONFIG_FILE = new File(WhatIsThat.getInstance().getDataFolder(), "config.yml");

    private static JsonObject vTranslations;

    static {
        if (!LANG_FOLDER.exists()) {
            LANG_FOLDER.mkdirs();
        }
        copyTemplate("config.yml", CONFIG_FILE);
        copyTemplate("values.yml", VALUES_FILE);
        copyTemplate("vanilla_en_us.json", VANILLA_FILE_EN);
        loadVanillaTranslation();
    }
    /**
     * Copies a template file from the plugin's resources to disk if it doesn't exist already.
     *
     * @param resourceName The name of the resource template file.
     * @param destination  The destination file on disk.
     */
    private static void copyTemplate(String resourceName, File destination) {
        if (!destination.exists()) {
            try (InputStream resourceStream = WhatIsThat.getInstance().getResource(resourceName)) {
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

    public static void loadVanillaTranslation() {
        Bukkit.getScheduler().runTaskAsynchronously(WhatIsThat.getInstance(), ()->{
            Gson gson = new Gson();
            try {
                vTranslations = gson.fromJson(new FileReader( new File(LANG_FOLDER, "vanilla_" +
                        YamlConfiguration.loadConfiguration(CONFIG_FILE)
                                .getString("core.vanilla_lang", "en_us") + ".json")), JsonObject.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }


    /**
     * Gets the translated value for a given key from the vanilla translations.
     *
     * @param key The translation key.
     * @return The translated value, or the key if not found.
     */
    public static String getTranslatedName(String key) {
        JsonElement element = vTranslations.get(key);
        return (element != null && element.isJsonPrimitive()) ? element.getAsString() : key;
    }
}