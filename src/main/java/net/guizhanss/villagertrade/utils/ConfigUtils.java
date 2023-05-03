package net.guizhanss.villagertrade.utils;

import javax.annotation.Nonnull;

import org.bukkit.configuration.Configuration;

import net.guizhanss.guizhanlib.slimefun.addon.AddonConfig;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class ConfigUtils {
    public static void addMissingOptions(@Nonnull AddonConfig config) {
        Configuration defaultConfig = config.getDefaults();
        for (String key : defaultConfig.getKeys(true)) {
            if (!config.contains(key)) {
                config.set(key, defaultConfig.get(key));
            }
        }
    }
}
