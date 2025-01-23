package me.darksoul.whatIsThat.compatibility;

import dev.aurelium.auraskills.api.AuraSkillsApi;
import dev.aurelium.auraskills.api.user.SkillsUser;
import me.darksoul.whatIsThat.Information;
import me.darksoul.whatIsThat.WAILAListener;
import me.darksoul.whatIsThat.WhatIsThat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class AuraSkillsCompat {
    private static boolean isInstalled;
    private static AuraSkillsApi skillsApi;

    public static void setup() {
        if (WAILAListener.getConfig().getBoolean("auraskills.enabled", true)) {
            if (WAILAListener.getConfig().getBoolean("auraskills.powerlevelinfo", true)) {
                MinecraftCompat.getPrefixVEntity().add(Information::auraSkills_getPowerLevel);
            }
        }
    }
    public static void hook() {
        Plugin pl = WhatIsThat.getInstance().getServer().getPluginManager().getPlugin("AuraSkills");
        isInstalled = pl != null && pl.isEnabled();
        if (isInstalled) {
            skillsApi = AuraSkillsApi.get();
            setup();
            WhatIsThat.getInstance().getLogger().info("Hooked into AuraSkills");
        } else {
            WhatIsThat.getInstance().getLogger().info("AuraSkills not found, skipping hook");
        }
    }
    public static boolean getIsInstalled() {
        return isInstalled;
    }

    public static AuraSkillsApi getSkillsApi() {
        return skillsApi;
    }
}
