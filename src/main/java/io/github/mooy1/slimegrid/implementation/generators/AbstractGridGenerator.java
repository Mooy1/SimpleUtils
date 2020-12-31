package io.github.mooy1.slimegrid.implementation.generators;

import io.github.mooy1.infinitylib.objects.AbstractContainer;
import io.github.mooy1.slimegrid.implementation.grid.PowerGenerator;
import io.github.mooy1.slimegrid.implementation.grid.PowerGrid;
import io.github.mooy1.slimegrid.lists.Categories;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
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

public abstract class AbstractGridGenerator extends AbstractContainer {

    private final Map<Location, PowerGenerator> generators = new HashMap<>();
    private final Map<Location, PowerGrid> grids = new HashMap<>();
    private final int statusSlot;
    
    public AbstractGridGenerator(SlimefunItemStack item, ItemStack[] recipe, int statusSlot) {
        super(Categories.GENERATORS, item, RecipeType.ENHANCED_CRAFTING_TABLE, recipe);

        this.statusSlot = statusSlot;
        
        registerBlockHandler(getId(), (p, b, item1, reason) -> {
            @Nullable PowerGrid grid = this.grids.remove(b.getLocation());
            @Nullable PowerGenerator generator = this.generators.remove(b.getLocation());
            if (grid != null && generator != null) {
                grid.getGenerators().remove(generator);
                onBreak(p, b, BlockStorage.getInventory(b), grid, generator);
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

    public void onBreak(Player p, Block b, BlockMenu menu, PowerGrid grid, PowerGenerator generator) {
        
    }
    
    @Override
    public void onNewInstance(@Nonnull BlockMenu menu, @Nonnull Block b) {
        PowerGenerator gen = new PowerGenerator(b.getLocation().hashCode());
        this.generators.put(b.getLocation(), gen);
        PowerGrid grid = PowerGrid.get(BlockStorage.getLocationInfo(b.getLocation(), "owner"));
        grid.getGenerators().add(gen);
        this.grids.put(b.getLocation(), grid);
        menu.replaceExistingItem(this.statusSlot, grid.getStatusItem(true, 0));
    }

    @Override
    public void tick(@Nonnull Block block, @Nonnull BlockMenu blockMenu) {
        int generation = getGeneration(blockMenu, block);
        @Nullable PowerGenerator generator = this.generators.get(block.getLocation());
        if (generator != null) {
            generator.setGeneration(generation);
            if (blockMenu.hasViewer()) {
                @Nullable PowerGrid grid = this.grids.get(block.getLocation());
                if (grid != null) {
                    blockMenu.replaceExistingItem(this.statusSlot, grid.getStatusItem(true, generation));
                }
            }
        }
    }
    
    public abstract int getGeneration(@Nonnull BlockMenu menu, @Nonnull Block b);

}
