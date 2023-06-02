package net.guizhanss.villagertrade.core.commands.subcommands;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.guizhanss.villagertrade.VillagerTrade;
import net.guizhanss.villagertrade.core.commands.SubCommand;
import net.guizhanss.villagertrade.implementation.menu.TradeListMenu;
import net.guizhanss.villagertrade.utils.constants.Permissions;

public final class ListCommand extends SubCommand {

    public ListCommand() {
        super("list", false);
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
        if (args.length != 1) {
            VillagerTrade.getLocalization().sendKeyedMessage(sender, "usage",
                msg -> msg.replace("%usage%", "/sfvt edit <trade_key>"));
            return;
        }

        new TradeListMenu(player);
    }
}
