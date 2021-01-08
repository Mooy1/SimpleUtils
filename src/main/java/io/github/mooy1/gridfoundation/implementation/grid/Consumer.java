package io.github.mooy1.gridfoundation.implementation.grid;

import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
    
    Consumer(@Nonnull ItemStack item, int consumption, @Nonnull Grid grid, @Nullable Location l) {
        super(item, grid, l);
        this.consumption = consumption;
    }


    @Nonnull
    @Override
    String getStatus() {
        return "&aConsuming: " + this.consumption;
    }

}
