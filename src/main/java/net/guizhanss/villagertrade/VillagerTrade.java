package net.guizhanss.villagertrade;

import java.io.File;
import java.lang.reflect.Method;

import org.bukkit.plugin.Plugin;

import io.github.thebusybiscuit.slimefun4.libraries.dough.updater.GitHubBuildsUpdater;

import net.guizhanss.guizhanlib.slimefun.addon.AbstractAddon;
import net.guizhanss.guizhanlib.slimefun.addon.AddonConfig;
import net.guizhanss.guizhanlib.updater.GuizhanBuildsUpdater;

import org.bstats.bukkit.Metrics;

public final class VillagerTrade extends AbstractAddon {

    public VillagerTrade() {
        super("ybw0014", "VillagerTrade", "master", "auto-update");
    }

    private static VillagerTrade inst() {
        return getInstance();
    }

    @Override
    public void enable() {
        setupMetrics();

        AddonConfig config = getAddonConfig();
    }

    @Override
    public void disable() {
    }

    private void setupMetrics() {
        new Metrics(this, 18292);
    }

    @Override
    protected void autoUpdate() {
        if (getDescription().getVersion().startsWith("DEV")) {
            String path = getGithubUser() + "/" + getGithubRepo() + "/" + getGithubBranch();
            new GitHubBuildsUpdater(this, getFile(), path).start();
        } else if (getDescription().getVersion().startsWith("Build")) {
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
