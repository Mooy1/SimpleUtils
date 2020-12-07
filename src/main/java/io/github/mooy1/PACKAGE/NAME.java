package io.github.mooy1.PACKAGE;

import io.github.thebusybiscuit.slimefun4.libraries.paperlib.PaperLib;
import lombok.Getter;
import me.mrCookieSlime.Slimefun.cscorelib2.updater.GitHubBuildsUpdater;
import org.apache.commons.lang.Validate;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;

import javax.annotation.Nonnull;
import java.util.logging.Level;

public class NAME extends JavaPlugin implements SlimefunAddon {

    @Getter
    private static NAME instance;

    @Override
    public void onEnable() {
        instance = this;
        
        updateConfig();
        
        //@SuppressWarnings("unused")
        //final Metrics metrics = new Metrics(this, METRICS_ID);

        PaperLib.suggestPaper(this);
        
        if (getDescription().getVersion().startsWith("DEV - ")) {
            log(Level.INFO, "Starting auto update");
            new GitHubBuildsUpdater(this, this.getFile(), "Mooy1/NAME/master").start();
        } else {
            log(Level.WARNING, "You must be on a DEV build to auto update!");
        }

        //setup
        
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    @Override
    public String getBugTrackerURL() {
        return "https://github.com/Mooy1/NAME/issues";
    }

    @Nonnull
    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }
    
    public static void log(@Nonnull Level level, @Nonnull String... logs) {
        for (String log : logs) {
            instance.getLogger().log(level, log);
        }
    }

    private void updateConfig() {
        getConfig().options().copyDefaults(true);
        getConfig().options().copyHeader(true);
        saveConfig();
    }
    
    public static void runSync(@Nonnull Runnable runnable, long delay) {
        Validate.notNull(runnable, "Cannot run null");
        Validate.isTrue(delay >= 0, "The delay cannot be negative");

        if (instance == null || !instance.isEnabled()) {
            return;
        }

        instance.getServer().getScheduler().runTaskLater(instance, runnable, delay);
    }

    public static void runSync(@Nonnull Runnable runnable) {
        Validate.notNull(runnable, "Cannot run null");

        if (instance == null || !instance.isEnabled()) {
            return;
        }

        instance.getServer().getScheduler().runTask(instance, runnable);
    }

}
