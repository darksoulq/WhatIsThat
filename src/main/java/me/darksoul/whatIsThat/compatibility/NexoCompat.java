package me.darksoul.whatIsThat.compatibility;

import com.nexomc.nexo.api.NexoBlocks;
import com.nexomc.nexo.api.NexoFurniture;
import com.nexomc.nexo.api.NexoItems;
import com.nexomc.nexo.items.ItemBuilder;
import com.nexomc.nexo.mechanics.custom_block.noteblock.NoteBlockMechanic;
import com.nexomc.nexo.mechanics.custom_block.stringblock.StringBlockMechanic;
import com.nexomc.nexo.mechanics.furniture.FurnitureMechanic;
import me.darksoul.whatIsThat.WAILAListener;
import me.darksoul.whatIsThat.display.WAILAManager;
import me.darksoul.whatIsThat.WhatIsThat;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class NexoCompat {
    private static boolean isInstalled;

    public static void hook() {
        Plugin pl = WhatIsThat.getInstance().getServer().getPluginManager().getPlugin("Nexo");
        isInstalled = pl != null && pl.isEnabled();
        if (isInstalled) {
            WhatIsThat.getInstance().getLogger().info("Hooked into Nexo");
        } else {
            WhatIsThat.getInstance().getLogger().info("Nexo not found, skipping hook");
        }
    }
    public static boolean getIsInstalled() {
        return isInstalled;
    }

    public static boolean handleBlock(Block block, Player player) {
        if (NexoBlocks.isCustomBlock(block) || NexoFurniture.isFurniture(block.getLocation())) {
            String name;
            name = handleNoteBlockMechanic(block);
            if (name.isEmpty()) {
                name = handleStringBlockMechanic(block);
            }
            if (name.isEmpty()) {
                name = handleFurniture(block);
            }
            WAILAListener.setLookingAt(player, name);
            WAILAListener.setLookingAtPrefix(player, "");
            WAILAListener.setLookingAtSuffix(player, "");
            WAILAListener.setLookingAtInfo(player, name);
            WAILAManager.setBar(player, name);
            return true;
        }
        return false;
    }
    public static boolean handleEntity(Entity entity, Player player) {
        if (NexoFurniture.isFurniture(entity)) {
            String name = handleFurniture(entity);
            WAILAListener.setLookingAt(player, name);
            WAILAListener.setLookingAtPrefix(player, "");
            WAILAListener.setLookingAtSuffix(player, "");
            WAILAListener.setLookingAtInfo(player, name);
            WAILAManager.setBar(player, name);;
            return true;
        }
        return false;
    }

    // Internal methods
    private static String handleNoteBlockMechanic(Block block) {
        NoteBlockMechanic mechanic = NexoBlocks.noteBlockMechanic(block);
        if (mechanic != null) {
            ItemBuilder item = NexoItems.itemFromId(mechanic.getItemID());
            if (item != null && item.getDisplayName() != null) {
                return ((TextComponent) item.getDisplayName()).content();
            }
        }
        return "";
    }
    private static String handleStringBlockMechanic(Block block) {
        StringBlockMechanic mechanic = NexoBlocks.stringMechanic(block);
        if (mechanic != null) {
            ItemBuilder item = NexoItems.itemFromId(mechanic.getItemID());
            if (item != null && item.getDisplayName() != null) {
                return ((TextComponent) item.getDisplayName()).content();
            }
        }
        return "";
    }
    private static String handleFurniture(Block block) {
        FurnitureMechanic mechanic = NexoFurniture.furnitureMechanic(block);
        if (mechanic != null) {
            ItemBuilder item = NexoItems.itemFromId(mechanic.getItemID());
            if (item != null && item.getDisplayName() != null) {
                return ((TextComponent) item.getDisplayName()).content();
            }
        }
        return "";
    }
    private static String handleFurniture(Entity entity) {
        FurnitureMechanic mechanic = NexoFurniture.furnitureMechanic(entity);
        if (mechanic != null) {
            ItemBuilder item = NexoItems.itemFromId(mechanic.getItemID());
            if (item != null && item.getDisplayName() != null) {
                return ((TextComponent) item.getDisplayName()).content();
            }
        }
        return "";
    }
}
