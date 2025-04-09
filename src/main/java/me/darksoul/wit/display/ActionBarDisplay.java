package me.darksoul.wit.display;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.Player;

import java.util.Objects;

public class ActionBarDisplay extends InfoDisplay {
    public ActionBarDisplay() {
        super("actionbar");
    }

    @Override
    public void setBar(Player player, Component text) {
        player.sendActionBar(Objects.requireNonNullElseGet(text, () -> Component.text("")));
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
