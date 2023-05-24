package net.guizhanss.villagertrade.implementation.menu;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.base.Preconditions;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.ChatUtils;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;

import net.guizhanss.villagertrade.VillagerTrade;
import net.guizhanss.villagertrade.api.trades.TradeConfiguration;
import net.guizhanss.villagertrade.api.trades.TradeItem;
import net.guizhanss.villagertrade.api.trades.TraderTypes;
import net.guizhanss.villagertrade.api.trades.mutables.MutableTradeItem;
import net.guizhanss.villagertrade.utils.MenuUtils;
import net.guizhanss.villagertrade.utils.SoundUtils;
import net.guizhanss.villagertrade.utils.constants.Strings;

import lombok.AccessLevel;
import lombok.Setter;

/**
 * The menu to edit a {@link TradeConfiguration}.
 *
 * @author ybw0014
 */
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

    private final ChestMenu menu;
    private final Player player;
    // ticking handlers, the only argument is slot
    private final Map<Integer, Consumer<Integer>> tickingHandlers = new HashMap<>();

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
        Preconditions.checkArgument(player != null, "Player cannot be null");
        Preconditions.checkArgument(trade != null, "TradeConfiguration cannot be null");

        this.player = player;
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
        menu = new ChestMenu(VillagerTrade.getLocalization().getString("menu.trade.title"));
        setupMenu();
        openMenu();
    }

    /**
     * Open the menu of the specified {@link TradeConfiguration} for {@link Player}.
     *
     * @param p
     *     The {@link Player} to open menu for.
     * @param trade
     *     The {@link TradeConfiguration} to be edited.
     *
     * @return The {@link TradeMenu} instance.
     */
    @ParametersAreNonnullByDefault
    public static TradeMenu open(Player p, TradeConfiguration trade) {
        return new TradeMenu(p, trade);
    }

    @ParametersAreNonnullByDefault
    private void setupMenu() {
        for (int slot : BACKGROUND) {
            menu.addItem(slot, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }

        menu.setEmptySlotsClickable(true);
        menu.setPlayerInventoryClickable(true);

        menu.addMenuOpeningHandler(player -> {
            TradeMenuTask.addPlayer(player.getUniqueId(), this);
            SoundUtils.playOpenMenuSound(player);
        });
        menu.addMenuCloseHandler(player -> {
            TradeMenuTask.removePlayer(player.getUniqueId());
        });

        // back button
        menu.addItem(BACK_SLOT, getBackButton(player), (player, slot, item, action) -> {
            TradeListMenu.open(player);
            return false;
        });

        // info button
        menu.addItem(INFO_SLOT, getInfoButton(), ChestMenuUtils.getEmptyClickHandler());

        // items
        addTradeItemSlots(INPUT_1_INFO_SLOT, INPUT_1_ITEM_SLOT, INPUT_1_AMOUNT_SLOT,
            Material.CYAN_STAINED_GLASS_PANE, "input1", input1);
        addTradeItemSlots(INPUT_2_INFO_SLOT, INPUT_2_ITEM_SLOT, INPUT_2_AMOUNT_SLOT,
            Material.CYAN_STAINED_GLASS_PANE, "input2", input2);
        addTradeItemSlots(OUTPUT_INFO_SLOT, OUTPUT_ITEM_SLOT, OUTPUT_AMOUNT_SLOT,
            Material.ORANGE_STAINED_GLASS_PANE, "output", output);

        // trader types
        menu.addItem(TRADER_TYPES_SLOT, getTraderTypesButton(), (player, slot, item, action) -> {
            TraderTypesMenu.open(player, traderTypes, (newTraderTypes) -> {
                traderTypes = newTraderTypes;
                menu.replaceExistingItem(TRADER_TYPES_SLOT, getTraderTypesButton());
                openMenu();
            }, () -> {
                openMenu();
            });
            return false;
        });


    }

    void tick() {
        for (Map.Entry<Integer, Consumer<Integer>> entry : tickingHandlers.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }
            entry.getValue().accept(entry.getKey());
        }
    }

    private void openMenu() {
        menu.open(player);
    }

    private void addTradeItemSlots(int infoSlot, int itemSlot, int amountSlot,
                                   Material infoMaterial, String key, MutableTradeItem tradeItem) {
        // add 3 items: info, item, amount
        menu.addItem(infoSlot, getItemInfoButton(infoMaterial, key, tradeItem), ChestMenuUtils.getEmptyClickHandler());
        menu.addItem(itemSlot, tradeItem.getItem());
        tickingHandlers.put(itemSlot, (slot) -> {
            ItemStack item = menu.getItemInSlot(slot);
            // we don't want to update the menu if the item is the same
            if (tradeItem.isItem(item)) {
                return;
            }

            // if item is updated, then update the menu
            tradeItem.setItem(item);
            refreshTradeItemSlots(infoSlot, itemSlot, amountSlot, infoMaterial, key, tradeItem);

        });
        menu.addItem(amountSlot, getItemAmountButton(tradeItem), (player, slot, item, action) -> {
            // left click +, right click -, shift = specify (input)
            if (tradeItem.getType() == TradeItem.TradeItemType.NONE) {
                return false;
            }
            if (action.isShiftClicked()) {
                inputAmount(tradeItem, amount -> {
                    openMenu();
                    // we only update when the input is not cancelled
                    if (amount.isPresent()) {
                        tradeItem.setAmount(amount.get());
                        refreshTradeItemSlots(infoSlot, itemSlot, amountSlot, infoMaterial, key, tradeItem);
                    }
                });
            } else {
                if (action.isRightClicked()) {
                    tradeItem.setAmount(tradeItem.getAmount() - 1);
                } else {
                    tradeItem.setAmount(tradeItem.getAmount() + 1);
                }
                refreshTradeItemSlots(infoSlot, itemSlot, amountSlot, infoMaterial, key, tradeItem);
            }
            return false;
        });
    }

    private void refreshTradeItemSlots(int infoSlot, int itemSlot, int amountSlot,
                                       Material infoMaterial, String key, MutableTradeItem tradeItem) {
        menu.replaceExistingItem(infoSlot, getItemInfoButton(infoMaterial, key, tradeItem));
        menu.replaceExistingItem(itemSlot, tradeItem.getItem());
        menu.replaceExistingItem(amountSlot, getItemAmountButton(tradeItem));
    }

    /**
     * This method ask player to input amount of item. It is not guarenteed that the amount is valid.
     *
     * @param tradeItem
     *     The trade item to be updated.
     * @param callback
     *     The callback to be called when the amount is inputted.
     *     The {@link Optional} is empty when the player cancels the input.
     */
    @ParametersAreNonnullByDefault
    private void inputAmount(MutableTradeItem tradeItem, Consumer<Optional<Integer>> callback) {
        player.closeInventory();
        VillagerTrade.getLocalization().sendKeyedMessage(player, "menu.trade.amount.input",
            msg -> msg.replace("%itemInfo%", tradeItem.toShortString(false))
                .replace("%itemAmount%", String.valueOf(tradeItem.getAmount()))
        );
        MenuUtils.awaitInput(player, (playerInput) -> {
            if (playerInput.equalsIgnoreCase("cancel")) {
                callback.accept(Optional.empty());
                return;
            }
            int amount;
            try {
                amount = Integer.parseInt(playerInput);
                callback.accept(Optional.of(amount));
            } catch (NumberFormatException e) {
                VillagerTrade.getLocalization().sendKeyedMessage(player, "menu.trade.amount.not-number");
                inputAmount(tradeItem, callback);
            }
        });
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
                "%itemId%", item.getId() != null ? item.getId() : "N/A",
                "%itemAmount%", String.valueOf(item.getAmount())
            )
        );
    }

    @Nonnull
    private ItemStack getItemAmountButton(MutableTradeItem item) {
        return MenuUtils.parseVariables(
            new CustomItemStack(
                Material.BOOK,
                VillagerTrade.getLocalization().getString("menu.trade.amount.name"),
                VillagerTrade.getLocalization().getStringList("menu.trade.amount.lore")
            ),
            Map.of(
                "%amount%", String.valueOf(item.getAmount())
            )
        );
    }

    @Nonnull
    private ItemStack getTraderTypesButton() {
        return MenuUtils.parseVariables(
            new CustomItemStack(
                Material.NAME_TAG,
                VillagerTrade.getLocalization().getString("menu.trade.trader_types.name"),
                VillagerTrade.getLocalization().getStringList("menu.trade.trader_types.lore")
            ),
            Map.of(
                "%wanderingTrader%", traderTypes.hasWanderingTrader() ? Strings.CHECK : Strings.CROSS,
                "%villagers%", traderTypes.getVillagerProfessions().stream()
                    .map(profession -> ChatUtils.humanize(profession.toString()))
                    .collect(Collectors.joining(", "))
            )
        );
    }
}
