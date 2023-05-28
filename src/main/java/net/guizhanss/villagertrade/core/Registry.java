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
                // TODO: wait for PR to Slimefun
                // sfItem.setTradable(false);
            }
        }
        slimefunTradeInputs.clear();
    }

    /**
     * Clear the specified {@link TradeConfiguration}.<br>
     * DO NOT call this method. It is only for internal use.
     */
    @ParametersAreNonnullByDefault
    public void clear(TradeConfiguration tradeConfig) {
        tradeConfigurations.remove(tradeConfig.getKey());
        wanderingTraderConfigurations.removeIf(trade -> trade.getKey().equals(tradeConfig.getKey()));
        villagerConfigurations.removeIf(trade -> trade.getKey().equals(tradeConfig.getKey()));
    }
}
