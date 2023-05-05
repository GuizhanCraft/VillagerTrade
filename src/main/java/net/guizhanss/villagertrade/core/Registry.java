package net.guizhanss.villagertrade.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.guizhanss.villagertrade.api.trades.TradeConfiguration;
import net.guizhanss.villagertrade.api.trades.TradeItem;

import lombok.Getter;

@Getter
public final class Registry {
    private final List<TradeConfiguration> tradeConfigurations = new ArrayList<>();
    private final Map<String, TradeConfiguration> tradeConfigurationMap = new HashMap<>();
    private final List<TradeConfiguration> wanderingTraderConfigurations = new ArrayList<>();
    private final List<TradeConfiguration> villagerConfigurations = new ArrayList<>();
    private final Set<TradeItem> slimefunTradeInputs = new HashSet<>();

    public void reset() {
        tradeConfigurations.clear();
        tradeConfigurationMap.clear();
        wanderingTraderConfigurations.clear();
        villagerConfigurations.clear();
        slimefunTradeInputs.clear();
    }
}
