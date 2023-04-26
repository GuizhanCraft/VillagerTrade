package net.guizhanss.villagertrade.core.commands;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class VtTabCompleter implements TabCompleter {
    private final VtCommand command;

    public VtTabCompleter(VtCommand command) {
        this.command = command;
    }

    @ParametersAreNonnullByDefault
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            return this.command.getSubCommands().stream()
                .map(SubCommand::getName)
                .toList();
        } else {
            return List.of();
        }
    }
}
