package net.guizhanss.villagertrade.core;

import java.util.ArrayList;
import java.util.List;

import net.guizhanss.villagertrade.api.trades.TradeConfiguration;

import lombok.Getter;

@Getter
public final class Registry {
    public List<TradeConfiguration> configurations = new ArrayList<>();
}
