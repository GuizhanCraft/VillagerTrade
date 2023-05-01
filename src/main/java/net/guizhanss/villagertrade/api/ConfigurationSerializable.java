package net.guizhanss.villagertrade.api;

import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nonnull;

public interface ConfigurationSerializable {
    void saveToConfig(@Nonnull ConfigurationSection section);
}
