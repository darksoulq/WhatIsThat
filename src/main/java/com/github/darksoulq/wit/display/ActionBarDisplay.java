package com.github.darksoulq.wit.display;

import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class ActionBarDisplay extends InfoDisplay {
    public ActionBarDisplay() {
        super("actionbar");
    }

    @Override
    public void setBar(Player player, Component text) {
        if (text == null) text = Component.empty();
        ((CraftPlayer) player).getHandle().connection.send(new ClientboundSetActionBarTextPacket(PaperAdventure.asVanilla(text)));
    }

    @Override
    public void setProgress(Player player, float value) {}

    @Override
    public void removeBar(Player player) {
        ((CraftPlayer) player).getHandle().connection.send(new ClientboundSetActionBarTextPacket(PaperAdventure.asVanilla(Component.empty())));
    }

    @Override
    public boolean isEmpty(Player player) {
        return false;
    }
}