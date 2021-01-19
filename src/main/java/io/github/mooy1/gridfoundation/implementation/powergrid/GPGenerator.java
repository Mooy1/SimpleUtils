package io.github.mooy1.gridfoundation.implementation.powergrid;

import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * represents a generator in a power grid
 */
public final class GPGenerator extends GPComponent {
    
    @Setter
    int generation;

    GPGenerator(@Nonnull ItemStack item) {
        super(item);
        this.generation = 0;
    }

    @Override
    protected void addViewerInfo(List<String> info) {
        info.add("");
        info.add("&aGenerating: " + this.generation);
    }

}
