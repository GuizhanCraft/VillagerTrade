package net.guizhanss.villagertrade.implementation.menu;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.scheduler.BukkitRunnable;

public class TradeMenuTask extends BukkitRunnable {

    private static TradeMenuTask instance;
    private final Map<UUID, TradeMenu> menuMap = new HashMap<>();

    public TradeMenuTask() {
        if (instance != null) {
            throw new IllegalStateException("This class may only be instantiated once");
        }
        instance = this;
    }

    @ParametersAreNonnullByDefault
    public static void addPlayer(UUID uuid, TradeMenu menu) {
        instance.menuMap.put(uuid, menu);
    }

    @ParametersAreNonnullByDefault
    public static void removePlayer(UUID uuid) {
        instance.menuMap.remove(uuid);
    }

    @Override
    public void run() {
        for (Map.Entry<UUID, TradeMenu> entry : menuMap.entrySet()) {
            entry.getValue().tick();
        }
    }
}
