package io.github.mooy1.simpleutils;

import io.github.mooy1.infinitylib.AbstractAddon;
import io.github.mooy1.infinitylib.bstats.bukkit.Metrics;
import io.github.mooy1.infinitylib.commands.AbstractCommand;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;

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
        return new Metrics(this,10285);
    }
    
    @Override
    protected String getGithubPath() {
        return "Mooy1/SimpleUtils/master";
    }

    @Nonnull
    @Override
    protected List<AbstractCommand> getSubCommands() {
        return new ArrayList<>();
    }

}
