package net.guizhanss.villagertrade.implementation.managers;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import net.guizhanss.villagertrade.VillagerTrade;
import net.guizhanss.villagertrade.implementation.listeners.VillagerListener;
import net.guizhanss.villagertrade.implementation.listeners.WanderingTraderListener;

public final class ListenerManager {
    private final VillagerTrade plugin;

    public ListenerManager(@Nonnull VillagerTrade plugin) {
        this.plugin = plugin;

        // register(new TradeInventoryListener());
        register(new VillagerListener());
        register(new WanderingTraderListener());
    }

    private void register(@Nonnull Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }
}
