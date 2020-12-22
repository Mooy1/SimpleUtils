package io.github.mooy1.slimegrid.implementation.generators;

import io.github.mooy1.infinitylib.PluginUtils;
import io.github.mooy1.infinitylib.objects.AbstractContainer;
import io.github.mooy1.slimegrid.implementation.grid.PowerGrid;
import io.github.mooy1.slimegrid.lists.Categories;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public abstract class AbstractGridConsumer extends AbstractContainer {

    private final Map<Location, PowerGrid> grids = new HashMap<>();
    private final int statusSlot;
    private final int consumption;

    public AbstractGridConsumer(SlimefunItemStack item, ItemStack[] recipe, int statusSlot, int consumption) {
        super(Categories.MACHINES, item, RecipeType.ENHANCED_CRAFTING_TABLE, recipe);
        
        this.statusSlot = statusSlot;
        this.consumption = consumption;
        
        registerBlockHandler(getId(), (p, b, item1, reason) -> {
            try {
                this.grids.remove(b.getLocation());
            } catch (NullPointerException e) {
                PluginUtils.log(Level.WARNING, "There was an error removing the consumer at " + b.getLocation().toString());
            }
            return true;
        });

        addItemHandler(new BlockPlaceHandler(false) {
            @Override
            public void onPlayerPlace(BlockPlaceEvent e) {
                BlockStorage.addBlockInfo(e.getBlockPlaced().getLocation(), "owner", e.getPlayer().getName());
            }
        });
    }

    @Override
    public void onNewInstance(@Nonnull BlockMenu menu, @Nonnull Block b) {
        PowerGrid grid = PowerGrid.get(BlockStorage.getLocationInfo(b.getLocation(), "owner"));
        this.grids.put(b.getLocation(), grid);
        menu.replaceExistingItem(this.statusSlot, grid.getStatusItem(false, 0));
    }

    @Override
    public void tick(@Nonnull Block block, @Nonnull BlockMenu blockMenu) {
        try {
            PowerGrid grid = this.grids.get(block.getLocation());
            if (grid.consume(this.consumption, false) && process(blockMenu, block)) {
                grid.consume(this.consumption, true);
                if (blockMenu.hasViewer()) {
                    blockMenu.replaceExistingItem(this.statusSlot, grid.getStatusItem(false, this.consumption));
                }
            } else if (blockMenu.hasViewer()) {
                blockMenu.replaceExistingItem(this.statusSlot, grid.getStatusItem(false, 0));
            }
        } catch (NullPointerException e) {
            PluginUtils.log(Level.WARNING, "There was an error consuming grid power at " + block.getLocation().toString());
        }
    }
    
    public abstract boolean process(@Nonnull BlockMenu menu, @Nonnull Block b);

}
