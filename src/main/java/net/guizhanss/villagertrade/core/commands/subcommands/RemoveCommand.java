package net.guizhanss.villagertrade.core.commands.subcommands;

import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.base.Preconditions;

import net.guizhanss.villagertrade.utils.constants.Keys;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.guizhanss.villagertrade.VillagerTrade;
import net.guizhanss.villagertrade.api.trades.TradeConfiguration;
import net.guizhanss.villagertrade.core.tasks.ConfirmationTask;
import net.guizhanss.villagertrade.implementation.menu.TradeListMenu;
import net.guizhanss.villagertrade.utils.constants.Permissions;

public final class RemoveCommand extends TradeKeyCompletionCommand {

    public RemoveCommand() {
        super("remove", false);
    }

    @ParametersAreNonnullByDefault
    public static void awaitRemoval(Player player, TradeConfiguration tradeConfig, Runnable callback) {
        Preconditions.checkArgument(player != null, "Player cannot be null");
        Preconditions.checkArgument(tradeConfig != null, "TradeConfiguration cannot be null");

        final String tradeKey = tradeConfig.getKey();
        VillagerTrade.getLocalization().sendKeyedMessage(player, "commands.remove.await-confirm",
            msg -> msg.replace(Keys.VAR_TRADE_KEY, tradeKey));
        ConfirmationTask.create(player.getUniqueId(), 30 * 1000L, () -> {
            VillagerTrade.getRegistry().clear(tradeConfig);
            VillagerTrade.getLocalization().sendKeyedMessage(player, "commands.remove.success",
                msg -> msg.replace(Keys.VAR_TRADE_KEY, tradeKey));
            // we need to close all open list
            TradeListMenu.closeAll();
            callback.run();
        });
    }

    @ParametersAreNonnullByDefault
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            VillagerTrade.getLocalization().sendKeyedMessage(sender, "no-console");
            return;
        }
        if (!sender.hasPermission(Permissions.ADMIN)) {
            VillagerTrade.getLocalization().sendKeyedMessage(sender, "no-permission");
            return;
        }
        if (args.length != 2) {
            VillagerTrade.getLocalization().sendKeyedMessage(sender, "usage",
                msg -> msg.replace(Keys.VAR_USAGE, "/sfvt remove <tradeKey>"));
            return;
        }

        String tradeKey = args[1];
        TradeConfiguration tradeConfig = VillagerTrade.getRegistry().getTradeConfigurations().get(tradeKey);
        if (tradeConfig == null) {
            VillagerTrade.getLocalization().sendKeyedMessage(sender, "commands.remove.not-found",
                msg -> msg.replace(Keys.VAR_TRADE_KEY, tradeKey));
            return;
        }
        awaitRemoval(player, tradeConfig, () -> {
        });
    }
}
