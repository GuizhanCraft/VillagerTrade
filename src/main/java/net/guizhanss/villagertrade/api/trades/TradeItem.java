package net.guizhanss.villagertrade.api.trades;

import java.util.Objects;
import java.util.logging.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Preconditions;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;

import net.guizhanss.villagertrade.VillagerTrade;
import net.guizhanss.villagertrade.api.trades.mutables.MutableTradeItem;

import lombok.Getter;

/**
 * Represents a immutable trade item.
 *
 * @author ybw0014
 * @see MutableTradeItem
 */
@Getter
@SuppressWarnings("ConstantConditions")
public final class TradeItem {

    private static final String KEY_TYPE = "type";
    private static final String KEY_ID = "id";
    private static final String KEY_AMOUNT = "amount";

    private final ItemStack item;
    private final TradeItemType type;
    private final String id;
    private final int amount;

    /**
     * Construct a {@link TradeItem}, with default amount 1.
     *
     * @param type
     *     The {@link String} representation of {@link TradeItemType}.
     * @param id
     *     The id of item, it can be a vanilla material name or a Slimefun item id.
     */
    public TradeItem(@Nonnull String type, @Nullable String id) {
        this(type, id, 1);
    }

    /**
     * Construct a {@link TradeItem}.
     *
     * @param type
     *     The {@link String} representation of {@link TradeItemType}.
     * @param id
     *     The id of item, it can be a vanilla material name or a Slimefun item id.
     * @param amount
     *     The amount of item, 1 by default.
     */
    public TradeItem(@Nonnull String type, @Nullable String id, int amount) {
        Preconditions.checkArgument(type != null, "TraderItemType cannot be null");

        this.id = id;
        this.amount = amount;

        TradeItemType tempType = TradeItemType.NONE;
        ItemStack tempItem = null;

        switch (type.toUpperCase()) {
            case "VANILLA" -> {
                try {
                    Material mat = Material.getMaterial(id.toUpperCase());
                    tempItem = new ItemStack(mat, amount);
                    tempType = TradeItemType.VANILLA;
                } catch (Exception ex) {
                    VillagerTrade.log(Level.SEVERE, "The material " + id + " is not a valid vanilla material");
                }
            }
            case "SLIMEFUN" -> {
                try {
                    tempItem = new CustomItemStack(SlimefunItem.getById(id).getItem(), amount);
                    tempType = TradeItemType.SLIMEFUN;
                } catch (Exception ex) {
                    VillagerTrade.log(Level.SEVERE, "The id " + id + " is not a valid Slimefun item");
                }
            }
            case "CUSTOM" -> {
                try {
                    tempItem = new CustomItemStack(VillagerTrade.getCustomItemService().getItem(id), amount);
                    tempType = TradeItemType.CUSTOM;
                } catch (Exception ex) {
                    VillagerTrade.log(Level.SEVERE, "The id " + id + " is not a valid custom item");
                }
            }
            case "NONE" -> {
            }
            default -> VillagerTrade.log(Level.SEVERE, "The type " + type + " is not a valid item type, setting to " +
                "NONE");
        }

        // amount check
        if (tempItem != null && (amount < 1 || amount > tempItem.getMaxStackSize())) {
            VillagerTrade.log(Level.SEVERE, "The amount " + amount + " is not a valid amount for " + id);
            tempType = TradeItemType.NONE;
            tempItem = null;
        }

        this.type = tempType;
        this.item = tempItem;
    }

    /**
     * Load a {@link TradeItem} from a {@link ConfigurationSection}.
     *
     * @param section
     *     The {@link ConfigurationSection} to load from.
     *
     * @return The {@link TradeItem}.
     */
    @Nonnull
    public static TradeItem loadFromConfig(@Nonnull ConfigurationSection section) {
        Preconditions.checkArgument(section != null, "The ConfigurationSection cannot be null");

        String type = section.getString(KEY_TYPE, "NONE");
        String id = section.getString(KEY_ID);
        int amount = section.getInt(KEY_AMOUNT, 1);

        return new TradeItem(type, id, amount);
    }

    /**
     * Load a {@link TradeItem} from an {@link ItemStack}.
     *
     * @param item
     *     The {@link ItemStack} to load from.
     *
     * @return The {@link TradeItem} generated from the given {@link ItemStack}.
     */
    @Nonnull
    public static TradeItem loadFromItem(@Nonnull ItemStack item) {
        Preconditions.checkArgument(item != null, "The ItemStack cannot be null");

        return loadFromItem(item, item.getAmount());
    }

    /**
     * Load a {@link TradeItem} from an {@link ItemStack}.
     *
     * @param item
     *     The {@link ItemStack} to load from.
     * @param amount
     *     The amount of item.
     *
     * @return The {@link TradeItem} generated from the given {@link ItemStack}.
     */
    @Nonnull
    public static TradeItem loadFromItem(@Nonnull ItemStack item, int amount) {
        Preconditions.checkArgument(item != null, "The ItemStack cannot be null");

        SlimefunItem sfItem = SlimefunItem.getByItem(item);
        if (sfItem != null) {
            return new TradeItem("SLIMEFUN", sfItem.getId(), amount);
        } else if (item.hasItemMeta()) {
            return new TradeItem("CUSTOM", VillagerTrade.getCustomItemService().getId(item), amount);
        } else {
            return new TradeItem("VANILLA", item.getType().name(), amount);
        }
    }

    /**
     * Save the {@link TradeItem} to a {@link ConfigurationSection}.
     *
     * @param section
     *     The {@link ConfigurationSection} to save to.
     */
    public void saveToConfig(@Nonnull ConfigurationSection section) {
        Preconditions.checkArgument(section != null, "The ConfigurationSection cannot be null");

        section.set(KEY_TYPE, type.name());
        section.set(KEY_ID, id);
        section.set(KEY_AMOUNT, amount);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TradeItem other = (TradeItem) o;
        return type == other.type && Objects.equals(id, other.id) && amount == other.amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, id, amount);
    }

    @Nonnull
    @Override
    public String toString() {
        return "TradeItem(type=" + type
            + ", id=" + id
            + ", amount=" + amount
            + ")";
    }

    @Nonnull
    public String toShortString() {
        if (type == TradeItemType.NONE) {
            return "NONE";
        } else {
            return type.toString() + ", " + id + " x" + amount;
        }
    }

    /**
     * Represents the type of item.
     */
    public enum TradeItemType {
        /**
         * The item is a vanilla item (the items get from creative mode inventory).
         */
        VANILLA,

        /**
         * The item is a Slimefun item.
         */
        SLIMEFUN,

        /**
         * This item is a modified vanilla item, but not a Slimefun item.
         */
        CUSTOM,

        /**
         * This item is null.
         */
        NONE
    }
}
