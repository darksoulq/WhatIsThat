package com.github.darksoulq.wit.misc.regional;

public class RegionalCollections {
    public static final boolean IS_FOLIA;

    static {
        boolean folia = false;
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            folia = true;
        } catch (Exception ignored) {}
        IS_FOLIA = folia;
    }
}