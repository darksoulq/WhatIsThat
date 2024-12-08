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
    private static boolean isAuraSkillsInstalled;
    private static AuraSkillsApi skillsApi;

    private static void setup() {
        skillsApi = AuraSkillsApi.get();
        if (WAILAListener.getConfig().getBoolean("auraskills.enabled", true)) {
            if (WAILAListener.getConfig().getBoolean("auraskills.powerlevelinfo", true)) {
                Information.getPrefixVEntity().add(AuraSkillsCompat::getPowerLevel);
            }
        }
    }
    public static boolean checkAuraSkills() {
        Plugin pl = WhatIsThat.getInstance().getServer().getPluginManager().getPlugin("AuraSkills");
        boolean isEnabled = pl != null && pl.isEnabled();
        if (isEnabled) {
            setup();
        }
        return isEnabled;
    }
    public static void setIsAuraSkillsInstalled(boolean isAuraSkillsInstalled) {
        AuraSkillsCompat.isAuraSkillsInstalled = isAuraSkillsInstalled;
    }

    private static String getPowerLevel(Entity entity) {
        if (entity instanceof Player player) {
            SkillsUser user = skillsApi.getUser(player.getUniqueId());
            int level = user.getPowerLevel();
            return "§c💪 " + level + " ";
        }
        return "";
    }
}
