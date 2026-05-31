package com.github.darksoulq.wit.display;

import com.github.darksoulq.wit.api.Info;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;

public abstract class InfoDisplay {
    private String id = "";
    public InfoDisplay(String id) {
        this.id = id;
    }
    public abstract void setBar(Player player, Info text);
    public void setProgress(Player player, float value) {}
    public void setColor(Player player, TextColor color) {}
    public abstract void removeBar(Player player);
    public abstract boolean isEmpty(Player player);

    public String getId() {
        return id;
    }
}
