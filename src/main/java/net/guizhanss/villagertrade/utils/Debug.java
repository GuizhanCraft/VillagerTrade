package net.guizhanss.villagertrade.utils;

import java.util.logging.Level;

import javax.annotation.ParametersAreNonnullByDefault;

import net.guizhanss.villagertrade.VillagerTrade;

@SuppressWarnings("ConstantConditions")
public final class Debug {

    public static boolean isEnabled() {
        if (VillagerTrade.getConfigManager() == null) {
            return VillagerTrade.getInstance().getConfig().getBoolean("debug");
        } else {
            return VillagerTrade.getConfigManager().isDebug();
        }
    }

    @ParametersAreNonnullByDefault
    public static void log(String message, Object... args) {
        if (isEnabled()) {
            VillagerTrade.log(Level.INFO, "[DEBUG] " + message, args);
        }
    }

    @ParametersAreNonnullByDefault
    public static void logRaw(Object obj) {
        if (isEnabled()) {
            VillagerTrade.getInstance().getLogger().log(Level.INFO, "[DEBUG] " + obj);
        }
    }
}
