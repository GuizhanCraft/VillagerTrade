package net.guizhanss.villagertrade.core.commands.subcommands;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.guizhanss.villagertrade.VillagerTrade;

public final class SaveCustomCommand extends AdminPlayerCommand {

    public SaveCustomCommand() {
        super("savecustom", false, "<itemId>");
    }

    @ParametersAreNonnullByDefault
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (!canExecute(sender, args)) {
            return;
        }

        final Player player = (Player) sender;
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType().isAir()) {
            VillagerTrade.getLocalization().sendKeyedMessage(sender, "commands.savecustom.no-item");
            return;
        }

        if (!item.hasItemMeta()) {
            VillagerTrade.getLocalization().sendKeyedMessage(sender, "commands.savecustom.vanilla-item");
            return;
        }

        String itemId = args[1];

        if (VillagerTrade.getCustomItemService().getItem(itemId) != null) {
            VillagerTrade.getLocalization().sendKeyedMessage(sender, "commands.savecustom.id-exists",
                msg -> msg.replace("%itemId%", itemId));
            return;
        }
        String existingId = VillagerTrade.getCustomItemService().getId(item);
        if (existingId != null) {
            VillagerTrade.getLocalization().sendKeyedMessage(sender, "commands.savecustom.item-exists",
                msg -> msg.replace("%itemId%", existingId));
            return;
        }

        VillagerTrade.getCustomItemService().addItem(itemId, item);
        VillagerTrade.getLocalization().sendKeyedMessage(sender, "commands.savecustom.success",
            msg -> msg.replace("%itemId%", itemId));
    }
}
