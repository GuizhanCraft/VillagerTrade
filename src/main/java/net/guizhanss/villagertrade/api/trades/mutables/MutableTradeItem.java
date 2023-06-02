package net.guizhanss.villagertrade.api.trades.mutables;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Preconditions;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;

import net.guizhanss.villagertrade.VillagerTrade;
import net.guizhanss.villagertrade.api.trades.TradeItem;
import net.guizhanss.villagertrade.implementation.menu.TradeMenu;
import net.guizhanss.villagertrade.utils.ItemUtils;

import lombok.Getter;

/**
 * This class is a wrapper of {@link TradeItem} that allows you to change the item.
 *
 * @author ybw0014
 * @see TradeItem
 * @see TradeMenu
 */
@Getter
public final class MutableTradeItem {
    private ItemStack item;
    private TradeItem.TradeItemType type;
    private String id;
    private int amount;

    public MutableTradeItem() {
        item = null;
        type = TradeItem.TradeItemType.NONE;
        id = null;
        amount = 1;
    }

    public MutableTradeItem(@Nonnull TradeItem tradeItem) {
        Preconditions.checkArgument(tradeItem != null, "tradeItem cannot be null");

        item = tradeItem.getItem();
        type = tradeItem.getType();
        id = tradeItem.getId();
        amount = tradeItem.getAmount();
    }

    /**
     * This method accept an {@link ItemStack} from menu.
     *
     * @param item
     *     The item from menu.
     */
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
        } else if (item.hasItemMeta()) {
            type = TradeItem.TradeItemType.CUSTOM;
            id = VillagerTrade.getCustomItemService().getId(item);

            // we need to create a new custom item, if the item does not match any registered custom item.
            if (id == null) {
                id = VillagerTrade.getCustomItemService().addItem(item);
            }
        } else {
            type = TradeItem.TradeItemType.VANILLA;
            id = item.getType().name();
        }
        amount = item.getAmount();
    }

    public boolean isItem(@Nullable ItemStack item) {
        if (item == null || item.getType().isAir()) {
            return type == TradeItem.TradeItemType.NONE;
        }
        if (item.getAmount() != this.amount) {
            return false;
        }

        return switch (type) {
            case VANILLA -> this.item.getType() == item.getType();
            case SLIMEFUN -> SlimefunItem.getById(id).isItem(item);
            case CUSTOM -> ItemUtils.canStack(this.item, item);
            default -> false;
        };
    }

    /**
     * Set the amount of item.
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

    /**
     * Get the maximum stack size of this item.
     *
     * @return the maximum stack size of this item, 1 when the item is invalid.
     */
    public int getMaxStackSize() {
        if (item != null && !item.getType().isAir()) {
            return item.getMaxStackSize();
        } else {
            return 1;
        }
    }

    /**
     * Make a {@link TradeItem} copy of this.
     *
     * @return A new {@link TradeItem} instance.
     */
    public TradeItem toTradeItem() {
        return new TradeItem(type.toString(), id, amount);
    }

    @Nonnull
    @Override
    public String toString() {
        return "MutableTradeItem(type=" + type
            + ", id=" + id
            + ", amount=" + amount
            + ")";
    }

    /**
     * Get the short "type:id:amount" string of this item.
     * The ":id:amount" part will not show if the type is NONE.
     * The ":amount" part will not show if the amount is 1.
     *
     * @param displayAmount
     *     Whether to display the amount. (set this to false will always not show the amount)
     *
     * @return The "type:id" string of this item.
     */
    @Nonnull
    public String toShortString(boolean displayAmount) {
        StringBuilder builder = new StringBuilder(type.toString());
        if (type != TradeItem.TradeItemType.NONE) {
            builder.append(':').append(id);
            if (displayAmount && amount != 1) {
                builder.append(':').append(amount);
            }
        }
        return builder.toString();
    }
}
