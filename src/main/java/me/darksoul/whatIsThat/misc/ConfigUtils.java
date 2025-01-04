package me.darksoul.whatIsThat.misc;

import com.google.gson.*;
import me.darksoul.whatIsThat.WhatIsThat;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.file.Files;

public class ConfigUtils {
    private static final File LANG_FOLDER = new File(WhatIsThat.getInstance().getDataFolder(), "lang");

    private static final File VANILLA_FILE_EN = new File(LANG_FOLDER, "vanilla_en_us.json");
    private static final File CONFIG_FILE = new File(WhatIsThat.getInstance().getDataFolder(), "config.yml");

    private static JsonObject vTranslations;

    static {
        if (!LANG_FOLDER.exists()) {
            LANG_FOLDER.mkdirs();
        }
        copyTemplate("config.yml", CONFIG_FILE);
        copyTemplateJSON("vanilla_en_us.json", VANILLA_FILE_EN);
        loadVTranslation();
    }
    /**
     * Helper method to copy a template file from the plugin's resources to the disk
     *
     * @param resourceName The name of the resource template file
     * @param destination  The destination file on disk
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
     * Helper method to copy a template JSON file from resources to the disk
     *
     * @param templateName The name of the resource template file
     * @param targetFile   The destination file on disk
     */
    public static void copyTemplateJSON(String templateName, File targetFile) {
        if (!targetFile.exists()) {
            try (InputStream inputStream = WhatIsThat.getInstance().getResource(templateName)) {
                if (inputStream != null) {
                    Files.copy(inputStream, targetFile.toPath());
                } else {
                    targetFile.createNewFile();
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

    private static void loadVTranslation() {
        Gson gson = new Gson();
        try {
            vTranslations = gson.fromJson(new FileReader( new File(LANG_FOLDER, "vanilla_" +
                    YamlConfiguration.loadConfiguration(CONFIG_FILE)
                            .getString("core.lang_file", "en_us") + ".json")), JsonObject.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getTranslatedVName(String key) {
        String name = key;
        JsonElement keyy = vTranslations.get(key);
        if (keyy != null) {
            name = keyy.getAsString();
        }
        return name;
    }
}