package net.guizhanss.villagertrade.implementation.managers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.command.PluginCommand;

import net.guizhanss.villagertrade.VillagerTrade;
import net.guizhanss.villagertrade.core.commands.VtCommand;
import net.guizhanss.villagertrade.core.commands.VtTabCompleter;

public final class CommandManager {
    public CommandManager() {
        VtCommand vtCommand = new VtCommand();
        getCommand("villagertrade").setExecutor(vtCommand);
        getCommand("villagertrade").setTabCompleter(new VtTabCompleter(vtCommand));
    }

    @Nullable
    private PluginCommand getCommand(@Nonnull String cmd) {
        return VillagerTrade.getInstance().getCommand(cmd);
    }
}
