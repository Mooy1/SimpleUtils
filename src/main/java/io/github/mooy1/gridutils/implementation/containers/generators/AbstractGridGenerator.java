package io.github.mooy1.gridutils.implementation.containers.generators;

import io.github.mooy1.gridutils.implementation.containers.AbstractGridContainer;
import io.github.mooy1.gridutils.implementation.powergrid.GridBlockGenerator;
import io.github.mooy1.gridutils.setup.Categories;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.List;

public abstract class AbstractGridGenerator extends AbstractGridContainer implements GridBlockGenerator {
    
    public AbstractGridGenerator(SlimefunItemStack item, ItemStack[] recipe) {
        super(Categories.GENERATORS, item, RecipeType.ENHANCED_CRAFTING_TABLE, recipe);
    }

    @Override
    public final int generate(BlockMenu menu, Block b, Config config) {
        return generate(menu, b, config, getUpgrade(config)) << getUpgrade(config);
    }
    
    protected abstract int generate(BlockMenu menu, Block b, Config config, int tier);

    @Override
    @OverridingMethodsMustInvokeSuper
    public void getStats(@Nonnull List<String> stats, int tier) {
        stats.add("&6Generation: &e" + (1 << tier) + "x");
    }
    
}
