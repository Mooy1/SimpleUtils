package io.github.mooy1.gridfoundation.implementation.consumers;

import io.github.mooy1.gridfoundation.implementation.grid.AbstractGridContainer;
import io.github.mooy1.gridfoundation.implementation.grid.PowerGrid;
import io.github.mooy1.gridfoundation.setup.Categories;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;

public abstract class AbstractGridConsumer extends AbstractGridContainer {

    private final int consumption;

    public AbstractGridConsumer(SlimefunItemStack item, ItemStack[] recipe, int statusSlot, int consumption) {
        super(Categories.MACHINES, item, statusSlot, RecipeType.ENHANCED_CRAFTING_TABLE, recipe);
        this.consumption = consumption;
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void onNewInstance(@Nonnull BlockMenu menu, @Nonnull Block b, @Nonnull PowerGrid grid) {
        menu.replaceExistingItem(this.statusSlot, grid.getStatusItem(false, 0));
    }

    @Override
    public final void tick(@Nonnull Block block, @Nonnull BlockMenu blockMenu, @Nonnull PowerGrid grid) {
        int consumption = this.consumption * getLevel(block) / 2;
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

}
