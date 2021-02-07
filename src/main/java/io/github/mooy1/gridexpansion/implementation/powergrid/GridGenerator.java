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
        GENERATORS.put(b.getLocation(), this);
    }

    @Override
    protected void add() {
        this.grid.addGenerator(this);
    }

    @Override
    public void remove() {
        this.grid.removeGenerator(this);
    }

    @Nonnull
    @Override
    protected String getInfo() {
        return "&aGenerating: " + this.generation + " GP";
    }

}
