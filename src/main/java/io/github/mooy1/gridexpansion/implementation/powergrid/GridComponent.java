package io.github.mooy1.gridexpansion.implementation.powergrid;

import me.mrCookieSlime.Slimefun.cscorelib2.chat.ChatColors;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * A component in a grid
 */
abstract class GridComponent {
    
    private final ItemStack item;
    private final String status;  
    private final Location location;
    protected final PowerGrid grid;
    
    GridComponent(@Nonnull ItemStack item, @Nonnull Block b, @Nonnull PowerGrid grid) {
        this.item = item;
        this.location = b.getLocation();
        this.status = "&7Location: " + b.getX() + "x " + b.getY() + "y " + b.getZ() + "z " + b.getWorld().getName();
        this.grid = grid;
        add(b.getLocation());
    }
    
    @Nonnull
    protected final ItemStack getViewerItem() {
        ItemStack item = this.item.clone();

        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore != null && lore.size() >= 4) {
            lore = lore.subList(0, lore.size() - 3);
            lore.add(ChatColors.color(getInfo()));
            lore.add(ChatColors.color((this.status)));
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        
        return item;
    }
    
    @Nonnull
    public ItemStack getStatusItem() {
        return new CustomItem(
                this.grid.maxed ? Material.RED_STAINED_GLASS_PANE : this.grid.usage == this.grid.max ? Material.YELLOW_STAINED_GLASS_PANE : Material.LIME_STAINED_GLASS_PANE,
                getInfo(),
                "&7Grid: " + this.grid.usage + " / " + this.grid.max,
                "",
                this.grid.maxed ? "&cGrid Overloaded!" : ""
        );
    }
    
    protected abstract void add(@Nonnull Location l);

    public abstract void remove(@Nonnull Location l);
    
    @Nonnull
    protected abstract String getInfo();

    @Override
    public final int hashCode() {
        return this.location.hashCode();
    }

    @Override
    public final boolean equals(Object obj) {
        return obj instanceof GridComponent && this.location.equals(((GridComponent) obj).location);
    }

}
