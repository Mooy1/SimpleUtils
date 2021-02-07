package io.github.mooy1.gridexpansion.implementation.containers.generators;

import io.github.mooy1.gridexpansion.implementation.containers.AbstractGridContainer;
import io.github.mooy1.gridexpansion.implementation.powergrid.GridGenerator;
import io.github.mooy1.gridexpansion.implementation.powergrid.PowerGrid;
import io.github.mooy1.gridexpansion.setup.Categories;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.List;

public abstract class AbstractGridGenerator extends AbstractGridContainer {
    
    public AbstractGridGenerator(SlimefunItemStack item, ItemStack[] recipe) {
        super(Categories.GENERATORS, item, RecipeType.ENHANCED_CRAFTING_TABLE, recipe);
    }

    @Override
    protected void tick(@Nonnull BlockMenu blockMenu, @Nonnull Block block, @Nonnull Config config) {
        GridGenerator generator = GridGenerator.get(block.getLocation());
        if (generator != null) {
            int tier = getUpgrade(config);
            generator.setGeneration(generate(blockMenu, block, config, tier) << tier);
            if (blockMenu.hasViewer()) {
                blockMenu.replaceExistingItem(getStatusSlot(), generator.getStatusItem(), false);
            }
        }
    }

    protected abstract int generate(BlockMenu menu, Block b, Config config, int tier);

    @Override
    @OverridingMethodsMustInvokeSuper
    public void getStats(@Nonnull List<String> stats, int tier) {
        stats.add("&6Generation: &e" + (1 << tier) + "x");
    }

    @Override
    protected void onBreak(@Nonnull BlockBreakEvent e, @Nonnull BlockMenu menu) {
        super.onBreak(e, menu);
        GridGenerator.get(e.getBlock().getLocation()).remove(e.getBlock().getLocation());
    }

    @Override
    protected void onNewInstance(@Nonnull BlockMenu menu, @Nonnull Block b) {
        super.onNewInstance(menu, b);
        menu.replaceExistingItem(getStatusSlot(),
                new GridGenerator(menu.getPreset().getSlimefunItem().getItem(), b, PowerGrid.get(b.getLocation())).getStatusItem()
        );
    }
    
}
