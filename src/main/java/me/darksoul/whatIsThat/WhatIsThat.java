package me.darksoul.whatIsThat;

import me.darksoul.whatIsThat.commands.WITCommand;
import me.darksoul.whatIsThat.compatibility.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class WhatIsThat extends JavaPlugin {
    private static WhatIsThat instance;

    @Override
    public void onEnable() {
        instance = this;
        MinetorioCompat.setIsMTInstalled(MinetorioCompat.checkMT());
        ItemsAdderCompat.setIsIAInstalled(ItemsAdderCompat.checkIA());
        EliteMobsCompat.setEMInstalled(EliteMobsCompat.checkEM());
        AuraSkillsCompat.setIsAuraSkillsInstalled(AuraSkillsCompat.checkAuraSkills());
        AuraMobsCompat.setIsAuraMobsInstalled(AuraMobsCompat.checkAuraMobs());
        ValhallaMMOCompat.setIsVMMOInstalled(ValhallaMMOCompat.checkVMMO());

        getCommand("wit").setExecutor(new WITCommand());
        getServer().getPluginManager().registerEvents(new WAILAListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static WhatIsThat getInstance() {
        return instance;
    }
}
