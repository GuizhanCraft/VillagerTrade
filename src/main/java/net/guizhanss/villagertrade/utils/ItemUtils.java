package net.guizhanss.villagertrade.utils;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.base.Preconditions;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import net.guizhanss.guizhanlib.minecraft.utils.ChatUtil;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class ItemUtils {
    @Nonnull
    public ItemStack addGlow(@Nonnull ItemStack item) {
        item.addUnsafeEnchantment(Enchantment.LUCK, 1);
        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Add extra lore to {@link ItemStack}. The item must have {@link ItemMeta}.
     *
     * @param item
     *     The {@link ItemStack} to add lore to.
     * @param extraLore
     *     The list of extra lore to be added.
     *
     * @return The new {@link ItemStack} with extra lore.
     */
    @Nonnull
    @ParametersAreNonnullByDefault
    public static ItemStack addLore(ItemStack item, List<String> extraLore) {
        Preconditions.checkArgument(item != null, "item cannot be null");
        Preconditions.checkArgument(extraLore != null, "extraLore cannot be null");
        Preconditions.checkArgument(item.hasItemMeta(), "item meta cannot be null");

        ItemMeta meta = item.getItemMeta();
        List<String> lore;

        if (meta.hasLore()) {
            lore = meta.getLore();
        } else {
            lore = new ArrayList<>();
        }

        for (String line : extraLore) {
            lore.add(ChatUtil.color(line));
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * This method checks if two {@link ItemStack}s can stack together.
     * Modified from dough.
     * <a href="https://github.com/baked-libs/dough/blob/main/dough-items/src/main/java/io/github/bakedlibs/dough/items/ItemUtils.java#L71">Here</a>
     *
     * @param a
     *     The item A to check.
     * @param b
     *     The item B to check.
     *
     * @return True if the two items can stack together.
     */
    public static boolean canStack(@Nullable ItemStack a, @Nullable ItemStack b) {
        if (a != null && b != null) {
            if (a.getType() == b.getType() && a.hasItemMeta() == b.hasItemMeta()) {
                if (a.hasItemMeta()) {
                    ItemMeta aMeta = a.getItemMeta();
                    ItemMeta bMeta = b.getItemMeta();
                    if (aMeta instanceof Damageable != (bMeta instanceof Damageable)) {
                        return false;
                    }

                    if (aMeta instanceof Damageable && ((Damageable) aMeta).getDamage() != ((Damageable) bMeta).getDamage()) {
                        return false;
                    }

                    if (aMeta instanceof LeatherArmorMeta != (bMeta instanceof LeatherArmorMeta)) {
                        return false;
                    }

                    if (aMeta instanceof LeatherArmorMeta && !((LeatherArmorMeta) aMeta).getColor().equals(((LeatherArmorMeta) bMeta).getColor())) {
                        return false;
                    }

                    if (aMeta.hasCustomModelData() != bMeta.hasCustomModelData()) {
                        return false;
                    }

                    if (aMeta.hasCustomModelData() && aMeta.getCustomModelData() != bMeta.getCustomModelData()) {
                        return false;
                    }

                    if (!aMeta.getPersistentDataContainer().equals(bMeta.getPersistentDataContainer())) {
                        return false;
                    }

                    if (!aMeta.getEnchants().equals(bMeta.getEnchants())) {
                        return false;
                    }

                    if (aMeta.hasDisplayName() != bMeta.hasDisplayName()) {
                        return false;
                    }

                    if (aMeta.hasDisplayName() && !aMeta.getDisplayName().equals(bMeta.getDisplayName())) {
                        return false;
                    }

                    if (aMeta.hasLore() != bMeta.hasLore()) {
                        return false;
                    }

                    if (aMeta.hasLore()) {
                        List<String> aLore = aMeta.getLore();
                        List<String> bLore = bMeta.getLore();
                        if (aLore.size() != bLore.size()) {
                            return false;
                        }

                        for (int i = 0; i < aLore.size(); ++i) {
                            if (!aLore.get(i).equals(bLore.get(i))) {
                                return false;
                            }
                        }
                    }
                }

                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

}
