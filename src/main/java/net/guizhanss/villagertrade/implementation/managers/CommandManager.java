package net.guizhanss.villagertrade.implementation.managers;

import javax.annotation.Nonnull;

import net.guizhanss.villagertrade.VillagerTrade;
import net.guizhanss.villagertrade.core.commands.VtCommand;
import net.guizhanss.villagertrade.core.commands.VtTabCompleter;

public final class CommandManager {

    public CommandManager(@Nonnull VillagerTrade plugin) {
        VtCommand vtCommand = new VtCommand();
        plugin.getCommand("villagertrade").setExecutor(vtCommand);
        plugin.getCommand("villagertrade").setTabCompleter(new VtTabCompleter(vtCommand));
    }
}
