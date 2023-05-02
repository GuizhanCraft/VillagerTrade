package net.guizhanss.villagertrade.implementation.menu;

import lombok.experimental.UtilityClass;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;

import org.bukkit.entity.Player;

@SuppressWarnings("deprecation")
@UtilityClass
public final class TradeDisplayMenu {
    public static void show(Player p) {
        ChestMenu menu = new ChestMenu("Trades");
    }
}
