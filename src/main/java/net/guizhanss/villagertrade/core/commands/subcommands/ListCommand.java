package net.guizhanss.villagertrade.core.commands.subcommands;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.guizhanss.villagertrade.implementation.menu.TradeListMenu;

public final class ListCommand extends AdminPlayerCommand {

    public ListCommand() {
        super("list", false, "");
    }

    @ParametersAreNonnullByDefault
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (!canExecute(sender, args)) {
            return;
        }

        new TradeListMenu((Player) sender);
    }
}
