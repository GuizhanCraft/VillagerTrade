package net.guizhanss.villagertrade.core.commands;

import java.util.List;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class VtTabCompleter implements TabCompleter {

    private final VtCommand command;

    public VtTabCompleter(VtCommand command) {
        this.command = command;
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return this.command.getSubCommands().stream()
                .filter(subCommand -> !subCommand.isHidden())
                .map(SubCommand::getName)
                .toList();
        } else if (args.length > 1) {
            for (SubCommand subCommand : this.command.getSubCommands()) {
                if (subCommand.isSubCommand(args[0])) {
                    return subCommand.onTabComplete(sender, args);
                }
            }
            return List.of();
        } else {
            return null;
        }
    }
}
