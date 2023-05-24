package net.guizhanss.villagertrade.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.ParametersAreNonnullByDefault;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;

import net.guizhanss.villagertrade.api.trades.TradeConfiguration;
import net.guizhanss.villagertrade.api.trades.TradeItem;

import lombok.Getter;

@Getter
public final class Registry {
    private final Map<String, TradeConfiguration> tradeConfigurations = new LinkedHashMap<>();
    private final List<TradeConfiguration> wanderingTraderConfigurations = new ArrayList<>();
    private final List<TradeConfiguration> villagerConfigurations = new ArrayList<>();
    private final Set<TradeItem> slimefunTradeInputs = new HashSet<>();

    public void reset() {
        tradeConfigurations.clear();
        wanderingTraderConfigurations.clear();
        villagerConfigurations.clear();

        // need to revert tradable changes of these items first
        for (TradeItem item : slimefunTradeInputs) {
            SlimefunItem sfItem = SlimefunItem.getById(item.getId());
            if (sfItem != null) {
                sfItem.setTradable(false);
            }
        }
        slimefunTradeInputs.clear();
    }

    /**
     * Replace the trade configuration with the new one.
     * DO NOT call this method. It is only for internal use.
     */
    @ParametersAreNonnullByDefault
    public void replace(TradeConfiguration oldConfig, TradeConfiguration newConfig) {
        tradeConfigurations.put(newConfig.getKey(), newConfig);
        if (wanderingTraderConfigurations.contains(oldConfig)) {
            if (newConfig.getTraderTypes().hasWanderingTrader()) {
                wanderingTraderConfigurations.set(wanderingTraderConfigurations.indexOf(oldConfig), newConfig);
            } else {
                wanderingTraderConfigurations.remove(oldConfig);
            }
        }
        if (villagerConfigurations.contains(oldConfig)) {
            if (newConfig.getTraderTypes().hasVillager()) {
                villagerConfigurations.set(villagerConfigurations.indexOf(oldConfig), newConfig);
            } else {
                villagerConfigurations.remove(oldConfig);
            }
        }

    }
}
