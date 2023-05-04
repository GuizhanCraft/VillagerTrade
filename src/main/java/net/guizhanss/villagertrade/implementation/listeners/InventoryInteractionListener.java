package net.guizhanss.villagertrade.implementation.listeners;

import javax.annotation.Nonnull;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class InventoryInteractionListener implements Listener {
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onInventoryClick(@Nonnull InventoryClickEvent e) {
        Inventory clickedInventory = e.getClickedInventory();
        Inventory topInventory = e.getView().getTopInventory();

        if (clickedInventory == null || topInventory.getType() != InventoryType.MERCHANT) {
        }

    }
}
