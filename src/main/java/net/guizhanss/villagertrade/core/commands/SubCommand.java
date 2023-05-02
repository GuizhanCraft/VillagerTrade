package net.guizhanss.villagertrade.core.commands;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.command.CommandSender;

import net.guizhanss.villagertrade.VillagerTrade;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

public abstract class SubCommand {

    @Getter
    private final String name;
    @Getter
    @Accessors(fluent = true)
    private final boolean isHidden;

    @Getter
    @Setter
    private String description;

    public SubCommand(@Nonnull String name, boolean hidden) {
        this.name = name;
        this.isHidden = hidden;
        this.description = VillagerTrade.getLocalizationService().getCommandDescription(name);
    }

    public boolean isSubCommand(@Nonnull String cmd) {
        return name.equalsIgnoreCase(cmd);
    }

    @ParametersAreNonnullByDefault
    public abstract void onCommand(CommandSender sender, String[] args);

    @ParametersAreNonnullByDefault
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return List.of();
    }
}
