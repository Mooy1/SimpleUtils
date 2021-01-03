package io.github.mooy1.gridfoundation.implementation.upgrades;

import org.bukkit.block.Block;

import javax.annotation.Nonnull;

/**
 * Interface for identifying upgradeable blocks
 */
public interface UpgradeableBlock {
    
    @Nonnull
    UpgradeType getMaxLevel();
    
    int getUpgradeSlot();
    
    default int getLevel(@Nonnull Block b) {
        return UpgradeManager.getType(b.getLocation()).level;
    }
    
}
