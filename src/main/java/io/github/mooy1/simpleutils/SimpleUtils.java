package io.github.mooy1.simpleutils;

import javax.annotation.Nonnull;

import io.github.mooy1.infinitylib.AbstractAddon;
import io.github.mooy1.infinitylib.bstats.bukkit.Metrics;
import io.github.mooy1.infinitylib.bstats.charts.SimplePie;
import io.github.mooy1.simpleutils.implementation.Items;

public final class SimpleUtils extends AbstractAddon {

    private static SimpleUtils instance;

    public static SimpleUtils inst() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        super.onEnable();

        Items.setup(this);
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    @Nonnull
    @Override
    protected Metrics setupMetrics() {
        Metrics metrics = new Metrics(this, 10285);
        String ixInstalled = String.valueOf(getServer().getPluginManager().isPluginEnabled("InfinityExpansion"));
        metrics.addCustomChart(new SimplePie("ix_installed", () -> ixInstalled));
        return metrics;
    }

    @Nonnull
    @Override
    protected String getGithubPath() {
        return "Mooy1/SimpleUtils/master";
    }

}
