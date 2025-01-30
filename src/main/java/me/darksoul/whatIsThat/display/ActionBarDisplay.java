package me.darksoul.whatIsThat.display;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class ActionBarDisplay extends InfoDisplay {
    public ActionBarDisplay() {
        super("actionbar");
    }

    @Override
    public void setBar(Player player, String text) {
        if (text == null || text.isEmpty()) {
            player.sendActionBar(Component.text(""));
        } else {
            player.sendActionBar(Component.text(text));
        }
    }

    @Override
    public void removeBar(Player player) {
        player.sendActionBar(Component.text(""));
    }

}
