package io.github.mooy1.gridfoundation.implementation.grid;

import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * represents a generator in a grid
 */
public final class Generator extends Component {
    
    @Setter
    int generation;

    Generator(@Nonnull ItemStack item, @Nonnull Grid grid, @Nullable Location l) {
        super(item, grid, l);
        this.generation = 0;
    }
    
    @Nonnull
    @Override
    String getStatus() {
        return "&aGenerating: " + this.generation;
    }

}
