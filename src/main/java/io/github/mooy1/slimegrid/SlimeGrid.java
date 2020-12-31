package io.github.mooy1.slimegrid;

import io.github.mooy1.infinitylib.PluginUtils;
import io.github.mooy1.infinitylib.command.CommandLib;
import io.github.mooy1.infinitylib.player.MessageUtils;
import io.github.mooy1.slimegrid.implementation.grid.PowerGrid;
import io.github.mooy1.slimegrid.setup.Setup;
import io.github.mooy1.slimegrid.setup.TimingsCommand;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import lombok.Getter;
import me.mrCookieSlime.bstats.bukkit.Metrics;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;

public class SlimeGrid extends JavaPlugin implements SlimefunAddon {

    @Getter
    private static SlimeGrid instance;
    @Getter
    private static long timings = 0;
    @Getter
    private static int tick = 1;

    @Override
    public void onEnable() {
        instance = this;

        PluginUtils.setup(this, "Mooy1/SlimeGrid/master", getFile());
        MessageUtils.setPrefix(ChatColor.GOLD + "SlimeGrid");
        new CommandLib(this, "slimegrid", "slimegrid.admin", "/sg");
        CommandLib.addCommand(new TimingsCommand());
        
        @SuppressWarnings("unused")
        final Metrics metrics = new Metrics(this, 9734);
        
        Setup.setup(this);

        PluginUtils.scheduleRepeatingSync(() -> {
            long time = System.nanoTime();
            PowerGrid.tick();
            if (tick < 60) {
                tick++;
            } else {
                tick = 1;
            }
            timings = (System.nanoTime() - time) / 1000000;
        }, 100, PluginUtils.TICKER_DELAY);
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
