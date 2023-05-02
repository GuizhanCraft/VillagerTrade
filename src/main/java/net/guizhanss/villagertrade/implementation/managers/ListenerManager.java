package net.guizhanss.villagertrade.implementation.managers;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import net.guizhanss.villagertrade.VillagerTrade;
import net.guizhanss.villagertrade.implementation.listeners.VillagerListener;

public final class ListenerManager {
    private final VillagerTrade plugin;

    public ListenerManager(@Nonnull VillagerTrade plugin) {
        this.plugin = plugin;

        register(new VillagerListener());
    }

    private void register(@Nonnull Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }
}
