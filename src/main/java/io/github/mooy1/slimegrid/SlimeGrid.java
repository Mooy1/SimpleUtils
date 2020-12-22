package io.github.mooy1.slimegrid;

import io.github.mooy1.infinitylib.PluginUtils;
import io.github.mooy1.infinitylib.player.MessageUtils;
import io.github.mooy1.slimegrid.implementation.grid.PowerGrid;
import io.github.mooy1.slimegrid.setup.Setup;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;

import javax.annotation.Nonnull;

public class SlimeGrid extends JavaPlugin implements SlimefunAddon {

    @Getter
    private static SlimeGrid instance;

    @Override
    public void onEnable() {
        instance = this;

        PluginUtils.setPlugin(this);
        PluginUtils.setupConfig();
        MessageUtils.setPrefix("&6SlimeGrid");
        
        //@SuppressWarnings("unused")
        //final Metrics metrics = new Metrics(this, METRICS_ID);

        //if (getDescription().getVersion().startsWith("DEV - ")) {
        //    new GitHubBuildsUpdater(this, this.getFile(), "Mooy1/SlimeUtils/master").start();
        //} else {
        //    log(Level.WARNING, "You must be on a DEV build to auto update!");
        //}
        
        Setup.setup(this);

        PowerGrid.start();
        
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    @Override
    public String getBugTrackerURL() {
        return "https://github.com/Mooy1/SlimeGrid/issues";
    }

    @Nonnull
    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

}
