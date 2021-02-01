package io.github.mooy1.gridutils;

import io.github.mooy1.gridutils.implementation.powergrid.PowerGridManager;
import io.github.mooy1.gridutils.setup.Setup;
import io.github.mooy1.infinitylib.PluginUtils;
import io.github.mooy1.infinitylib.command.CommandManager;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import lombok.Getter;
import org.bstats.bukkit.Metrics;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;

public class GridUtils extends JavaPlugin implements SlimefunAddon {

    @Getter
    private static GridUtils instance;

    @Override
    public void onEnable() {
        instance = this;

        PluginUtils.setup(ChatColor.GOLD + "GridUtils", this, "Mooy1/GridUtils/master", getFile());
        CommandManager.setup("gridutils", "gridutils.admin", "grid, gu");
        
        @SuppressWarnings("unused")
        final Metrics metrics = new Metrics(this, 9734);
        
        Setup.setup(this);
        
        PluginUtils.startTicker(PowerGridManager::tick);
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    @Override
    public String getBugTrackerURL() {
        return "https://github.com/Mooy1/GridFoundation/issues";
    }

    @Nonnull
    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

}
