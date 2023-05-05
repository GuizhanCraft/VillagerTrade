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
import net.guizhanss.villagertrade.utils.Debug;

public final class VillagerListener implements Listener {

    @EventHandler
    public void onCareerChange(@Nonnull VillagerCareerChangeEvent e) {
        final Villager trader = e.getEntity();
        VillagerTrade.getScheduler().run(() -> {
            List<MerchantRecipe> recipes = new ArrayList<>(trader.getRecipes());
            for (TradeConfiguration tradeConfig : VillagerTrade.getRegistry().getVillagerConfigurations()) {
                if (tradeConfig.getTraderTypes().isValid(e.getProfession())) {
                    recipes.add(tradeConfig.getMerchantRecipe());
                    Debug.log("Added MerchantRecipe to Villager: " + tradeConfig.getKey());
                }
            }
            trader.setRecipes(recipes);
        });
    }
}
