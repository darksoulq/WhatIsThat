package com.github.darksoulq.wit.api;

import com.github.darksoulq.wit.Information;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.ArrayList;
import java.util.List;

/*
 * This class is used for supplying Prefix, Suffix and Name (of entity/block) to relevant functions
 */
public class Info {
    private final List<Component> prefix = new ArrayList<>();
    private final List<Component> suffix = new ArrayList<>();
    private Component name;

    public void addPrefix(Component information) {
        prefix.add(information);
    }
    public void addSuffix(Component information) {
        suffix.add(information);
    }
    public void setName(Component name) {
        this.name = name;
    }

    public Component getPrefix() {
        Component p = Component.empty();
        for (Component pref : prefix) {
            p = p.append(pref);
        }
        return p;
    }
    public Component getSuffix() {
        Component s = Component.empty();
        for (Component suf : suffix) {
            s = s.append(suf);
        }
        return s;
    }
    public Component getName() {
        return name;
    }
    public Component getCombined() {
        Component combined = Component.empty();

        if (!prefix.isEmpty()) {
            for (Component p : prefix) {
                combined = combined.append(p);
            }
            combined = combined.append(MiniMessage.miniMessage().deserialize(Information.getValuesFile().getString("SPLITTER", " §f| ")));
        }
        if (name != null) {
            combined = combined.append(name);
        }
        if (!suffix.isEmpty()) {
            combined = combined.append(MiniMessage.miniMessage().deserialize(Information.getValuesFile().getString("SPLITTER", " §f| ")));
            for (Component s : suffix) {
                combined = combined.append(s);
            }
        }

        return combined;
    }

}
