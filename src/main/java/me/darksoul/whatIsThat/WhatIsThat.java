package me.darksoul.whatIsThat;

import me.darksoul.whatIsThat.commands.WITCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class WhatIsThat extends JavaPlugin {
    private static WhatIsThat instance;
    private static boolean isMTinstaled;

    @Override
    public void onEnable() {
        instance = this;
        isMTinstaled = checkMT();

        getCommand("wit").setExecutor(new WITCommand());
        getServer().getPluginManager().registerEvents(new WAILAListener(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private boolean checkMT() {
        return getServer().getPluginManager().getPlugin("Minetorio") != null;
    }

    public static boolean getIsMTInstalled() {
        return isMTinstaled;
    }

    public static WhatIsThat getInstance() {
        return instance;
    }
}
