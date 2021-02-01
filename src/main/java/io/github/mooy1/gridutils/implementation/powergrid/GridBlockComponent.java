package io.github.mooy1.gridutils.implementation.powergrid;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public interface GridBlockComponent {
    
    String INFO = "owner";
    
    default void placeGrid(@Nonnull BlockPlaceEvent e) {
        BlockStorage.addBlockInfo(e.getBlockPlaced(), INFO, e.getPlayer().getUniqueId().toString());
    }

    default void breakGrid(@Nonnull BlockBreakEvent e, @Nonnull BlockMenu menu) {
        removeComponent(e.getBlock(), PowerGridManager.get(BlockStorage.getLocationInfo(e.getBlock().getLocation(), INFO)));
    }

    default void instanceGrid(@Nonnull BlockMenu menu, @Nonnull Block b) {
        addComponent(b, menu.getPreset().getSlimefunItem().getItem(), PowerGridManager.get(BlockStorage.getLocationInfo(b.getLocation(), INFO)));
    }

    void addComponent(Block b, ItemStack item, PowerGrid grid);
    
    void removeComponent(Block b, PowerGrid grid);
    
    void tickComponent(Block b, BlockMenu menu, Config config);
    
}
