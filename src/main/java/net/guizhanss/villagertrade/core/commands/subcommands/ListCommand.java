package net.guizhanss.villagertrade.core.commands.subcommands;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.command.CommandSender;

import net.guizhanss.villagertrade.core.commands.SubCommand;

public final class ListCommand extends SubCommand {

    public ListCommand() {
        super("list", false);
    }

    @ParametersAreNonnullByDefault
    @Override
    public void onCommand(CommandSender sender, String[] args) {
    }
}
