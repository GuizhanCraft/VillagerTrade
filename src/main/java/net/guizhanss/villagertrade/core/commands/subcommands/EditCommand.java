package net.guizhanss.villagertrade.core.commands.subcommands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.guizhanss.villagertrade.VillagerTrade;
import net.guizhanss.villagertrade.api.trades.TradeConfiguration;
import net.guizhanss.villagertrade.core.commands.SubCommand;
import net.guizhanss.villagertrade.implementation.menu.TradeMenu;
import net.guizhanss.villagertrade.utils.constants.Permissions;

public final class EditCommand extends SubCommand {

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
                msg -> msg.replace("%usage%", "/sfvt edit <trade_id>"));
            return;
        }

        String tradeId = args[1];
        TradeConfiguration tradeConfig = VillagerTrade.getRegistry().getTradeConfigurations().get(tradeId);
        if (tradeConfig != null) {
            new TradeMenu(player, tradeConfig);
        } else {
            VillagerTrade.getLocalization().sendKeyedMessage(sender, "commands.edit.not-found",
                msg -> msg.replace("%tradeId%", tradeId));
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 2) {
            List<String> result = new ArrayList<>();
            for (Map.Entry<String, TradeConfiguration> entry : VillagerTrade.getRegistry().getTradeConfigurations().entrySet()) {
                final String key = entry.getKey();
                final TradeConfiguration config = entry.getValue();
                if (config.isExternalConfig()) {
                    continue;
                }
                if (key.startsWith(args[1])) {
                    result.add(key);
                }
            }
            return result;
        } else {
            return List.of();
        }
    }
}
