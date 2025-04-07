package me.darksoul.wit;

import me.darksoul.wit.command.Commands;
import me.darksoul.wit.compatibility.*;
import me.darksoul.wit.display.ActionBarDisplay;
import me.darksoul.wit.display.BossBarDisplay;
import me.darksoul.wit.display.WAILAManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class WIT extends JavaPlugin {
    private static WIT instance;

    @Override
    public void onEnable() {
        instance = this;
        setupDisplays();
        WITListener.setup();
        Handlers.setup();

//        getCommand("wit").setExecutor(new Commands());
        getServer().getPluginManager().registerEvents(new WITListener(), this);
        PlaceholderAPICompat.checkWITPAPI();
    }

    @Override
    public void onDisable() {
    }

    public static WIT getInstance() {
        return instance;
    }
    public static void resetHooks() {
    }
    private static void setupDisplays() {
        WAILAManager.addDisplay(new ActionBarDisplay());
        WAILAManager.addDisplay(new BossBarDisplay());
    }
}
