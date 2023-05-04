package net.guizhanss.villagertrade.core;

import java.util.ArrayList;
import java.util.List;

import net.guizhanss.villagertrade.api.trades.TradeConfiguration;
import net.guizhanss.villagertrade.api.trades.TradeItem;

import lombok.Getter;

@Getter
public final class Registry {
    public List<TradeConfiguration> tradeConfigurations = new ArrayList<>();
    public List<TradeConfiguration> wanderingTraderConfigurations = new ArrayList<>();
    public List<TradeConfiguration> villagerConfigurations = new ArrayList<>();
    public List<TradeItem> slimefunTradeItems = new ArrayList<>();

    public void reset() {
        tradeConfigurations.clear();
        wanderingTraderConfigurations.clear();
        villagerConfigurations.clear();
        slimefunTradeItems.clear();
    }
}
