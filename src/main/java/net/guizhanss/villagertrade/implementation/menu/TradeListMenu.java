package net.guizhanss.villagertrade.implementation.menu;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.base.Preconditions;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;

import net.guizhanss.guizhanlib.minecraft.utils.ChatUtil;
import net.guizhanss.villagertrade.VillagerTrade;
import net.guizhanss.villagertrade.api.trades.TradeConfiguration;
import net.guizhanss.villagertrade.utils.MenuUtils;
import net.guizhanss.villagertrade.utils.constants.Keys;

import lombok.experimental.UtilityClass;

@SuppressWarnings("deprecation")
@UtilityClass
public final class TradeListMenu {
    // slots
    private static final int[] HEADER = { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
    private static final int[] FOOTER = { 45, 47, 48, 49, 50, 51, 53 };
    private static final int PAGE_SIZE = 36;
    private static final int PAGE_PREVIOUS = 46;
    private static final int PAGE_NEXT = 52;

    public static void show(@Nonnull Player p) {
        final ChestMenu menu = new ChestMenu(VillagerTrade.getLocalization().getLang("menu.list.title"));
        setupMenu(menu);
        displayList(p, menu, 1);
        menu.open(p);
    }

    @ParametersAreNonnullByDefault
    private static void setupMenu(ChestMenu menu) {
        for (int slot : HEADER) {
            menu.addItem(slot, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }
        for (int slot : FOOTER) {
            menu.addItem(slot, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }

        menu.setEmptySlotsClickable(false);
    }

    private static void displayList(Player player, ChestMenu menu, int page) {
        final List<TradeConfiguration> trades = VillagerTrade.getRegistry().getTradeConfigurations();
        final int total = trades.size();
        final int totalPages = (int) Math.ceil(total / (double) PAGE_SIZE);
        final int start = (page - 1) * PAGE_SIZE;
        final int end = Math.min(start + PAGE_SIZE, total);

        final List<TradeConfiguration> subList = trades.subList(start, end);

        setupFooter(player, menu, page, totalPages);

        // Sound
        player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1.0F, 1.0F);

        for (int i = 0; i < PAGE_SIZE; i++) {
            final int slot = i + 9;
            if (i + 1 <= subList.size()) {
                final TradeConfiguration trade = subList.get(i);
                menu.replaceExistingItem(slot, getTradeDisplayItem(trade));
                menu.addMenuClickHandler(slot, ChestMenuUtils.getEmptyClickHandler());
            } else {
                menu.replaceExistingItem(slot, null);
                menu.addMenuClickHandler(slot, ChestMenuUtils.getEmptyClickHandler());
            }
        }
    }

    @ParametersAreNonnullByDefault
    private static void setupFooter(Player player, ChestMenu menu, int page, int totalPages) {
        for (int slot : FOOTER) {
            menu.replaceExistingItem(slot, ChestMenuUtils.getBackground());
            menu.addMenuClickHandler(slot, ChestMenuUtils.getEmptyClickHandler());
        }

        menu.replaceExistingItem(PAGE_PREVIOUS, ChestMenuUtils.getPreviousButton(player, page, totalPages));
        menu.addMenuClickHandler(PAGE_PREVIOUS, (p, slot, itemStack, clickAction) -> {
            final int previousPage = page - 1;
            if (previousPage >= 1) {
                displayList(p, menu, previousPage);
            }
            return false;
        });

        menu.replaceExistingItem(PAGE_NEXT, ChestMenuUtils.getNextButton(player, page, totalPages));
        menu.addMenuClickHandler(PAGE_NEXT, (p, slot, itemStack, clickAction) -> {
            final int nextPage = page + 1;
            if (nextPage <= totalPages) {
                displayList(p, menu, nextPage);
            }
            return false;
        });
    }

    @Nonnull
    private static ItemStack getTradeDisplayItem(@Nonnull TradeConfiguration tradeConfig) {
        Preconditions.checkArgument(tradeConfig != null, "TradeConfiguration cannot be null");

        return new CustomItemStack(
            Material.PAPER,
            getLine(Keys.TRADES_KEY, tradeConfig.getKey()),
            getLine(Keys.TRADES_OUTPUT, tradeConfig.getOutput().toShortString()),
            getLine(Keys.TRADES_INPUT_1, tradeConfig.getInput1().toShortString()),
            getLine(Keys.TRADES_INPUT_2, tradeConfig.getInput2().toShortString()),
            getLine(Keys.TRADES_EXP_REWARD, tradeConfig.isExpReward()),
            getLine(Keys.TRADES_EXP_VILLAGER, tradeConfig.getExpVillager()),
            getLine(Keys.TRADES_PRICE_MULTIPLIER, tradeConfig.getPriceMultiplier()),
            "",
            getLine("click-info")
        );
    }

    @Nonnull
    private static String getLine(String key) {
        return ChatUtil.color(VillagerTrade.getLocalization().getLang("menu.list." + key));
    }

    @Nonnull
    private static String getLine(String key, Object value) {
        return MenuUtils.format(VillagerTrade.getLocalization().getLang("menu.list." + key), value);
    }
}
