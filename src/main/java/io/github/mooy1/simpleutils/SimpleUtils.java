package io.github.mooy1.simpleutils;

import io.github.mooy1.infinitylib.InfinityAddon;
import io.github.mooy1.infinitylib.command.AbstractCommand;

public final class SimpleUtils extends InfinityAddon {
    
    private static SimpleUtils instance;

    public static SimpleUtils inst() {
        return instance;
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        instance = this;
        Setup.setup(this);
    }

    @Override
    protected int getMetricsID() {
        return 10285;
    }

    @Override
    protected String getGithubPath() {
        return "Mooy1/SimpleUtils/master";
    }

    @Override
    protected AbstractCommand[] getCommands() {
        return new AbstractCommand[0];
    }

    @Override
    public void onDisable() {
        instance = null;
    }

}
