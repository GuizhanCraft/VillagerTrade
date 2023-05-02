package net.guizhanss.villagertrade.core.services;

import net.guizhanss.guizhanlib.minecraft.utils.ChatUtil;

import net.guizhanss.guizhanlib.slimefun.addon.AddonConfig;

import net.guizhanss.villagertrade.VillagerTrade;

import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import java.util.function.UnaryOperator;

public final class LocalizationService {

    private static final String DEFAULT_PREFIX = "&f[&aVillagerTrade&f]&7 ";
    private static final String FILENAME_MESSAGES = "messages.yml";

    private final AddonConfig messages;

    private String prefix;

    public LocalizationService(VillagerTrade plugin) {
        messages = new AddonConfig(plugin, FILENAME_MESSAGES);

        afterReload();
    }

    public void reloadAll() {
        messages.reload();

        afterReload();
    }

    private void afterReload() {
        prefix = messages.getString("prefix", DEFAULT_PREFIX);
    }

    @Nonnull
    public String getCommandDescription(@Nonnull String command) {
        return messages.getString("commands." + command + ".description", "");
    }

    @ParametersAreNonnullByDefault
    public void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(ChatUtil.color(prefix + message));
    }

    @ParametersAreNonnullByDefault
    public void sendKeyedMessage(CommandSender sender, String key) {
        sendKeyedMessage(sender, key, msg -> msg);
    }

    @ParametersAreNonnullByDefault
    public void sendKeyedMessage(CommandSender sender, String key, UnaryOperator<String> function) {
        sendMessage(sender, function.apply(messages.getString(key)));
    }
}
