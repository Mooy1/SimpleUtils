package io.github.mooy1.gridexpansion.implementation.containers;

import io.github.mooy1.gridexpansion.implementation.powergrid.PowerGrid;
import io.github.mooy1.gridexpansion.implementation.upgrades.UpgradeableBlock;
import io.github.mooy1.infinitylib.abstracts.AbstractTicker;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;

public abstract class AbstractGridContainer extends AbstractTicker implements UpgradeableBlock {

    public AbstractGridContainer(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
        itemUpgrade(this);
    }
    
    @Override
    @OverridingMethodsMustInvokeSuper
    protected void onNewInstance(@Nonnull BlockMenu menu, @Nonnull Block b) {
        instanceUpgrade(menu, b);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    protected void onBreak(@Nonnull BlockBreakEvent e, @Nonnull BlockMenu menu) {
        breakUpgrade(e, menu);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    protected void onPlace(@Nonnull BlockPlaceEvent e) {
        placeUpgrade(e);
        PowerGrid.addOwner(e.getBlockPlaced(), e.getPlayer());
    }
    
    protected abstract int getStatusSlot();

}
