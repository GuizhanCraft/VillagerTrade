package net.guizhanss.villagertrade.utils;

import net.guizhanss.guizhanlib.slimefun.addon.AddonConfig;

import org.bukkit.configuration.Configuration;

import lombok.experimental.UtilityClass;

import javax.annotation.Nonnull;

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
