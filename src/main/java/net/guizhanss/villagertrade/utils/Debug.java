package net.guizhanss.villagertrade.utils;

import java.util.logging.Level;

import net.guizhanss.villagertrade.VillagerTrade;

public final class Debug {
    public static void log(String message, Object... args) {
        if (VillagerTrade.getConfigManager().isDebug()) {
            VillagerTrade.log(Level.INFO, message, args);
        }
    }
}
