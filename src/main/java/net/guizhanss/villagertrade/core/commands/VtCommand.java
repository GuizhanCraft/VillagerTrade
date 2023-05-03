package net.guizhanss.villagertrade.core.commands;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.guizhanss.villagertrade.VillagerTrade;
import net.guizhanss.villagertrade.core.commands.subcommands.EditCommand;
import net.guizhanss.villagertrade.core.commands.subcommands.ListCommand;
import net.guizhanss.villagertrade.core.commands.subcommands.ReloadCommand;

import lombok.Getter;

public final class VtCommand implements CommandExecutor {

    @Getter
    private final Set<SubCommand> subCommands = new HashSet<>();

    public VtCommand() {
        subCommands.add(new ListCommand());
        subCommands.add(new EditCommand());
        subCommands.add(new ReloadCommand());
    }

    @ParametersAreNonnullByDefault
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            for (SubCommand subCommand : subCommands) {
                if (subCommand.isSubCommand(args[0])) {
                    subCommand.onCommand(sender, args);
                    return true;
                }
            }
            sendHelp(sender);
        } else {
            sendHelp(sender);
        }
        return true;
    }

    public void sendHelp(@Nonnull CommandSender sender) {
        sender.sendMessage("&e&lVillagerTrade &6v" + VillagerTrade.getInstance().getPluginVersion());
        for (SubCommand subCommand : subCommands) {
            if (subCommand.isHidden()) {
                continue;
            }
            sender.sendMessage("&e/sfvt " + subCommand.getName() + " &7" + subCommand.getDescription());
        }
    }
}
