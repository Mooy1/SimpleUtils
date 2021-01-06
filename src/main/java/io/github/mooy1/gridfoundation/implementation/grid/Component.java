package io.github.mooy1.gridfoundation.implementation.grid;

import lombok.AllArgsConstructor;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * A component in a grid
 */
@AllArgsConstructor
public abstract class Component {
    
    final ItemStack item;
    final Grid grid;
    
    @Nonnull
    abstract ItemStack getViewerItem();
    
    @Nonnull
    public abstract ItemStack getStatusItem();

    public void updateStatus(@Nonnull BlockMenu menu, int slot) {
        if (menu.hasViewer()) {
            menu.replaceExistingItem(slot, getStatusItem());
        }
    }
    
}
