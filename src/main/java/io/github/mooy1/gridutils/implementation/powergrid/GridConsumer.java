package io.github.mooy1.gridutils.implementation.powergrid;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.graalvm.compiler.graph.Node;

import javax.annotation.Nonnull;

/**
 * represents a consumer in a power grid
 */
public final class GridConsumer extends GridComponent {
    
    final int consumption;
    boolean consume;
    
    public boolean canConsume() {
        return this.consume;
    }
    
    GridConsumer(@Nonnull ItemStack item, int consumption, @Node.ConstantNodeParameter Block b) {
        super(item, b);
        this.consumption = consumption;
    }


    @Nonnull
    @Override
    protected String getViewerInfo() {
        return "&aConsuming: " + this.consumption;
    }

}
