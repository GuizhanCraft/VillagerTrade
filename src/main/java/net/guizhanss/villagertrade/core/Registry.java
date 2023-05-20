package net.guizhanss.villagertrade.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
}
