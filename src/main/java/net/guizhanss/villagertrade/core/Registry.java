package net.guizhanss.villagertrade.core;

import java.util.List;

import net.guizhanss.villagertrade.api.TradeConfiguration;

import lombok.Data;

@Data
public final class Registry {
    public List<TradeConfiguration> configurations;
}
