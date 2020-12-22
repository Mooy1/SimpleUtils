package io.github.mooy1.slimegrid.implementation.generators;

import io.github.mooy1.infinitylib.PluginUtils;
import io.github.mooy1.infinitylib.objects.AbstractContainer;
import io.github.mooy1.slimegrid.implementation.grid.PowerGrid;
import io.github.mooy1.slimegrid.implementation.grid.GridGenerator;
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

public abstract class AbstractGridGenerator extends AbstractContainer {

    private final Map<Location, GridGenerator> generators = new HashMap<>();
    private final Map<Location, PowerGrid> grids = new HashMap<>();
    private final int statusSlot;
    
    public AbstractGridGenerator(SlimefunItemStack item, ItemStack[] recipe, int statusSlot) {
        super(Categories.GENERATORS, item, RecipeType.ENHANCED_CRAFTING_TABLE, recipe);

        this.statusSlot = statusSlot;
        
        registerBlockHandler(getId(), (p, b, item1, reason) -> {
            try {
                this.grids.remove(b.getLocation()).getGenerators().remove(this.generators.remove(b.getLocation()));
            } catch (NullPointerException e) {
                PluginUtils.log(Level.WARNING, "There was an error removing the generator at " + b.getLocation().toString());
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
        GridGenerator gen = new GridGenerator(b.getLocation().hashCode());
        this.generators.put(b.getLocation(), gen);
        PowerGrid grid = PowerGrid.get(BlockStorage.getLocationInfo(b.getLocation(), "owner"));
        grid.getGenerators().add(gen);
        this.grids.put(b.getLocation(), grid);
        menu.replaceExistingItem(this.statusSlot, grid.getStatusItem(true, 0));
    }

    @Override
    public void tick(@Nonnull Block block, @Nonnull BlockMenu blockMenu) {
        try {
            int generation = getGeneration(blockMenu, block);
            this.generators.get(block.getLocation()).setGeneration(generation);
            if (blockMenu.hasViewer()) {
                blockMenu.replaceExistingItem(this.statusSlot, this.grids.get(block.getLocation()).getStatusItem(true, generation));
            }
        } catch (NullPointerException e) {
            PluginUtils.log(Level.WARNING, "There was an error setting the generation of the generator at " + block.getLocation().toString());
        }
    }
    
    public abstract int getGeneration(@Nonnull BlockMenu menu, @Nonnull Block b);

}
