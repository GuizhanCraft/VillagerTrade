package net.guizhanss.villagertrade.implementation.listeners;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.entity.WanderingTrader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.MerchantRecipe;

import net.guizhanss.villagertrade.VillagerTrade;
import net.guizhanss.villagertrade.api.trades.TradeConfiguration;
import net.guizhanss.villagertrade.utils.Debug;

public final class WanderingTraderListener implements Listener {

    @EventHandler
    public void onSpawn(@Nonnull EntitySpawnEvent e) {
        if (!(e.getEntity() instanceof WanderingTrader trader)) {
            return;
        }

        List<MerchantRecipe> recipes = new ArrayList<>(trader.getRecipes());
        for (TradeConfiguration tradeConfig : VillagerTrade.getRegistry().getWanderingTraderConfigurations()) {
            if (tradeConfig.getTraderTypes().isValid(trader)) {
                recipes.add(tradeConfig.getMerchantRecipe());
                Debug.log("Added MerchantRecipe to WanderingTrader: " + tradeConfig.getKey());
            }
        }
        trader.setRecipes(recipes);
    }
}
