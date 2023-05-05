package net.guizhanss.villagertrade.utils;

import java.text.MessageFormat;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;

import net.guizhanss.guizhanlib.minecraft.utils.ChatUtil;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class MenuUtils {
    @Nonnull
    public static String format(@Nonnull String message, @Nullable Object... args) {
        return ChatUtil.color(MessageFormat.format(message, args));
    }

    @Nonnull
    public static ItemStack getBackButton(@Nonnull Player player) {
        return ChestMenuUtils.getBackButton(
            player,
            "",
            ChatColor.GRAY + Slimefun.getLocalization().getMessage(player, "guide.back.guide")
        );
    }
}
