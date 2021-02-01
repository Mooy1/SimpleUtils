package io.github.mooy1.gridutils.implementation.powergrid;

import io.github.mooy1.infinitylib.items.LoreUtils;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * A component in a grid
 */
abstract class GridComponent {
    
    private final ItemStack item;
    private final String status;  
    private final Location location;
    
    GridComponent(@Nonnull ItemStack item, @Nonnull Block b) {
        this.item = item;
        this.location = b.getLocation();
        this.status = "Location: " + b.getX() + "x " + b.getY() + "y " + b.getZ() + "z " + b.getWorld().getName();
    }
    
    @Nonnull
    final ItemStack getViewerItem() {
        ItemStack item = this.item.clone();
        List<String> lore = new ArrayList<>(4);
        lore.add("");
        lore.add(getViewerInfo());
        if (this.status != null) {
            lore.add(this.status);
        }
        LoreUtils.setLore(item, lore.toArray(new String[0]));
        return item;
    }
    
    @Nonnull
    protected abstract String getViewerInfo();

    @Override
    public final int hashCode() {
        return this.location.hashCode();
    }

    @Override
    public final boolean equals(Object obj) {
        return obj instanceof GridComponent && this.location.equals(((GridComponent) obj).location);
    }

}
