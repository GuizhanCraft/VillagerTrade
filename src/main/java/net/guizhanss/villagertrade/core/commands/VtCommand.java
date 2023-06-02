package net.guizhanss.villagertrade.core.commands;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.guizhanss.guizhanlib.minecraft.utils.ChatUtil;
import net.guizhanss.villagertrade.VillagerTrade;
import net.guizhanss.villagertrade.core.commands.subcommands.AddCommand;
import net.guizhanss.villagertrade.core.commands.subcommands.ConfirmCommand;
import net.guizhanss.villagertrade.core.commands.subcommands.EditCommand;
import net.guizhanss.villagertrade.core.commands.subcommands.ListCommand;
import net.guizhanss.villagertrade.core.commands.subcommands.ReloadCommand;
import net.guizhanss.villagertrade.core.commands.subcommands.RemoveCommand;
import net.guizhanss.villagertrade.core.commands.subcommands.SaveCustomCommand;

import lombok.Getter;

public final class VtCommand implements CommandExecutor {

    @Getter
    private final Set<SubCommand> subCommands = new HashSet<>();

    public VtCommand() {
        subCommands.add(new ListCommand());
        subCommands.add(new AddCommand());
        subCommands.add(new EditCommand());
        subCommands.add(new RemoveCommand());
        subCommands.add(new ReloadCommand());
        subCommands.add(new ConfirmCommand());
        subCommands.add(new SaveCustomCommand());
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
        sender.sendMessage(ChatUtil.color("&e&lVillagerTrade &6v" + VillagerTrade.getInstance().getPluginVersion()));
        for (SubCommand subCommand : subCommands) {
            if (subCommand.isHidden()) {
                continue;
            }
            sender.sendMessage(ChatUtil.color("&e/sfvt " + subCommand.getName() + "&7 - " + subCommand.getDescription()));
        }
    }
}
