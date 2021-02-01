package io.github.mooy1.gridutils.implementation.powergrid;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public interface GridBlockConsumer extends GridBlockComponent {

    Map<Location, GridConsumer> CONSUMERS = new HashMap<>();

    @Override
    default void tickComponent(@Nonnull Block b, BlockMenu menu, Config config) {
        GridConsumer consumer = CONSUMERS.get(b.getLocation());
        if (consumer != null && consumer.canConsume()) {
            process(b, menu, config);
        }
    }

    @Override
    default void removeComponent(@Nonnull Block b, @Nonnull PowerGrid grid) {
        grid.removeConsumer(CONSUMERS.remove(b.getLocation()));
    }

    @Override
    default void addComponent(@Nonnull Block b, ItemStack item, @Nonnull PowerGrid grid) {
        CONSUMERS.put(b.getLocation(), grid.addConsumer(new GridConsumer(item, getConsumption(), b)));
    }
    
    void process(@Nonnull Block b, BlockMenu menu, Config config);
    
    int getConsumption();
    
}
