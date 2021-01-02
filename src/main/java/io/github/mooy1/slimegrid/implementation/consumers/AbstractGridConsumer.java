package io.github.mooy1.slimegrid.implementation.consumers;

import io.github.mooy1.infinitylib.objects.AbstractContainer;
import io.github.mooy1.slimegrid.implementation.grid.PowerGrid;
import io.github.mooy1.slimegrid.lists.Categories;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
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
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractGridConsumer extends AbstractContainer {

    private final Map<Location, PowerGrid> grids = new HashMap<>();
    private final int statusSlot;
    private final int consumption;

    public AbstractGridConsumer(SlimefunItemStack item, ItemStack[] recipe, int statusSlot, int consumption) {
        super(Categories.MACHINES, item, RecipeType.ENHANCED_CRAFTING_TABLE, recipe);
        
        this.statusSlot = statusSlot;
        this.consumption = consumption;
        
        registerBlockHandler(getId(), (p, b, item1, reason) -> {
            onBreak(p, b, b.getLocation(), BlockStorage.getInventory(b), this.grids.remove(b.getLocation()));
            return true;
        });

        addItemHandler(new BlockPlaceHandler(false) {
            @Override
            public void onPlayerPlace(BlockPlaceEvent e) {
                BlockStorage.addBlockInfo(e.getBlockPlaced().getLocation(), "owner", e.getPlayer().getName());
            }
        });
    }
    
    public abstract void onBreak(Player p, Block b, Location l, BlockMenu menu, PowerGrid grid);

    /**
     * Always call super when overriding
     */
    @Override
    public void onNewInstance(@Nonnull BlockMenu menu, @Nonnull Block b) {
        PowerGrid grid = PowerGrid.get(BlockStorage.getLocationInfo(b.getLocation(), "owner"));
        this.grids.put(b.getLocation(), grid);
        menu.addMenuClickHandler(this.statusSlot, ChestMenuUtils.getEmptyClickHandler());
        menu.replaceExistingItem(this.statusSlot, grid.getStatusItem(false, 0));
    }

    @Override
    public final void tick(@Nonnull Block block, @Nonnull BlockMenu blockMenu) {
        @Nullable PowerGrid grid = this.grids.get(block.getLocation());
        if (grid != null) {
            if (grid.consume(this.consumption, false) && process(blockMenu, block)) {
                grid.consume(this.consumption, true);
                if (blockMenu.hasViewer()) {
                    blockMenu.replaceExistingItem(this.statusSlot, grid.getStatusItem(false, this.consumption));
                }
            } else if (blockMenu.hasViewer()) {
                blockMenu.replaceExistingItem(this.statusSlot, grid.getStatusItem(false, 0));
            }
        }
    }
    
    public abstract boolean process(@Nonnull BlockMenu menu, @Nonnull Block b);

}
