package net.guizhanss.villagertrade.core.menu;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;

import javax.annotation.Nonnull;

import java.util.List;

/**
 * This is a modified {@link ChestMenu} that adds ticking method.
 *
 * @author ybw0014
 */
public class TickingMenu {
    private String title;
    private Inventory inventory;
    private List<ItemStack> items;

    public TickingMenu(@Nonnull String title) {
        this.title = title;
    }
}
