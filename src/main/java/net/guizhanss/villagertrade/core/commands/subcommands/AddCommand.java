package net.guizhanss.villagertrade.core.commands.subcommands;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.guizhanss.villagertrade.VillagerTrade;
import net.guizhanss.villagertrade.core.commands.SubCommand;
import net.guizhanss.villagertrade.implementation.menu.TradeMenu;
import net.guizhanss.villagertrade.utils.constants.Permissions;

public final class AddCommand extends SubCommand {

    public AddCommand() {
        super("add", false);
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
                msg -> msg.replace("%usage%", "/sfvt add <trade_id>"));
            return;
        }

        String tradeId = args[1];
        new TradeMenu(player, tradeId);
    }
}
