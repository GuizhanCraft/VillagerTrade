package net.guizhanss.villagertrade.core.commands.subcommands;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.guizhanss.villagertrade.VillagerTrade;
import net.guizhanss.villagertrade.core.commands.SubCommand;
import net.guizhanss.villagertrade.utils.constants.Keys;
import net.guizhanss.villagertrade.utils.constants.Permissions;

public abstract class AdminPlayerCommand extends SubCommand {

    private final String usage;

    @ParametersAreNonnullByDefault
    protected AdminPlayerCommand(String name, boolean hidden, String usage) {
        super(name, hidden);
        this.usage = usage;
    }

    @ParametersAreNonnullByDefault
    protected boolean canExecute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            VillagerTrade.getLocalization().sendKeyedMessage(sender, "no-console");
            return false;
        }
        if (!sender.hasPermission(Permissions.ADMIN)) {
            VillagerTrade.getLocalization().sendKeyedMessage(sender, "no-permission");
            return false;
        }

        String[] usageArgs = usage.split(" ");
        int argsLength = 1;
        for (String usageArg : usageArgs) {
            if (usageArg.startsWith("[")) {
                break;
            }
            argsLength++;
        }
        if (args.length != argsLength) {
            VillagerTrade.getLocalization().sendKeyedMessage(sender, "usage",
                msg -> msg.replace(Keys.VAR_USAGE, "/sfvt " + getName() + " " + usage));
            return false;
        }
        return true;
    }
}
