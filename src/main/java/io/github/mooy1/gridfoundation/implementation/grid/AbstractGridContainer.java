package io.github.mooy1.gridfoundation.implementation.grid;

import io.github.mooy1.infinitylib.objects.AbstractContainer;
import io.github.mooy1.gridfoundation.implementation.upgrades.UpgradeManager;
import io.github.mooy1.gridfoundation.implementation.upgrades.UpgradeableBlock;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
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
    protected final int statusSlot;

    public AbstractGridContainer(Category category, SlimefunItemStack item, int statusSlot, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
        this.statusSlot = statusSlot;
        
        addItemHandler((BlockBreakHandler) (e, item12, fortune, drops) -> {
            Block b = e.getBlock();
            PowerGrid grid = this.grids.remove(b.getLocation());
            e.setDropItems(false);
            ItemStack drop = getItem().clone();
            UpgradeManager.onBreak(b.getLocation(), drop);
            onBreak(e.getPlayer(), b, BlockStorage.getInventory(b), grid);
            return true;
        });

        addItemHandler(new BlockPlaceHandler(false) {
            @Override
            public void onPlayerPlace(BlockPlaceEvent e) {
                BlockStorage.addBlockInfo(e.getBlockPlaced().getLocation(), "owner", e.getPlayer().getName());
                UpgradeManager.onPlace(e.getBlockPlaced().getLocation(), e.getItemInHand(), AbstractGridContainer.this);
            }
        });
        
    }

    @OverridingMethodsMustInvokeSuper
    public abstract void onBreak(Player p, Block b, BlockMenu menu, PowerGrid grid);

    @Override
    public final void onNewInstance(@Nonnull BlockMenu menu, @Nonnull Block b) {
        PowerGrid grid = PowerGrid.get(BlockStorage.getLocationInfo(b.getLocation(), "owner"));
        this.grids.put(b.getLocation(), grid);
        onNewInstance(menu, b, grid);
        menu.addMenuClickHandler(this.statusSlot, ChestMenuUtils.getEmptyClickHandler());
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
