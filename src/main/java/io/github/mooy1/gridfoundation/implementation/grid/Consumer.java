package io.github.mooy1.gridfoundation.implementation.grid;

import lombok.Setter;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * represents a consumer in a grid
 */
public final class Consumer extends Component {


    @Setter
    int consumption;
    
    boolean consume = false;
    
    public boolean canConsume() {
        return this.consume;
    }
    
    Consumer(@Nonnull ItemStack item, int consumption, @Nonnull Grid grid) {
        super(item, grid);
        this.consumption = consumption;
    }

    @Nonnull
    @Override
    ItemStack getViewerItem() {
        return null;
    }

    @Nonnull
    @Override
    public ItemStack getStatusItem() {
        return new CustomItem(
                this.grid.maxed ? Material.RED_STAINED_GLASS_PANE : this.grid.usage == this.grid.max ? Material.YELLOW_STAINED_GLASS_PANE : Material.LIME_STAINED_GLASS_PANE,
                "&aConsuming: " + this.consumption + " GP",
                "&7Grid: " + this.grid.usage + " / " + this.grid.max,
                "&7Owner: " + this.grid.uuid,
                "",
                this.grid.maxed ? "&cGrid Overloaded!" : ""
        );
    }
    
}
