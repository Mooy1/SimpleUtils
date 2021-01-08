package io.github.mooy1.gridfoundation.implementation.grid;

import io.github.mooy1.infinitylib.items.LoreUtils;
import io.github.mooy1.infinitylib.menus.LocationUtils;
import lombok.AllArgsConstructor;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A component in a grid
 */
@AllArgsConstructor
public abstract class Component {
    
    private final ItemStack item;
    private final Grid grid;
    
    @Nullable
    private final Location location;
            
    @Nonnull
    public ItemStack getViewerItem() {
        ItemStack item = this.item.clone();
        if (this.location != null && this.location.getWorld() != null) {
            LoreUtils.setLore(item, "", getStatus(), "", ChatColor.GRAY + "Location: x " + this.location.getX() + " y " + this.location.getY() + " z " + this.location.getZ() + " in " + this.location.getWorld().getName()
            );
        } else {
            LoreUtils.setLore(item, "", getStatus());
        }
        return item;
    }
    
    @Nonnull
    public ItemStack getStatusItem() {
        return new CustomItem(
                this.grid.maxed ? Material.RED_STAINED_GLASS_PANE : this.grid.usage == this.grid.max ? Material.YELLOW_STAINED_GLASS_PANE : Material.LIME_STAINED_GLASS_PANE,
                getStatus() + " GP",
                "&7Grid: " + this.grid.usage + " / " + this.grid.max,
                "",
                this.grid.maxed ? "&cGrid Overloaded!" : ""
        );
    }
    
    @Nonnull
    abstract String getStatus();

    public void updateStatus(@Nonnull BlockMenu menu, int slot) {
        if (menu.hasViewer()) {
            menu.replaceExistingItem(slot, getStatusItem());
        }
    }
    
    public void breakBlock() {
        if (this.location != null) {
            LocationUtils.breakBlock(this.location);
        }
    }
    
}
