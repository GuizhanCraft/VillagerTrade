package net.guizhanss.villagertrade.implementation.managers;

import java.util.logging.Level;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import net.guizhanss.guizhanlib.slimefun.addon.AddonConfig;
import net.guizhanss.villagertrade.VillagerTrade;
import net.guizhanss.villagertrade.api.trades.TradeConfiguration;
import net.guizhanss.villagertrade.utils.ConfigUtils;
import net.guizhanss.villagertrade.utils.Debug;

import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
public final class ConfigManager {

    private static final String FILE_TRADES = "trades.yml";

    private final VillagerTrade plugin;
    private final AddonConfig config;
    private final AddonConfig trades;

    @Accessors(fluent = true)
    private boolean isDebug;

    public ConfigManager(@Nonnull VillagerTrade plugin) {
        this.plugin = plugin;

        config = (AddonConfig) plugin.getConfig();
        trades = new AddonConfig(plugin, FILE_TRADES);

        afterReload();
    }

    public void reloadAll() {
        config.reload();
        trades.reload();
        afterReload();
    }

    @ParametersAreNonnullByDefault
    public void saveTrade(TradeConfiguration tradeConfig) {
        tradeConfig.saveToConfig(ConfigUtils.getOrCreateSection(trades, tradeConfig.getKey()));
        trades.save();
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

        int count = 0;
        for (String key : trades.getKeys(false)) {
            Debug.log("Loading trade: " + key);
            try {
                TradeConfiguration tradeConfig =
                    TradeConfiguration.loadFromConfig(key, trades.getConfigurationSection(key));
                Debug.log("Trade config of " + key + ":");
                Debug.logRaw(tradeConfig.toString());
                tradeConfig.register(plugin);
                if (tradeConfig.getState() == TradeConfiguration.RegistrationState.REGISTERED) {
                    Debug.log("Successfully registered trade: " + key);
                    count++;
                }
            } catch (NullPointerException ex) {
                VillagerTrade.log(Level.SEVERE, "Failed to load trade: " + key + ", invalid config has been provided!");
            } catch (Exception ex) {
                VillagerTrade.log(Level.SEVERE, ex, "Failed to load trade: " + key);
            }
        }

        VillagerTrade.log(Level.INFO, "Successfully loaded " + count + " trades!");
    }
}
