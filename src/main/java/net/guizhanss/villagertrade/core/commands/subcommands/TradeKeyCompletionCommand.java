package net.guizhanss.villagertrade.core.commands.subcommands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.command.CommandSender;

import net.guizhanss.villagertrade.VillagerTrade;
import net.guizhanss.villagertrade.api.trades.TradeConfiguration;
import net.guizhanss.villagertrade.core.commands.SubCommand;

/**
 * A command that display trade keys as tab completion.
 *
 * @author ybw0014
 */
abstract class TradeKeyCompletionCommand extends SubCommand {
    protected TradeKeyCompletionCommand(@Nonnull String name, boolean hidden) {
        super(name, hidden);
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
