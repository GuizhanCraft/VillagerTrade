package net.guizhanss.villagertrade.implementation.menu;

import java.util.List;
import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.base.Preconditions;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.ChatUtils;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;

import net.guizhanss.villagertrade.VillagerTrade;
import net.guizhanss.villagertrade.api.trades.TradeConfiguration;
import net.guizhanss.villagertrade.api.trades.TraderTypes;
import net.guizhanss.villagertrade.utils.Heads;
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
public final class TraderTypesMenu {
    // slots
    private static final int[] BACKGROUND = {
        18, 19, 21, 22, 23, 25, 26
    };
    private static final int CONFIRM_SLOT = 20;
    private static final int CANCEL_SLOT = 24;

    private final ChestMenu menu;
    private final Player player;
    private Consumer<TraderTypes> confirmCallback;
    private Runnable cancelCallback;

    private boolean wanderingTrader;
    private List<Villager.Profession> villagerProfessions;

    @ParametersAreNonnullByDefault
    private TraderTypesMenu(Player player, TraderTypes types, Consumer<TraderTypes> confirmCallback,
                            Runnable cancelCallback) {
        Preconditions.checkArgument(player != null, "Player cannot be null");
        Preconditions.checkArgument(types != null, "TraderTypes cannot be null");
        Preconditions.checkArgument(confirmCallback != null, "Confirm callback cannot be null");
        Preconditions.checkArgument(cancelCallback != null, "Cancel callback cannot be null");

        this.player = player;
        this.confirmCallback = confirmCallback;
        this.cancelCallback = cancelCallback;

        // cache the values
        wanderingTrader = types.hasWanderingTrader();
        villagerProfessions = types.getVillagerProfessions();

        // prepare menu
        menu = new ChestMenu(VillagerTrade.getLocalization().getString("menu.trader_types.title"));
        setupMenu();
        openMenu();
    }

    /**
     * Open the menu of the specified {@link TraderTypes} for {@link Player}.
     *
     * @param p
     *     The {@link Player} to open menu for.
     * @param types
     *     The {@link TraderTypes} to be edited.
     * @param confirmCallback
     *     The callback to be called when the player clicks the confirm button.
     * @param cancelCallback
     *     The callback to be called when the player clicks the cancel button, or close the menu.
     *
     * @return The {@link TraderTypesMenu} instance.
     */
    @ParametersAreNonnullByDefault
    public static TraderTypesMenu open(Player p, TraderTypes types, Consumer<TraderTypes> confirmCallback,
                                       Runnable cancelCallback) {
        return new TraderTypesMenu(p, types, confirmCallback, cancelCallback);
    }

    @ParametersAreNonnullByDefault
    private void setupMenu() {
        for (int slot : BACKGROUND) {
            menu.addItem(slot, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }

        menu.setEmptySlotsClickable(false);
        menu.setPlayerInventoryClickable(false);

        menu.addMenuOpeningHandler(SoundUtils::playOpenMenuSound);
        menu.addMenuCloseHandler(player -> cancelCallback.run());

        // wandering trader
        menu.addItem(0, getTraderItem(Heads.WANDERING_TRADER, "wandering_trader", wanderingTrader),
            (player1, slot, item, clickAction) -> {
                wanderingTrader = !wanderingTrader;
                menu.replaceExistingItem(slot, getTraderItem(Heads.WANDERING_TRADER, "wandering_trader",
                    wanderingTrader));
                return false;
            }
        );

        // villager professions
        int slot = 1;
        for (Villager.Profession profession : Villager.Profession.values()) {
            if (profession == Villager.Profession.NONE || profession == Villager.Profession.NITWIT) {
                continue;
            }
            menu.addItem(
                slot++,
                getTraderItem(
                    Heads.valueOf(profession.name()),
                    profession.name(),
                    villagerProfessions.contains(profession)
                ),
                (player, slot1, item, action) -> {
                    if (villagerProfessions.contains(profession)) {
                        villagerProfessions.remove(profession);
                    } else {
                        villagerProfessions.add(profession);
                    }
                    menu.replaceExistingItem(
                        slot1,
                        getTraderItem(
                            Heads.valueOf(profession.name()),
                            profession.name(),
                            villagerProfessions.contains(profession)
                        )
                    );
                    return false;
                }
            );
        }

        // confirm button
        menu.addItem(CONFIRM_SLOT, getConfirmItem(), (player, slot1, item, action) -> {
            confirmCallback.accept(new TraderTypes(wanderingTrader, villagerProfessions));
            return false;
        });

        // cancel button
        menu.addItem(CANCEL_SLOT, getCancelItem(), (player, slot1, item, action) -> {
            cancelCallback.run();
            return false;
        });
    }

    private void openMenu() {
        menu.open(player);
    }

    @Nonnull
    private ItemStack getTraderItem(Heads hash, String name, boolean enabled) {
        return new CustomItemStack(
            hash.getItem(),
            ChatColor.YELLOW + ChatUtils.humanize(name) + ChatColor.GRAY + ": "
                + (enabled ? Strings.CHECK : Strings.CROSS)
        );
    }

    @Nonnull
    private ItemStack getConfirmItem() {
        return new CustomItemStack(
            Material.GREEN_STAINED_GLASS_PANE,
            VillagerTrade.getLocalization().getString("menu.trader_types.confirm.name"),
            VillagerTrade.getLocalization().getStringList("menu.trader_types.confirm.lore")
        );
    }

    @Nonnull
    private ItemStack getCancelItem() {
        return new CustomItemStack(
            Material.RED_STAINED_GLASS_PANE,
            VillagerTrade.getLocalization().getString("menu.trader_types.cancel.name"),
            VillagerTrade.getLocalization().getStringList("menu.trader_types.cancel.lore")
        );
    }
}
