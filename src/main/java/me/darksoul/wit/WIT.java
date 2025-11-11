package me.darksoul.wit;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import me.darksoul.wit.command.WCommands;
import me.darksoul.wit.compatibility.*;
import me.darksoul.wit.display.ActionBarDisplay;
import me.darksoul.wit.display.BossBarDisplay;
import me.darksoul.wit.display.WAILAManager;
import me.darksoul.wit.misc.Events;
import me.darksoul.wit.misc.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Logger;

public final class WIT extends JavaPlugin {
    private static WIT INSTANCE;
    public static Logger LOGGER;

    @Override
    public void onEnable() {
        INSTANCE = this;
        LOGGER = getLogger();
        setupDisplays();
        WITListener.setup();
        Handlers.setup();

        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getPluginManager().callEvent(new Information.StartTierRegistrationEvent());
            }
        }.runTaskLater(this, 10);

        if (WITListener.getConfig().getBoolean("core.bstats", true)) {
            int pluginId = 25423;
            Metrics metrics = new Metrics(INSTANCE, pluginId);
        }

        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS,
                event -> event.registrar().register(WCommands.createCommand().build(), "Main command of What is That?")
        );
        getServer().getPluginManager().registerEvents(new WITListener(), this);
        getServer().getPluginManager().registerEvents(new Events(), this);
        PlaceholderAPICompat.checkWITPAPI();
    }

    @Override
    public void onDisable() {
    }

    public static WIT instance() {
        return INSTANCE;
    }
    private static void setupDisplays() {
        WAILAManager.addDisplay(new ActionBarDisplay());
        WAILAManager.addDisplay(new BossBarDisplay());
    }
}
