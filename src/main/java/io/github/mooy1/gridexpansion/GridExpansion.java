package io.github.mooy1.gridexpansion;

import io.github.mooy1.gridexpansion.implementation.powergrid.PowerGrid;
import io.github.mooy1.gridexpansion.setup.Setup;
import io.github.mooy1.infinitylib.PluginUtils;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import lombok.Getter;
import org.bstats.bukkit.Metrics;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;

public class GridExpansion extends JavaPlugin implements SlimefunAddon {

    @Getter
    private static GridExpansion instance;

    @Override
    public void onEnable() {
        instance = this;

        PluginUtils.setup(ChatColor.GOLD + "GridExpansion", this, "Mooy1/GridExpansion/master", getFile());
        
        @SuppressWarnings("unused")
        final Metrics metrics = new Metrics(this, 9734);
        
        Setup.setup(this);
        
        PluginUtils.startTicker(PowerGrid::tickAll);
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    @Override
    public String getBugTrackerURL() {
        return "https://github.com/Mooy1/GridExpansion/issues";
    }

    @Nonnull
    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

}
