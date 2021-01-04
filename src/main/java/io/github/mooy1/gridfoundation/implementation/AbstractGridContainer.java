package io.github.mooy1.gridfoundation.implementation;

import io.github.mooy1.gridfoundation.implementation.grid.PowerGrid;
import io.github.mooy1.gridfoundation.implementation.upgrades.UpgradeManager;
import io.github.mooy1.gridfoundation.implementation.upgrades.UpgradeableBlock;
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
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractGridContainer extends AbstractContainer implements UpgradeableBlock {

    private final Map<Location, PowerGrid> grids = new HashMap<>();

    public AbstractGridContainer(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
        UpgradeManager.addTier(item);
    }
    
    @Override
    public void preRegister() {
        super.preRegister();
        addItemHandler((BlockBreakHandler) (e, item12, fortune, drops) -> {
            Block b = e.getBlock();
            PowerGrid grid = this.grids.remove(b.getLocation());
            e.setDropItems(false);
            ItemStack drop = getItem().clone();
            breakUpgrade(b.getLocation(), drop);
            onBreak(e.getPlayer(), b, BlockStorage.getInventory(b), grid);
            return true;
        });

        addItemHandler(new BlockPlaceHandler(false) {
            @Override
            public void onPlayerPlace(BlockPlaceEvent e) {
                BlockStorage.addBlockInfo(e.getBlockPlaced().getLocation(), "owner", e.getPlayer().getName());
                placeUpgrade(e.getBlockPlaced().getLocation(), e.getItemInHand());
            }
        });
    }

    @OverridingMethodsMustInvokeSuper
    public abstract void onBreak(Player p, Block b, BlockMenu menu, PowerGrid grid);

    @Override
    public final void onNewInstance(@Nonnull BlockMenu menu, @Nonnull Block b) {
        PowerGrid grid = PowerGrid.get(BlockStorage.getLocationInfo(b.getLocation(), "owner"));
        this.grids.put(b.getLocation(), grid);
        updateMenuUpgrade(menu, b.getLocation());
        onNewInstance(menu, b, grid);
    }
    
    @OverridingMethodsMustInvokeSuper
    public abstract void onNewInstance(@Nonnull BlockMenu menu, @Nonnull Block b, @Nonnull PowerGrid grid);

    @Override
    public final void tick(@Nonnull Block block, @Nonnull BlockMenu menu) {
        @Nullable PowerGrid grid = this.grids.get(block.getLocation());
        if (grid != null) {
            tick(block, menu, grid);
        }
    }
    
    public abstract void tick(@Nonnull Block b, @Nonnull BlockMenu menu, @Nonnull PowerGrid grid);

}
