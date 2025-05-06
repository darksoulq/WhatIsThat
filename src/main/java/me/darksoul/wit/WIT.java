package me.darksoul.wit;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import me.darksoul.wit.command.WCommands;
import me.darksoul.wit.compatibility.*;
import me.darksoul.wit.display.ActionBarDisplay;
import me.darksoul.wit.display.BossBarDisplay;
import me.darksoul.wit.display.WAILAManager;
import me.darksoul.wit.misc.Events;
import me.darksoul.wit.misc.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

public final class WIT extends JavaPlugin {
    private static WIT INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;
        setupDisplays();
        WITListener.setup();
        Handlers.setup();

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
