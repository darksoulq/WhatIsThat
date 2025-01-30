package me.darksoul.whatIsThat.display;

import org.bukkit.entity.Player;

public abstract class InfoDisplay {
    private String id = "";
    public InfoDisplay(String id) {
        this.id = id;
    }
    public abstract void setBar(Player player, String text);
    public abstract void removeBar(Player player);

    public String getId() {
        return id;
    }
}
