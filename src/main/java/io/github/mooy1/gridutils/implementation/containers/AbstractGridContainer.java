package io.github.mooy1.gridutils.implementation.containers;

import io.github.mooy1.gridutils.implementation.powergrid.GridBlockComponent;
import io.github.mooy1.gridutils.implementation.upgrades.UpgradeableBlock;
import io.github.mooy1.infinitylib.abstracts.AbstractTicker;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
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

public abstract class AbstractGridContainer extends AbstractTicker implements UpgradeableBlock, GridBlockComponent {

    public AbstractGridContainer(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
        itemUpgrade(this);
    }

    @Override
    protected final void tick(@Nonnull BlockMenu blockMenu, @Nonnull Block block, @Nonnull Config config) {
        tickComponent(block, blockMenu, config);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    protected void onNewInstance(@Nonnull BlockMenu menu, @Nonnull Block b) {
        instanceUpgrade(menu, b);
        instanceGrid(menu, b);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    protected void onBreak(@Nonnull BlockBreakEvent e, @Nonnull BlockMenu menu) {
        breakUpgrade(e, menu);
        breakGrid(e, menu);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    protected void onPlace(@Nonnull BlockPlaceEvent e) {
        placeUpgrade(e);
        placeGrid(e);
    }

}
