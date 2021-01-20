package io.github.mooy1.gridfoundation.implementation.powergrid;

import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * represents a generator in a power grid
 */
public final class GridGenerator extends GridComponent {
    
    @Setter
    int generation;

    GridGenerator(@Nonnull ItemStack item) {
        super(item);
    }

    @Override
    protected void addViewerInfo(@Nonnull List<String> info) {
        info.add("");
        info.add("&aGenerating: " + this.generation);
    }

}
