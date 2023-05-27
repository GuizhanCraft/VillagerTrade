package net.guizhanss.villagertrade.utils;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.base.Preconditions;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

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

    @Nonnull
    @ParametersAreNonnullByDefault
    public static ConfigurationSection getOrCreateSection(ConfigurationSection section, String path) {
        Preconditions.checkArgument(section != null, "section cannot be null");
        Preconditions.checkArgument(path != null, "path cannot be null");

        ConfigurationSection newSection = section.getConfigurationSection(path);
        if (newSection == null) {
            newSection = section.createSection(path);
        }
        return newSection;
    }
}
