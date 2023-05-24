package net.guizhanss.villagertrade.core.menu;

import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;

/**
 * This is a modified {@link ChestMenu} that adds ticking method.
 *
 * @author ybw0014
 */
public class TickingMenu {
    private final String title;
    private Inventory inventory;
    private List<ItemStack> items;

    public TickingMenu(@Nonnull String title) {
        this.title = title;
    }
}
