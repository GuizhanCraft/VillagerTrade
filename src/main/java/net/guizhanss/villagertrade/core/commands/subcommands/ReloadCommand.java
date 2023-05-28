package net.guizhanss.villagertrade.core.commands.subcommands;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.command.CommandSender;

import net.guizhanss.villagertrade.VillagerTrade;
import net.guizhanss.villagertrade.core.commands.SubCommand;
import net.guizhanss.villagertrade.utils.constants.Permissions;

public final class ReloadCommand extends SubCommand {

    public ReloadCommand() {
        super("reload", false);
    }

    @ParametersAreNonnullByDefault
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission(Permissions.ADMIN)) {
            VillagerTrade.getLocalization().sendKeyedMessage(sender, "no-permission");
            return;
        }
        VillagerTrade.getRegistry().reset();
        VillagerTrade.getLocalization().reloadAll();
        VillagerTrade.getCustomItemService().reload();
        VillagerTrade.getConfigManager().reloadAll();
        VillagerTrade.getLocalization().sendKeyedMessage(sender, "commands.reload.success");
    }
}
