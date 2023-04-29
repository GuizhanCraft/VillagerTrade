package net.guizhanss.villagertrade.implementation.managers;

import java.util.logging.Level;

import lombok.experimental.Accessors;

import org.bukkit.configuration.Configuration;

import net.guizhanss.guizhanlib.slimefun.addon.AddonConfig;
import net.guizhanss.villagertrade.VillagerTrade;
import net.guizhanss.villagertrade.api.TradeConfiguration;

import lombok.Getter;

public final class ConfigManager {

    private static final String FILENAME_CONFIG = "config.yml";
    private static final String FILENAME_TRADES = "trades.yml";

    @Getter
    private final AddonConfig config;
    @Getter
    private final AddonConfig trades;

    @Getter
    @Accessors(fluent = true)
    private boolean isDebug;

    public ConfigManager(VillagerTrade plugin) {
        config = new AddonConfig(plugin, FILENAME_CONFIG);
        trades = new AddonConfig(plugin, FILENAME_TRADES);

        afterReload();
    }

    public void reloadAll() {
        config.reload();
        trades.reload();
        afterReload();
    }

    private void afterReload() {
        updateConfig(config);

        isDebug = config.getBoolean("debug", false);

        loadTrades();
    }

    private void updateConfig(AddonConfig config) {
        Configuration defaultConfig = config.getDefaults();
        for (String key : defaultConfig.getKeys(true)) {
            if (!config.contains(key)) {
                config.set(key, defaultConfig.get(key));
            }
        }
    }

    private void loadTrades() {
        if (!config.getBoolean("enable-trades")) {
            VillagerTrade.log(Level.WARNING, "Trades are disabled in the config!");
            VillagerTrade.log(Level.WARNING, "You need to set up the trades in trades.yml,");
            VillagerTrade.log(Level.WARNING, "and enable trades in config.yml.");
            return;
        }

        for (String key : trades.getKeys(false)) {
            TradeConfiguration tradeConfig = trades.getSerializable(key, TradeConfiguration.class);
            continue;
        }
    }
}
