package net.guizhanss.villagertrade.utils;

import javax.annotation.Nonnull;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SoundUtils {
    public static void playOpenMenuSound(@Nonnull Player player) {
        player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1.0F, 1.0F);
    }
}
