package net.guizhanss.villagertrade.implementation.managers;

import java.util.logging.Level;

import org.bukkit.configuration.Configuration;

import net.guizhanss.guizhanlib.slimefun.addon.AddonConfig;
import net.guizhanss.villagertrade.VillagerTrade;
import net.guizhanss.villagertrade.api.trades.TradeConfiguration;
import net.guizhanss.villagertrade.utils.Debug;

import lombok.Getter;
import lombok.experimental.Accessors;

public final class ConfigManager {

    private static final String FILENAME_TRADES = "trades.yml";

    @Getter
    private final AddonConfig config;
    @Getter
    private final AddonConfig trades;

    @Getter
    @Accessors(fluent = true)
    private boolean isDebug;

    public ConfigManager(VillagerTrade plugin) {
        config = (AddonConfig) plugin.getConfig();
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

        VillagerTrade.getScheduler().run(this::loadTrades);
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

        VillagerTrade.log(Level.INFO, "Trades are enabled! Loading...");

        for (String key : trades.getKeys(false)) {
            Debug.log("Loading trade: " + key);
            try {
                TradeConfiguration tradeConfig = TradeConfiguration.loadFromConfig(trades.getConfigurationSection(key));
                tradeConfig.register();
                Debug.log("Successfully registered trade: " + key);
            } catch (Exception ex) {
                VillagerTrade.log(Level.SEVERE, ex, "Failed to load trade: " + key);
            }
        }
    }
}
