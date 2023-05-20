package net.guizhanss.villagertrade.implementation.menu.objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Preconditions;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;

import net.guizhanss.villagertrade.api.trades.TradeItem;

import lombok.Getter;

/**
 * This class is a wrapper of {@link TradeItem} for menu use ONLY.
 *
 * @author ybw0014
 * @see TradeItem
 */
@Getter
public final class MutableTradeItem {
    private ItemStack item;
    private TradeItem.TradeItemType type;
    private String id;
    private int amount;

    public MutableTradeItem(@Nonnull TradeItem tradeItem) {
        Preconditions.checkArgument(tradeItem != null, "tradeItem cannot be null");

        item = tradeItem.getItem();
        type = tradeItem.getType();
        id = tradeItem.getId();
        amount = tradeItem.getAmount();
    }

    public void setItem(@Nullable ItemStack item) {
        this.item = item;

        if (item == null || item.getType().isAir()) {
            type = TradeItem.TradeItemType.NONE;
            id = null;
            amount = 1;
            return;
        }

        SlimefunItem sfItem = SlimefunItem.getByItem(item);
        if (sfItem != null) {
            type = TradeItem.TradeItemType.SLIMEFUN;
            id = sfItem.getId();
        } else {
            type = TradeItem.TradeItemType.VANILLA;
            id = item.getType().name();
        }
        amount = item.getAmount();
    }

    /**
     * Set the amount of item.<br>
     * If the amount is invalid, it will be set to the nearest valid value, and return false.
     *
     * @param amount
     *     The amount of item.
     *
     * @return true if the amount is valid, false otherwise.
     */
    public boolean setAmount(int amount) {
        boolean isValid = true;
        if (amount < 1) {
            amount = 1;
            isValid = false;
        }
        if (amount > item.getMaxStackSize()) {
            amount = item.getMaxStackSize();
            isValid = false;
        }

        this.amount = amount;
        item.setAmount(amount);
        return isValid;
    }

    public int getMaxStackSize() {
        if (item != null && !item.getType().isAir()) {
            return item.getMaxStackSize();
        } else {
            return 1;
        }
    }

    public TradeItem toTradeItem() {
        return new TradeItem(type.toString(), id, amount);
    }
}
