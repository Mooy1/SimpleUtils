package io.github.mooy1.gridutils.implementation.powergrid;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public interface GridBlockGenerator extends GridBlockComponent {

    Map<Location, GridGenerator> GENERATORS = new HashMap<>();

    int generate(BlockMenu menu, Block b, Config config);

    @Override
    default void tickComponent(Block b, BlockMenu menu, Config config) {
        GridGenerator generator = GENERATORS.get(b.getLocation());
        if (generator != null) {
            generator.setGeneration(generate(menu, b, config));
            if (menu.hasViewer()) {
                // update status in slot of generator
            }
        }
    }

    @Override
    default void removeComponent(@Nonnull Block b, @Nonnull PowerGrid grid) {
        grid.removeGenerator(GENERATORS.remove(b.getLocation()));
    }

    @Override
    default void addComponent(@Nonnull Block b, ItemStack item, @Nonnull PowerGrid grid) {
        GENERATORS.put(b.getLocation(), grid.addGenerator(new GridGenerator(item, b)));
    }
    
}
