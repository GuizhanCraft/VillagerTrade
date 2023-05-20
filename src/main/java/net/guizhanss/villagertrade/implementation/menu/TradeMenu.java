package net.guizhanss.villagertrade.implementation.menu;


import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.base.Preconditions;

import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;

import lombok.AccessLevel;

import lombok.Setter;

import net.guizhanss.villagertrade.api.trades.TradeItem;
import net.guizhanss.villagertrade.implementation.menu.objects.MutableTradeItem;
import net.guizhanss.villagertrade.utils.MenuUtils;
import net.guizhanss.villagertrade.utils.SoundUtils;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;

import net.guizhanss.villagertrade.VillagerTrade;
import net.guizhanss.villagertrade.api.trades.TradeConfiguration;
import net.guizhanss.villagertrade.api.trades.TraderTypes;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@Setter(AccessLevel.PRIVATE)
@SuppressWarnings("deprecation")
public final class TradeMenu {
    // slots
    private static final int[] BACKGROUND = {
        0, 2, 3, 4, 5, 6, 8,
        18, 19, 20, 21, 22, 23, 24, 25, 26,
        27, 28, 34, 35,
        36, 37, 38, 39, 41, 42, 43, 44
    };
    private static final int BACK_SLOT = 1;
    private static final int INFO_SLOT = 7;
    private static final int SAVE_SLOT = 40;
    private static final int INPUT_1_INFO_SLOT = 9;
    private static final int INPUT_1_ITEM_SLOT = 10;
    private static final int INPUT_1_AMOUNT_SLOT = 11;
    private static final int INPUT_2_INFO_SLOT = 12;
    private static final int INPUT_2_ITEM_SLOT = 13;
    private static final int INPUT_2_AMOUNT_SLOT = 14;
    private static final int OUTPUT_INFO_SLOT = 15;
    private static final int OUTPUT_ITEM_SLOT = 16;
    private static final int OUTPUT_AMOUNT_SLOT = 17;
    private static final int TRADER_TYPES_SLOT = 29;
    private static final int MAX_USES_SLOT = 30;
    private static final int EXP_REWARD_SLOT = 31;
    private static final int EXP_VILLAGER_SLOT = 32;
    private static final int PRICE_MULTIPLIER_SLOT = 33;

    private static final Map<Player, Boolean> OPEN_MAP = new HashMap<>();

    private TradeConfiguration originalConfig;

    private TraderTypes traderTypes;
    private MutableTradeItem input1;
    private MutableTradeItem input2;
    private MutableTradeItem output;
    private int maxUses;
    private boolean expReward;
    private int expVillager;
    private float priceMultiplier;

    @ParametersAreNonnullByDefault
    private TradeMenu(Player player, TradeConfiguration trade) {
        Preconditions.checkArgument(trade != null, "TradeConfiguration cannot be null");

        originalConfig = trade;

        // cache the values
        traderTypes = trade.getTraderTypes();
        input1 = new MutableTradeItem(trade.getInput1());
        input2 = new MutableTradeItem(trade.getInput2());
        output = new MutableTradeItem(trade.getOutput());
        maxUses = trade.getMaxUses();
        expReward = trade.isExpReward();
        expVillager = trade.getExpVillager();
        priceMultiplier = trade.getPriceMultiplier();

        // prepare menu
        final ChestMenu menu = new ChestMenu(VillagerTrade.getLocalization().getString("menu.trade.title"));
        setupMenu(menu, player);
        menu.open(player);
    }

    @ParametersAreNonnullByDefault
    public static void open(Player p, TradeConfiguration trade) {
        new TradeMenu(p, trade);
    }

    @ParametersAreNonnullByDefault
    private void setupMenu(ChestMenu menu, Player p) {
        for (int slot : BACKGROUND) {
            menu.addItem(slot, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }

        menu.setEmptySlotsClickable(true);
        menu.setPlayerInventoryClickable(true);

        menu.addMenuOpeningHandler(player -> {
            OPEN_MAP.put(player, true);
            SoundUtils.playOpenMenuSound(player);
        });
        menu.addMenuCloseHandler(OPEN_MAP::remove);

        // back button
        menu.addItem(BACK_SLOT, getBackButton(p), (player, slot, item, action) -> {
            TradeListMenu.open(p);
            return false;
        });

        // info button
        menu.addItem(INFO_SLOT, getInfoButton(), ChestMenuUtils.getEmptyClickHandler());

        // input 1
        addTradeItemSlots(menu, INPUT_1_INFO_SLOT, INPUT_1_ITEM_SLOT, INPUT_1_AMOUNT_SLOT,
            Material.CYAN_STAINED_GLASS_PANE, "input1", input1);
    }

    private void addTradeItemSlots(ChestMenu menu, int infoSlot, int itemSlot, int amountSlot,
                                   Material infoMaterial, String key, MutableTradeItem tradeItem) {
        menu.addItem(infoSlot, getItemInfoButton(infoMaterial, key, tradeItem), ChestMenuUtils.getEmptyClickHandler());
        menu.addItem(itemSlot, tradeItem.getItem(), (player, slot, item, action) -> {
            tradeItem.setItem(menu.getItemInSlot(slot));
            refreshTradeItemSlots(menu, infoSlot, itemSlot, amountSlot, infoMaterial, key, tradeItem);
            return true;
        });
        menu.addItem(amountSlot, getItemAmountButton(tradeItem), (player, slot, item, action) -> {
            // left click +, right click -, shift = specify (input)
            if (tradeItem.getType() == TradeItem.TradeItemType.NONE) {
                return false;
            }
            if (action.isShiftClicked()) {

            } else {
                if (action.isRightClicked()) {
                    tradeItem.setAmount(tradeItem.getAmount() - 1);
                } else {
                    tradeItem.setAmount(tradeItem.getAmount() + 1);
                }
                refreshTradeItemSlots(menu, infoSlot, itemSlot, amountSlot, infoMaterial, key, tradeItem);
            }
            return false;
        });
    }

    private void refreshTradeItemSlots(ChestMenu menu, int infoSlot, int itemSlot, int amountSlot,
                                       Material infoMaterial, String key, MutableTradeItem tradeItem) {
        menu.replaceExistingItem(infoSlot, getItemInfoButton(infoMaterial, key, tradeItem));
        menu.replaceExistingItem(itemSlot, tradeItem.getItem());
        menu.replaceExistingItem(amountSlot, getItemAmountButton(tradeItem));
    }

    @Nonnull
    private ItemStack getBackButton(@Nonnull Player p) {
        return ChestMenuUtils.getBackButton(
            p,
            VillagerTrade.getLocalization().getStringList("menu.trade.back.lore").toArray(new String[0])
        );
    }

    @Nonnull
    private ItemStack getInfoButton() {
        return MenuUtils.parseVariables(
            new CustomItemStack(
                Material.NAME_TAG,
                VillagerTrade.getLocalization().getString("menu.trade.info.name"),
                VillagerTrade.getLocalization().getStringList("menu.trade.info.lore")
            ),
            Map.of(
                "%tradeId%", originalConfig.getKey()
            )
        );
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    private ItemStack getItemInfoButton(Material material, String key, MutableTradeItem item) {
        return MenuUtils.parseVariables(
            new CustomItemStack(
                material,
                VillagerTrade.getLocalization().getString("menu.trade." + key + ".name"),
                VillagerTrade.getLocalization().getStringList("menu.trade." + key + ".lore")
            ),
            Map.of(
                "%itemType%", item.getType().toString(),
                "%itemId%", item.getId() != null ? item.getId() : "null",
                "%itemAmount%", String.valueOf(item.getAmount())
            )
        );
    }

    @Nonnull
    private ItemStack getItemAmountButton(MutableTradeItem item) {
        return MenuUtils.parseVariables(
                new CustomItemStack(
                Material.NAME_TAG,
                VillagerTrade.getLocalization().getString("menu.trade.amount.name"),
                VillagerTrade.getLocalization().getStringList("menu.trade.amount.lore")
            ),
            Map.of(
                "%amount%", String.valueOf(item.getAmount())
            )
        );
    }
}
