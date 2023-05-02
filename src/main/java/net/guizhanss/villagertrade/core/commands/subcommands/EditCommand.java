package net.guizhanss.villagertrade.core.commands.subcommands;

import net.guizhanss.villagertrade.VillagerTrade;
import net.guizhanss.villagertrade.core.commands.SubCommand;

import org.bukkit.command.CommandSender;

import javax.annotation.ParametersAreNonnullByDefault;

public final class EditCommand extends SubCommand {

    public EditCommand() {
        super("edit", false);
    }

    @ParametersAreNonnullByDefault
    @Override
    public void onCommand(CommandSender sender, String[] args) {
    }
}
