package net.guizhanss.villagertrade.core.commands.subcommands;

import javax.annotation.ParametersAreNonnullByDefault;

import net.guizhanss.villagertrade.utils.constants.Keys;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.guizhanss.villagertrade.VillagerTrade;
import net.guizhanss.villagertrade.api.trades.TradeConfiguration;
import net.guizhanss.villagertrade.implementation.menu.TradeMenu;
import net.guizhanss.villagertrade.utils.constants.Permissions;

public final class EditCommand extends TradeKeyCompletionCommand {

    public EditCommand() {
        super("edit", false);
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
                msg -> msg.replace(Keys.VAR_USAGE, "/sfvt edit <tradeKey>"));
            return;
        }

        String tradeKey = args[1];
        TradeConfiguration tradeConfig = VillagerTrade.getRegistry().getTradeConfigurations().get(tradeKey);
        if (tradeConfig != null) {
            new TradeMenu(player, tradeConfig);
        } else {
            VillagerTrade.getLocalization().sendKeyedMessage(sender, "commands.edit.not-found",
                msg -> msg.replace(Keys.VAR_TRADE_KEY, tradeKey));
        }
    }
}
