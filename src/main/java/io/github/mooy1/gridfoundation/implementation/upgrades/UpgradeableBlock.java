package io.github.mooy1.gridfoundation.implementation.upgrades;

import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Interface for identifying upgradeable blocks
 */
public interface UpgradeableBlock {
    
    @Nonnull
    UpgradeType getMaxLevel();
    
    int getUpgradeSlot();
    
    void getStats(@Nonnull List<String> stats, int level);
    
    default int getLevel(@Nonnull Block b) {
        return UpgradeManager.getType(b.getLocation()).level;
    }
    
    default void placeUpgrade(@Nonnull Location l, @Nonnull ItemStack item) {
        UpgradeManager.onPlace(l, item, this);
    }

    default void breakUpgrade(@Nonnull Location l, @Nonnull ItemStack item) {
        UpgradeManager.onBreak(l, item);
    }
    
    default void updateMenuUpgrade(@Nonnull BlockMenu menu, @Nonnull Location l) {
        UpgradeManager.updateMenu(menu, UpgradeManager.getType(l), this);
    }
    
}
