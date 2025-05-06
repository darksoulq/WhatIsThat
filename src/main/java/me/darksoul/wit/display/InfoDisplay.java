package me.darksoul.wit.display;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public abstract class InfoDisplay {
    private String id = "";
    public InfoDisplay(String id) {
        this.id = id;
    }
    public abstract void setBar(Player player, Component text);
    public void setProgress(Player player, float value) {}
    public abstract void removeBar(Player player);
    public abstract boolean isEmpty(Player player);

    public String getId() {
        return id;
    }
}
