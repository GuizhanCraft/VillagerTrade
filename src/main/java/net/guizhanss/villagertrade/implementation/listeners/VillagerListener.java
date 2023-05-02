package net.guizhanss.villagertrade.implementation.listeners;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.VillagerCareerChangeEvent;
import org.bukkit.inventory.MerchantRecipe;

import net.guizhanss.villagertrade.VillagerTrade;
import net.guizhanss.villagertrade.api.trades.TradeConfiguration;

public final class VillagerListener implements Listener {

    @EventHandler
    public void onCareerChange(@Nonnull VillagerCareerChangeEvent e) {
        Villager trader = e.getEntity();
        List<MerchantRecipe> recipes = new ArrayList<>(trader.getRecipes());
        for (TradeConfiguration tradeConfig : VillagerTrade.getRegistry().getVillagerConfigurations()) {
            if (tradeConfig.getTraderTypes().isValid(trader)) {
                recipes.add(tradeConfig.getMerchantRecipe());
            }
        }
        trader.setRecipes(recipes);
    }
}
