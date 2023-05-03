package net.guizhanss.villagertrade.implementation.menu;

import org.bukkit.entity.Player;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;

import lombok.experimental.UtilityClass;

@SuppressWarnings("deprecation")
@UtilityClass
public final class TradeDisplayMenu {
    private final int SIZE = 54;

    // slots
    private final int[] HEADER = { 0, 2, 3, 4, 5, 6, 8 };
    private final int BACK_SLOT = 1;
    private final int INFO_SLOT = 7;
    private final int LIST_START = 9;
    private final int LIST_END = 53;

    public static void show(Player p) {
        ChestMenu menu = new ChestMenu("Trades");
    }
}
