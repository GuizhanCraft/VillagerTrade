package net.guizhanss.villagertrade.core.commands.subcommands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.command.CommandSender;

import net.guizhanss.villagertrade.VillagerTrade;
import net.guizhanss.villagertrade.api.trades.TradeConfiguration;

interface TradeKeyCompletion {
    @Nonnull
    @ParametersAreNonnullByDefault
    default List<String> tabComplete(CommandSender sender, String[] args, int index) {
        if (args.length == index + 1) {
            List<String> result = new ArrayList<>();
            for (Map.Entry<String, TradeConfiguration> entry : VillagerTrade.getRegistry().getTradeConfigurations().entrySet()) {
                final String key = entry.getKey();
                final TradeConfiguration config = entry.getValue();
                if (config.isExternalConfig()) {
                    continue;
                }
                if (key.startsWith(args[index])) {
                    result.add(key);
                }
            }
            return result;
        } else {
            return List.of();
        }
    }
}
