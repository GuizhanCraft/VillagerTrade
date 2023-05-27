package net.guizhanss.villagertrade.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.base.Preconditions;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.thebusybiscuit.slimefun4.libraries.dough.chat.ChatInput;

import net.guizhanss.guizhanlib.minecraft.utils.ChatUtil;
import net.guizhanss.villagertrade.VillagerTrade;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class MenuUtils {
    @Nonnull
    @ParametersAreNonnullByDefault
    public static ItemStack parseVariables(ItemStack item, Map<String, String> map) {
        Preconditions.checkArgument(item != null, "item cannot be null");
        Preconditions.checkArgument(map != null, "map cannot be null");
        Preconditions.checkArgument(item.hasItemMeta(), "item meta cannot be null");

        ItemMeta meta = item.getItemMeta();

        if (meta.hasDisplayName()) {
            String displayName = meta.getDisplayName();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                displayName = displayName.replace(entry.getKey(), entry.getValue());
            }
            meta.setDisplayName(displayName);
        }

        if (meta.hasLore()) {
            List<String> lore = meta.getLore();
            for (int i = 0; i < lore.size(); i++) {
                String line = lore.get(i);
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    line = line.replace(entry.getKey(), entry.getValue());
                }
                lore.set(i, line);
            }
            meta.setLore(lore);
        }

        item.setItemMeta(meta);
        return item;
    }

    @ParametersAreNonnullByDefault
    public void awaitInput(Player p, Consumer<String> callback) {
        ChatInput.waitForPlayer(VillagerTrade.getInstance(), p, callback);
    }

    @ParametersAreNonnullByDefault
    public void awaitInput(Player p, Predicate<String> validator, Consumer<String> successCallback) {
        awaitInput(p, validator, successCallback, (str) -> {
        });
    }

    @ParametersAreNonnullByDefault
    public void awaitInput(Player p, Predicate<String> validator, Consumer<String> successCallback,
                           Consumer<String> failCallback) {
        ChatInput.waitForPlayer(VillagerTrade.getInstance(), p, (input) -> {
            if (validator.test(input)) {
                successCallback.accept(input);
            } else {
                failCallback.accept(input);
                awaitInput(p, validator, successCallback, failCallback);
            }
        });
    }


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
}
