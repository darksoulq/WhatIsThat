package me.darksoul.whatIsThat;

import me.darksoul.whatIsThat.commands.WITCommand;
import me.darksoul.whatIsThat.compatibility.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class WhatIsThat extends JavaPlugin {
    private static WhatIsThat instance;

    @Override
    public void onEnable() {
        instance = this;
        WAILAListener.setup();
        MinetorioCompat.hook();
        ItemsAdderCompat.hook();
        EliteMobsCompat.hook();
        AuraSkillsCompat.hook();
        AuraMobsCompat.hook();
        ValhallaMMOCompat.hookRaces();
        ValhallaMMOCompat.hook();
        SlimefunCompat.hook();
        LiteFarmCompat.hook();
        NexoCompat.hook();

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
    public static void resetHooks() {
        if (AuraMobsCompat.getIsInstalled() && WAILAListener.getConfig().getBoolean("auramobs.enabled", false)) {
            AuraMobsCompat.setup();
        }
        if (AuraSkillsCompat.getIsInstalled() && WAILAListener.getConfig().getBoolean("auraskills.enabled", true)) {
            AuraSkillsCompat.setup();
        }
        if (EliteMobsCompat.getIsInstalled() && WAILAListener.getConfig().getBoolean("elitemobs.enabled", true)) {
            EliteMobsCompat.setup();
        }
        if (ItemsAdderCompat.getIsInstalled()) {
            ItemsAdderCompat.setup();
        }
        MinecraftCompat.setup();
        if (MinetorioCompat.getIsInstalled() && WAILAListener.getConfig().getBoolean("minetorio.enabled", true)) {
            MinetorioCompat.setup();
        }
        if (SlimefunCompat.getIsInstalled() && WAILAListener.getConfig().getBoolean("slimefun.enabled", true)) {
            SlimefunCompat.setup();
        }
        if (ValhallaMMOCompat.getIsInstalled()) {
            ValhallaMMOCompat.setup();
        }
    }
}
