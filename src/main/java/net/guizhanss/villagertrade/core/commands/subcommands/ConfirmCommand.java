package net.guizhanss.villagertrade.core.commands.subcommands;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.guizhanss.villagertrade.VillagerTrade;
import net.guizhanss.villagertrade.core.tasks.ConfirmationTask;

public final class ConfirmCommand extends AdminPlayerCommand {

    public ConfirmCommand() {
        super("confirm", false, "");
    }

    @ParametersAreNonnullByDefault
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (!canExecute(sender, args)) {
            return;
        }

        final Player player = (Player) sender;
        final ConfirmationTask task = VillagerTrade.getRegistry().getConfirmationTasks().get(player.getUniqueId());

        if (task == null || !task.execute()) {
            VillagerTrade.getLocalization().sendKeyedMessage(sender, "commands.confirm.no-active");
        }
    }
}
