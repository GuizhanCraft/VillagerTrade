package net.guizhanss.villagertrade.implementation.managers;

import java.util.logging.Level;

import net.guizhanss.guizhanlib.slimefun.addon.AddonConfig;
import net.guizhanss.villagertrade.VillagerTrade;
import net.guizhanss.villagertrade.api.trades.TradeConfiguration;
import net.guizhanss.villagertrade.utils.ConfigUtils;
import net.guizhanss.villagertrade.utils.Debug;

import lombok.Getter;
import lombok.experimental.Accessors;

import javax.annotation.Nonnull;

@Getter
public final class ConfigManager {

    private static final String FILENAME_TRADES = "trades.yml";

    private final AddonConfig config;
    private final AddonConfig trades;

    @Accessors(fluent = true)
    private boolean isDebug;

    public ConfigManager(@Nonnull VillagerTrade plugin) {
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
        ConfigUtils.addMissingOptions(config);

        isDebug = config.getBoolean("debug", false);

        VillagerTrade.getScheduler().run(this::loadTrades);
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
