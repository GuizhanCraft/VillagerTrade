package net.guizhanss.villagertrade.core.commands.subcommands;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.guizhanss.villagertrade.implementation.menu.TradeMenu;

public final class AddCommand extends AdminPlayerCommand {

    public AddCommand() {
        super("add", false, "<tradeKey>");
    }

    @ParametersAreNonnullByDefault
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (!canExecute(sender, args)) {
            return;
        }

        String tradeKey = args[1];
        new TradeMenu((Player) sender, tradeKey);
    }
}
