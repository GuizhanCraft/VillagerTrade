package net.guizhanss.villagertrade.utils;

import java.util.logging.Level;

import javax.annotation.ParametersAreNonnullByDefault;

import net.guizhanss.villagertrade.VillagerTrade;

public final class Debug {
    @ParametersAreNonnullByDefault
    public static void log(String message, Object... args) {
        if (VillagerTrade.getConfigManager() == null) {
            if (VillagerTrade.getInstance().getConfig().getBoolean("debug")) {
                logMsg(message, args);
            }
        } else {
            if (VillagerTrade.getConfigManager().isDebug()) {
                logMsg(message, args);
            }
        }
    }

    @ParametersAreNonnullByDefault
    private static void logMsg(String message, Object... args) {
        VillagerTrade.log(Level.INFO, "[DEBUG] " + message, args);
    }
}
