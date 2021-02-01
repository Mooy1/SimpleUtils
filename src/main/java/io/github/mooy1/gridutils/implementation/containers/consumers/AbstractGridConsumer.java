package io.github.mooy1.gridutils.implementation.containers.consumers;

import io.github.mooy1.gridutils.implementation.containers.AbstractGridContainer;
import io.github.mooy1.gridutils.implementation.powergrid.GridBlockConsumer;
import io.github.mooy1.gridutils.setup.Categories;
import io.github.mooy1.gridutils.utils.GridLorePreset;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.List;
import java.util.Locale;

public abstract class AbstractGridConsumer extends AbstractGridContainer implements GridBlockConsumer {
    
    public AbstractGridConsumer(SlimefunItemStack item, ItemStack[] recipe) {
        super(Categories.MACHINES, item, RecipeType.ENHANCED_CRAFTING_TABLE, recipe);
    }
    
    @Override
    public void process(@Nonnull Block b, @Nonnull BlockMenu menu, @Nonnull Config config) {
        process(b, menu, config, getUpgrade(config));
    }
    
    protected abstract void process(@Nonnull Block b, @Nonnull BlockMenu menu, @Nonnull Config config, int tier);
    
    protected static SlimefunItemStack make(int power, @Nonnull String name, @Nonnull String desc, @Nonnull Material material) {
        return new SlimefunItemStack(
                "GRID_" + name.replace(" ", "_").toUpperCase(Locale.ROOT),
                material,
                "&e" + name,
                "&7" + desc,
                "",
                GridLorePreset.consumesGridPower(power)
        );
    }
    
    @Override
    @OverridingMethodsMustInvokeSuper
    public void getStats(@Nonnull List<String> stats, int tier) {
        stats.add("&6Usage: &e" +  (1 << Math.max(0, tier - 1)) + "x");
    } // TODO add speed for consumers
    
}
