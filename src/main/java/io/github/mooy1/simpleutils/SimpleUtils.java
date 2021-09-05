package io.github.mooy1.simpleutils;

import java.io.File;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;

import io.github.mooy1.infinitylib.core.AbstractAddon;
import io.github.mooy1.infinitylib.metrics.bukkit.Metrics;
import io.github.mooy1.infinitylib.metrics.charts.SimplePie;
import io.github.mooy1.simpleutils.implementation.Items;

public final class SimpleUtils extends AbstractAddon {

    public SimpleUtils(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file,
                "Mooy1", "SimpleUtils", "master", "auto-update");
    }

    public SimpleUtils() {
        super("Mooy1", "SimpleUtils", "master", "auto-update");
    }

    @Override
    protected void enable() {
        Items.setup(this);
        Metrics metrics = new Metrics(this, 10285);
        String ixInstalled = String.valueOf(getServer().getPluginManager().isPluginEnabled("InfinityExpansion"));
        String autoUpdates = String.valueOf(autoUpdatesEnabled());
        metrics.addCustomChart(new SimplePie("ix_installed", () -> ixInstalled));
        metrics.addCustomChart(new SimplePie("auto_updates", () -> autoUpdates));
    }

    @Override
    protected void disable() {

    }

}
