package me.darksoul.whatIsThat.misc;

import me.darksoul.whatIsThat.WhatIsThat;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class LanguageUtils {
    private static final File LANG_FOLDER = new File(WhatIsThat.getInstance().getDataFolder(), "lang");

    private static final File VANILLA_BLOCKS_FILE = new File(LANG_FOLDER, "lang_vanilla.yml");

    private static File mtLangFile;

    static {
        if (!LANG_FOLDER.exists()) {
            LANG_FOLDER.mkdirs();
        }

        copyTemplate("lang_vanilla.yml", VANILLA_BLOCKS_FILE);

        if (WhatIsThat.getIsMTInstalled()) {
            mtLangFile = new File(LANG_FOLDER, "lang_mt.yml");
            copyTemplate("lang_mt.yml", mtLangFile);
        }
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
     * Loads the vanilla blocks language file into a YamlConfiguration object
     *
     * @return YamlConfiguration for the vanilla blocks language file
     */
    public static YamlConfiguration loadVanillaBlocksLang() {
        return YamlConfiguration.loadConfiguration(VANILLA_BLOCKS_FILE);
    }

    /**
     * Loads the MT language file if MT is installed
     *
     * @return YamlConfiguration for the MT language file, or null if MT is not installed
     */
    public static YamlConfiguration loadMTLang() {
        return mtLangFile != null ? YamlConfiguration.loadConfiguration(mtLangFile) : null;
    }

    /**
     * Saves the MT language file with the provided configuration
     *
     * @param config The YamlConfiguration to save
     * @throws IOException if an error occurs during saving
     */
    public static void saveMTLang(YamlConfiguration config) throws IOException {
        if (mtLangFile != null) {
            config.save(mtLangFile);
        }
    }

}