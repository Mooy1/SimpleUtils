package io.github.mooy1.gridfoundation.implementation;

import io.github.mooy1.gridfoundation.implementation.grid.Grid;
import io.github.mooy1.gridfoundation.implementation.grid.GridBlock;
import io.github.mooy1.infinitylib.objects.AbstractContainer;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;

public abstract class AbstractGridContainer extends AbstractContainer implements GridBlock {
    
    public AbstractGridContainer(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        addItemHandler((BlockBreakHandler) (e, item12, fortune, drops) -> {
            Block b = e.getBlock();
            Grid grid = removeGrid(b.getLocation());
            BlockMenu menu = BlockStorage.getInventory(b);
            if (menu != null && grid != null) {
                onBreak(e, e.getBlock().getLocation(), menu, grid);
            }
            return true;
        });

        addItemHandler(new BlockPlaceHandler(false) {
            @Override
            public void onPlayerPlace(BlockPlaceEvent e) {
                storeGrid(e.getBlockPlaced().getLocation(), e.getPlayer());
                onPlace(e);
            }
        });
    }

    public abstract void onPlace(@Nonnull BlockPlaceEvent e);

    @OverridingMethodsMustInvokeSuper
    public abstract void onBreak(@Nonnull BlockBreakEvent e, @Nonnull Location l, @Nonnull BlockMenu menu, @Nonnull Grid grid);

    @Override
    public final void onNewInstance(@Nonnull BlockMenu menu, @Nonnull Block b) {
        Grid grid = addGrid(b.getLocation());
        if (grid != null) {
            onNewInstance(menu, b, grid);
        }
    }
    
    @OverridingMethodsMustInvokeSuper
    public abstract void onNewInstance(@Nonnull BlockMenu menu, @Nonnull Block b, @Nonnull Grid grid);

    @Override
    public final void tick(@Nonnull Block block, @Nonnull BlockMenu menu) {
        Grid grid = getGrid(block.getLocation());
        if (grid != null) {
            tick(block, menu, grid);
        }
    }
    
    public abstract void tick(@Nonnull Block b, @Nonnull BlockMenu menu, @Nonnull Grid grid);

}
