package io.github.mooy1.gridexpansion.implementation.upgrades;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import javax.annotation.Nonnull;
import java.util.List;

/** 
 * Interface for identifying and adding handlers to Upgradeable Blocks
 */
public interface UpgradeableBlock {

    @Nonnull UpgradeType getMaxLevel();

    /**
     * Return -1 for none
     */
    int getUpgradeSlot();

    void getStats(@Nonnull List<String> stats, int tier);
    
    default int getUpgrade(@Nonnull Config config) {
        return UpgradeManager.getUpgrade(config).tier();
    }

    default void breakUpgrade(@Nonnull BlockBreakEvent e, @Nonnull BlockMenu menu) {
        UpgradeManager.onBreak(e, menu.getPreset().getSlimefunItem().getItem());
    }

    default void placeUpgrade(@Nonnull BlockPlaceEvent e) {
        UpgradeManager.onPlace(e.getBlockPlaced().getLocation(), e.getItemInHand(), this);
    }

    default void instanceUpgrade(@Nonnull BlockMenu menu, @Nonnull Block b) {
        UpgradeManager.updateMenu(menu, UpgradeManager.getUpgrade(b.getLocation()), this);
    }

    default void itemUpgrade(@Nonnull SlimefunItem item) {
        UpgradeManager.addDefaultMeta((SlimefunItemStack) item.getItem());
    }
    
}
