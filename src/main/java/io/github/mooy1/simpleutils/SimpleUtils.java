package io.github.mooy1.simpleutils;

import io.github.mooy1.infinitylib.PluginUtils;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;

public final class SimpleUtils extends JavaPlugin implements SlimefunAddon {
    
    @Getter
    private static SimpleUtils instance;

    @Override
    public void onEnable() {
        instance = this;

        
        PluginUtils.setup(ChatColor.GOLD + "SimpleUtils", this, "Mooy1/SimpleUtils/master", getFile());
        
        PluginUtils.setupMetrics(10285);
        
        Setup.setup(this);
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    @Override
    public String getBugTrackerURL() {
        return "https://github.com/Mooy1/SimpleUtils/issues";
    }

    @Nonnull
    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

}
