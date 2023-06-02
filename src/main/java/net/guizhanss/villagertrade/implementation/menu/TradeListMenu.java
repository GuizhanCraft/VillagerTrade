package net.guizhanss.villagertrade.implementation.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.base.Preconditions;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;

import net.guizhanss.villagertrade.VillagerTrade;
import net.guizhanss.villagertrade.api.trades.TradeConfiguration;
import net.guizhanss.villagertrade.utils.ItemUtils;
import net.guizhanss.villagertrade.utils.MenuUtils;
import net.guizhanss.villagertrade.utils.SoundUtils;
import net.guizhanss.villagertrade.utils.Validators;
import net.guizhanss.villagertrade.utils.constants.Keys;

@SuppressWarnings("deprecation")
public final class TradeListMenu {
    // slots
    private static final int[] HEADER = { 0, 1, 2, 3, 4, 5, 6, 8 };
    private static final int[] FOOTER = { 45, 47, 48, 49, 50, 51, 53 };
    private static final int INFO_SLOT = 1;
    private static final int ADD_SLOT = 7;
    private static final int PAGE_SIZE = 36;
    private static final int PAGE_PREVIOUS = 46;
    private static final int PAGE_NEXT = 52;

    private static final String LANG_MENU = "menu.list.";

    private static final Map<UUID, Boolean> OPEN_MAP = new HashMap<>();

    private final Player player;
    private final ChestMenu menu;

    public TradeListMenu(@Nonnull Player player) {
        this.player = player;
        menu = new ChestMenu(VillagerTrade.getLocalization().getString("menu.list.title"));
        setupMenu();
        displayPage(1);
        menu.open(player);
    }

    public static void closeAll() {
        for (UUID uuid : OPEN_MAP.keySet()) {
            final Player p = Bukkit.getPlayer(uuid);
            if (p != null) {
                p.closeInventory();
            }
            OPEN_MAP.remove(uuid);
        }
    }

    private void setupMenu() {
        for (int slot : HEADER) {
            menu.addItem(slot, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }
        for (int slot : FOOTER) {
            menu.addItem(slot, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }

        menu.setEmptySlotsClickable(false);

        menu.addMenuOpeningHandler(p -> OPEN_MAP.put(p.getUniqueId(), true));

        menu.addMenuCloseHandler(p -> OPEN_MAP.remove(p.getUniqueId()));

        // info
        menu.addItem(INFO_SLOT, getInfoItem(), ChestMenuUtils.getEmptyClickHandler());

        // add
        menu.addItem(ADD_SLOT, getAddItem(), ChestMenuUtils.getEmptyClickHandler());
    }

    private void displayPage(int page) {
        final List<TradeConfiguration> trades =
            new ArrayList<>(VillagerTrade.getRegistry().getTradeConfigurations().values());
        final int total = trades.size();
        final int totalPages = (int) Math.ceil(total / (double) PAGE_SIZE);
        final int start = (page - 1) * PAGE_SIZE;
        final int end = Math.min(start + PAGE_SIZE, total);

        final List<TradeConfiguration> subList = trades.subList(start, end);

        // header
        // add
        menu.addMenuClickHandler(ADD_SLOT, (p, slot, item, action) -> {
            p.closeInventory();
            VillagerTrade.getLocalization().sendKeyedMessage(p, LANG_MENU + "add");
            MenuUtils.awaitInput(p, Validators::notEmpty, (input) -> {
                new TradeMenu(p, input);
            });
            return false;
        });

        // footer
        setupFooter(page, totalPages);

        // sound
        SoundUtils.playOpenMenuSound(player);

        for (int i = 0; i < PAGE_SIZE; i++) {
            final int slot = i + 9;
            if (i + 1 <= subList.size()) {
                final TradeConfiguration trade = subList.get(i);
                menu.replaceExistingItem(slot, getTradeDisplayItem(trade));
                menu.addMenuClickHandler(slot, (p, slot1, item, action) -> {
                    if (!trade.isExternalConfig()) {
                        new TradeMenu(p, trade);
                    }
                    return false;
                });
            } else {
                menu.replaceExistingItem(slot, null);
                menu.addMenuClickHandler(slot, ChestMenuUtils.getEmptyClickHandler());
            }
        }
    }

    @ParametersAreNonnullByDefault
    private void setupFooter(int page, int totalPages) {
        for (int slot : FOOTER) {
            menu.replaceExistingItem(slot, ChestMenuUtils.getBackground());
            menu.addMenuClickHandler(slot, ChestMenuUtils.getEmptyClickHandler());
        }

        menu.replaceExistingItem(PAGE_PREVIOUS, ChestMenuUtils.getPreviousButton(player, page, totalPages));
        menu.addMenuClickHandler(PAGE_PREVIOUS, (p, slot, itemStack, clickAction) -> {
            final int previousPage = page - 1;
            if (previousPage >= 1) {
                displayPage(previousPage);
            }
            return false;
        });

        menu.replaceExistingItem(PAGE_NEXT, ChestMenuUtils.getNextButton(player, page, totalPages));
        menu.addMenuClickHandler(PAGE_NEXT, (p, slot, itemStack, clickAction) -> {
            final int nextPage = page + 1;
            if (nextPage <= totalPages) {
                displayPage(nextPage);
            }
            return false;
        });
    }

    @Nonnull
    private ItemStack getTradeDisplayItem(@Nonnull TradeConfiguration tradeConfig) {
        Preconditions.checkArgument(tradeConfig != null, "TradeConfiguration cannot be null");

        ItemStack item = MenuUtils.parseVariables(
            getItem(Material.PAPER, "trade"),
            Map.of(
                "%tradeKey%", tradeConfig.getKey(),
                "%traderTypes%", tradeConfig.getTraderTypes().toHumanizedString(),
                "%input1%", tradeConfig.getInput1().toShortString(),
                "%input2%", tradeConfig.getInput2().toShortString(),
                "%output%", tradeConfig.getOutput().toShortString(),
                "%maxUses%", String.valueOf(tradeConfig.getMaxUses()),
                "%expReward%", String.valueOf(tradeConfig.isExpReward()),
                "%expVillager%", String.valueOf(tradeConfig.getExpVillager()),
                "%priceMultiplier%", String.valueOf(tradeConfig.getPriceMultiplier())
            )
        );

        String pathPrefix = LANG_MENU + "trade.lore-extra.";
        return ItemUtils.addLore(
            item,
            VillagerTrade.getLocalization().getStringList(
                tradeConfig.isExternalConfig() ? pathPrefix + "not-editable" : pathPrefix + "editable"
            )
        );
    }

    @Nonnull
    private ItemStack getInfoItem() {
        return getItem(Material.ENCHANTED_BOOK, "info");
    }

    @Nonnull
    private ItemStack getAddItem() {
        return getItem(Material.WRITABLE_BOOK, "add");
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    private ItemStack getItem(Material material, String key) {
        return new CustomItemStack(
            material,
            VillagerTrade.getLocalization().getString(LANG_MENU + key + "." + Keys.ITEM_NAME),
            VillagerTrade.getLocalization().getStringList(LANG_MENU + key + "." + Keys.ITEM_LORE)
        );
    }
}
