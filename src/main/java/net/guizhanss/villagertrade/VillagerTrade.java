package net.guizhanss.villagertrade;

import java.io.File;
import java.lang.reflect.Method;
import java.util.logging.Level;

import javax.annotation.Nonnull;

import org.bukkit.plugin.Plugin;

import io.github.thebusybiscuit.slimefun4.libraries.dough.updater.GitHubBuildsUpdater;

import net.guizhanss.guizhanlib.slimefun.addon.AbstractAddon;
import net.guizhanss.guizhanlib.updater.GuizhanBuildsUpdater;
import net.guizhanss.villagertrade.core.Registry;
import net.guizhanss.villagertrade.core.services.CustomItemService;
import net.guizhanss.villagertrade.core.services.LocalizationService;
import net.guizhanss.villagertrade.implementation.managers.CommandManager;
import net.guizhanss.villagertrade.implementation.managers.ConfigManager;
import net.guizhanss.villagertrade.implementation.managers.ListenerManager;
import net.guizhanss.villagertrade.implementation.managers.TaskManager;

import org.bstats.bukkit.Metrics;

public final class VillagerTrade extends AbstractAddon {

    private LocalizationService localizationService;
    private CustomItemService customItemService;
    private Registry registry;
    private ConfigManager configManager;
    private CommandManager commandManager;
    private ListenerManager listenerManager;
    private TaskManager taskManager;

    public VillagerTrade() {
        super("ybw0014", "VillagerTrade", "master", "auto-update");
    }

    @Nonnull
    public static Registry getRegistry() {
        return inst().registry;
    }

    @Nonnull
    public static ConfigManager getConfigManager() {
        return inst().configManager;
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
    public static TaskManager getTaskManager() {
        return inst().taskManager;
    }

    @Nonnull
    public static LocalizationService getLocalization() {
        return inst().localizationService;
    }

    @Nonnull
    public static CustomItemService getCustomItemService() {
        return inst().customItemService;
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

        registry = new Registry();
        localizationService = new LocalizationService(this);
        customItemService = new CustomItemService(this);
        configManager = new ConfigManager(this);
        commandManager = new CommandManager(this);
        listenerManager = new ListenerManager(this);
        taskManager = new TaskManager();

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
