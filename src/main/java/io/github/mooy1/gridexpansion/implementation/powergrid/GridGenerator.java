package io.github.mooy1.gridexpansion.implementation.powergrid;

import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * represents a generator in a power grid
 */
public final class GridGenerator extends GridComponent {
    
    private static final Map<Location, GridGenerator> GENERATORS = new HashMap<>();

    public static GridGenerator get(@Nonnull Location l) {
        return GENERATORS.get(l);
    }
    
    @Setter
    int generation;

    public GridGenerator(@Nonnull ItemStack item, @Nonnull Block b, @Nonnull PowerGrid grid) {
        super(item, b, grid);
    }

    @Override
    protected void add(@Nonnull Location l) {
        this.grid.addGenerator(this);
        GENERATORS.put(l, this);
    }

    @Override
    public void remove(@Nonnull Location l) {
        this.grid.removeGenerator(this);
        GENERATORS.remove(l);
    }

    @Nonnull
    @Override
    protected String getInfo() {
        return "&aGenerating: " + this.generation + " GP";
    }

}
