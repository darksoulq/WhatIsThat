package me.darksoul.whatIsThat;

import me.darksoul.whatIsThat.commands.WITCommand;
import me.darksoul.whatIsThat.compatibility.ItemsAdderCompat;
import me.darksoul.whatIsThat.compatibility.MinetorioCompat;
import org.bukkit.plugin.java.JavaPlugin;

public final class WhatIsThat extends JavaPlugin {
    private static WhatIsThat instance;

    @Override
    public void onEnable() {
        instance = this;
        MinetorioCompat.setIsMTInstalled(MinetorioCompat.checkMT());
        ItemsAdderCompat.setIsIAInstalled(ItemsAdderCompat.checkIA());

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
