package com.github.darksoulq.wit;

import com.github.darksoulq.wit.compatibility.PlaceholderAPICompat;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import com.github.darksoulq.wit.command.WCommands;
import com.github.darksoulq.wit.display.ActionBarDisplay;
import com.github.darksoulq.wit.display.BossBarDisplay;
import com.github.darksoulq.wit.display.DisplayManager;
import com.github.darksoulq.wit.misc.Events;
import com.github.darksoulq.wit.misc.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
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

        for (Player player : Bukkit.getOnlinePlayers()) {
            WITListener.loadSettings(player, () -> {
                if (!WITListener.getSettings(player).disabled && !WITListener.DISABLED_WORLDS.contains(player.getWorld().getName())) {
                    WITListener.addPlayer(player);
                }
            });
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getPluginManager().callEvent(new Information.StartTierRegistrationEvent());
            }
        }.runTaskLater(this, 10);

        if (WITListener.getConfig().getBoolean("core.bstats", true)) {
            new Metrics(INSTANCE, 25423);
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
        for (Player player : Bukkit.getOnlinePlayers()) {
            DisplayManager.removeBar(player, WITListener.getSettings(player).type);
        }
    }

    public static WIT instance() {
        return INSTANCE;
    }

    private static void setupDisplays() {
        DisplayManager.addDisplay(new ActionBarDisplay());
        DisplayManager.addDisplay(new BossBarDisplay());
    }
}