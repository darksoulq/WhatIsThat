package me.darksoul.wit.display;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.Player;

public class ActionBarDisplay extends InfoDisplay {
    public ActionBarDisplay() {
        super("actionbar");
    }

    @Override
    public void setBar(Player player, Component text) {
        if (text == null || ((TextComponent) text).content().isEmpty()) {
            player.sendActionBar(Component.text(""));
        } else {
            player.sendActionBar(text);
        }
    }

    @Override
    public void removeBar(Player player) {
        player.sendActionBar(Component.text(""));
    }

    @Override
    public boolean isEmpty(Player player) {
        return false;
    }

}
