package me.darksoul.whatIsThat;

import me.darksoul.whatIsThat.commands.WITCommand;
import me.darksoul.whatIsThat.compatibility.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class WhatIsThat extends JavaPlugin {
    private static WhatIsThat instance;

    @Override
    public void onEnable() {
        instance = this;
        MinetorioCompat.checkMT();
        ItemsAdderCompat.checkIA();
        EliteMobsCompat.checkEM();
        AuraSkillsCompat.checkAuraSkills();
        AuraMobsCompat.checkAuraMobs();
        ValhallaMMOCompat.checkVRaces();
        ValhallaMMOCompat.checkVMMO();
        SlimefunCompat.checkSlimefun();
        LiteFarmCompat.checkLitefarm();
        NexoCompat.checkNexo();

        Handlers.setup();

        getCommand("wit").setExecutor(new WITCommand());
        getServer().getPluginManager().registerEvents(new WAILAListener(), this);
        if (WITPAPI.checkWITPAPI()) {
            new WITPAPI().register();
        }
    }

    @Override
    public void onDisable() {
    }

    public static WhatIsThat getInstance() {
        return instance;
    }
}
