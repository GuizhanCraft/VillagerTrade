package net.guizhanss.villagertrade;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;
import java.util.logging.Level;

import javax.annotation.Nonnull;

import net.guizhanss.villagertrade.api.TradeConfiguration;

import net.guizhanss.villagertrade.api.configurations.TraderType;

import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import io.github.thebusybiscuit.slimefun4.libraries.dough.updater.GitHubBuildsUpdater;

import net.guizhanss.guizhanlib.slimefun.addon.AbstractAddon;
import net.guizhanss.guizhanlib.updater.GuizhanBuildsUpdater;
import net.guizhanss.villagertrade.core.Registry;
import net.guizhanss.villagertrade.implementation.managers.CommandManager;
import net.guizhanss.villagertrade.implementation.managers.ConfigManager;
import net.guizhanss.villagertrade.implementation.managers.ListenerManager;

import org.bstats.bukkit.Metrics;

public final class VillagerTrade extends AbstractAddon {

    private ConfigManager configManager;
    private Registry registry;
    private CommandManager commandManager;
    private ListenerManager listenerManager;

    public VillagerTrade() {
        super("ybw0014", "VillagerTrade", "master", "auto-update");
    }

    @Nonnull
    public static ConfigManager getConfigManager() {
        return inst().configManager;
    }

    @Nonnull
    public static Registry getRegistry() {
        return inst().registry;
    }

    @Nonnull
    public static CommandManager getCommandManager() {
        return inst().commandManager;
    }

    @Nonnull
    public static ListenerManager getListenerManager() {
        return inst().listenerManager;
    }

    @Nonnull
    private static VillagerTrade inst() {
        return getInstance();
    }

    @Override
    public void enable() {
        log(Level.INFO, "====================");
        log(Level.INFO, "   VillagerTrade    ");
        log(Level.INFO, "     by ybw0014     ");
        log(Level.INFO, "====================");

        setupClassRegistrations();

        configManager = new ConfigManager(this);
        registry = new Registry();

        commandManager = new CommandManager();
        listenerManager = new ListenerManager();

        TradeConfiguration tradeConfiguration = TradeConfiguration.builder()
            .traderTypes(List.of(TraderType.valueOf(Villager.Profession.ARMORER)))
            .input1(new ItemStack(Material.COAL, 2))
            .input2(new ItemStack(Material.COAL, 2))
            .output(new ItemStack(Material.DIAMOND))
            .build();

        configManager.getTrades().set("test", tradeConfiguration);
        configManager.getTrades().save();

        setupMetrics();
    }

    @Override
    public void disable() {
        registry = null;
        configManager = null;
        listenerManager = null;
    }

    private void setupMetrics() {
        new Metrics(this, 18292);
    }

    private void setupClassRegistrations() {
        ConfigurationSerialization.registerClass(TradeConfiguration.class);
    }

    @Override
    protected void autoUpdate() {
        if (getPluginVersion().startsWith("DEV")) {
            String path = getGithubUser() + "/" + getGithubRepo() + "/" + getGithubBranch();
            new GitHubBuildsUpdater(this, getFile(), path).start();
        } else if (getPluginVersion().startsWith("Build")) {
            try {
                // use updater in lib plugin
                Class<?> clazz = Class.forName("net.guizhanss.guizhanlibplugin.updater.GuizhanUpdater");
                Method updaterStart = clazz.getDeclaredMethod("start", Plugin.class, File.class, String.class, String.class, String.class);
                updaterStart.invoke(null, this, getFile(), getGithubUser(), getGithubRepo(), getGithubBranch());
            } catch (Exception ignored) {
                // use updater in lib
                new GuizhanBuildsUpdater(this, getFile(), getGithubUser(), getGithubRepo(), getGithubBranch()).start();
            }
        }
    }
}
