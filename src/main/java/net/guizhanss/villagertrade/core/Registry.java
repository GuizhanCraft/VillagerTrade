package net.guizhanss.villagertrade.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        slimefunTradeInputs.clear();
    }
}
