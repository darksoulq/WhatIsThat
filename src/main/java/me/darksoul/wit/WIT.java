package me.darksoul.wit;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import me.darksoul.wit.api.API;
import me.darksoul.wit.command.WCommands;
import me.darksoul.wit.compatibility.*;
import me.darksoul.wit.display.ActionBarDisplay;
import me.darksoul.wit.display.BossBarDisplay;
import me.darksoul.wit.display.WAILAManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class WIT extends JavaPlugin implements Listener {
    private static WIT instance;

    @Override
    public void onEnable() {
        instance = this;
        setupDisplays();
        WITListener.setup();
        Handlers.setup();

        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS,
                event -> event.registrar().register(WCommands.createCommand().build(), "Main command of What is That?")
        );
        getServer().getPluginManager().registerEvents(new WITListener(), this);
        getServer().getPluginManager().registerEvents(this, this);
        PlaceholderAPICompat.checkWITPAPI();
    }

    @Override
    public void onDisable() {
    }

    public static WIT getInstance() {
        return instance;
    }
    private static void setupDisplays() {
        WAILAManager.addDisplay(new ActionBarDisplay());
        WAILAManager.addDisplay(new BossBarDisplay());
    }

    @EventHandler
    public void onServerLoad(ServerLoadEvent e) {
        API.fireReload();
    }
}
