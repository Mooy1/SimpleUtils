package io.github.mooy1.gridfoundation.implementation.powergrid;

import io.github.mooy1.infinitylib.items.LoreUtils;
import lombok.AllArgsConstructor;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * A component in a grid
 */
@AllArgsConstructor
abstract class GridComponent {
    
    private final ItemStack item;
            
    @Nonnull
    public final ItemStack getViewerItem() {
        ItemStack item = this.item.clone();
        List<String> lore = new ArrayList<>(8);
        addViewerInfo(lore);
        LoreUtils.setLore(item, lore.toArray(new String[0]));
        return item;
    }
    
    protected abstract void addViewerInfo(List<String> info);
    
}
