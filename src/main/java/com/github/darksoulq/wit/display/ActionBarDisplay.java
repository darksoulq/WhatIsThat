package com.github.darksoulq.wit.display;

import com.github.darksoulq.wit.WITListener;
import com.github.darksoulq.wit.api.Info;
import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ActionBarDisplay extends InfoDisplay {
    private final Map<UUID, Component> currentTextMap = new ConcurrentHashMap<>();

    public ActionBarDisplay() {
        super("actionbar");
    }

    private void sendPacket(Player player, Component text) {
        ((CraftPlayer) player).getHandle().connection.send(new ClientboundSetActionBarTextPacket(PaperAdventure.asVanilla(text)));
    }

    @Override
    public void setBar(Player player, Info info) {
        Component text = info != null ? info.getCombined() : null;
        if (text == null) text = Component.empty();
        currentTextMap.put(player.getUniqueId(), text);
        sendPacket(player, text);
    }

    @Override
    public void setProgress(Player player, float value) {
        Component text = currentTextMap.getOrDefault(player.getUniqueId(), Component.empty());
        WITListener.PlayerSettings settings = WITListener.getSettings(player);

        if (settings.abProgressMode == WITListener.ActionBarProgressMode.OFF) {
            sendPacket(player, text);
            return;
        }

        Component suffix = Component.empty();
        if (settings.abProgressMode == WITListener.ActionBarProgressMode.BAR) {
            int bars = 10;
            int filled = Math.round(bars * value);

            suffix = Component.text(" [")
                .color(NamedTextColor.DARK_GRAY)
                .append(Component.text("|".repeat(Math.max(0, filled))).color(NamedTextColor.GREEN))
                .append(Component.text("|".repeat(Math.max(0, bars - filled))).color(NamedTextColor.GRAY))
                .append(Component.text("]"));

        } else if (settings.abProgressMode == WITListener.ActionBarProgressMode.PERCENT) {
            suffix = Component.text(" (" + Math.round(value * 100) + "%)")
                .color(NamedTextColor.YELLOW);

        } else if (settings.abProgressMode == WITListener.ActionBarProgressMode.UNDERLINE) {
            int length = 20;
            int filled = Math.round(length * value);

            suffix = Component.text("  ")
                .append(Component.text(" ".repeat(Math.max(0, filled)))
                    .decorate(TextDecoration.UNDERLINED)
                    .color(NamedTextColor.GREEN))
                .append(Component.text(" ".repeat(Math.max(0, length - filled)))
                    .decorate(TextDecoration.UNDERLINED)
                    .color(NamedTextColor.GRAY));
        }

        sendPacket(player, text.append(suffix));
    }

    @Override
    public void removeBar(Player player) {
        currentTextMap.remove(player.getUniqueId());
        sendPacket(player, Component.empty());
    }

    @Override
    public boolean isEmpty(Player player) {
        return false;
    }
}