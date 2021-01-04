package io.github.mooy1.gridfoundation.implementation.consumers;

import io.github.mooy1.gridfoundation.implementation.AbstractGridContainer;
import io.github.mooy1.gridfoundation.implementation.grid.PowerGrid;
import io.github.mooy1.gridfoundation.setup.Categories;
import io.github.mooy1.gridfoundation.utils.GridLorePreset;
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

public abstract class AbstractGridConsumer extends AbstractGridContainer {

    private final int consumption;
    private final int statusSlot;
    
    public AbstractGridConsumer(SlimefunItemStack item, ItemStack[] recipe, int statusSlot, int consumption) {
        super(Categories.MACHINES, item, RecipeType.ENHANCED_CRAFTING_TABLE, recipe);
        this.statusSlot = statusSlot;
        this.consumption = consumption;
    }

    @Override
    public void onNewInstance(@Nonnull BlockMenu menu, @Nonnull Block b, @Nonnull PowerGrid grid) {
        menu.replaceExistingItem(this.statusSlot, grid.getStatusItem(false, 0));
    }

    @Override
    public final void tick(@Nonnull Block block, @Nonnull BlockMenu blockMenu, @Nonnull PowerGrid grid) {
        int consumption = this.consumption * Math.max(1, getLevel(block) / 2);
        if (grid.consume(consumption, false) && process(blockMenu, block)) {
            grid.consume(consumption, true);
            if (blockMenu.hasViewer()) {
                blockMenu.replaceExistingItem(this.statusSlot, grid.getStatusItem(false, this.consumption));
            }
        } else if (blockMenu.hasViewer()) {
            blockMenu.replaceExistingItem(this.statusSlot, grid.getStatusItem(false, 0));
        }
    }
    
    public abstract boolean process(@Nonnull BlockMenu menu, @Nonnull Block b);

    protected static SlimefunItemStack make(int power, String name, String desc, Material material) {
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
    public void getStats(@Nonnull List<String> stats, int level) {
        stats.add("&6Usage: &e" + Math.max(1, level / 2) + "x");
    }

}
