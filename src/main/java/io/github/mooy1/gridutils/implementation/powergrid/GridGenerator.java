package io.github.mooy1.gridutils.implementation.powergrid;

import lombok.Setter;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * represents a generator in a power grid
 */
public final class GridGenerator extends GridComponent {
    
    @Setter
    int generation;

    GridGenerator(@Nonnull ItemStack item, @Nonnull Block b) {
        super(item, b);
    }

    @Nonnull
    @Override
    protected String getViewerInfo() {
        return "&aGenerating: " + this.generation;
    }

}
