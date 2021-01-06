package io.github.mooy1.gridfoundation.implementation.grid;

import io.github.mooy1.infinitylib.menus.LocationUtils;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Interface for blocks which connect to a players grid
 */
public interface GridBlock {

    Map<Location, Grid> locations = new HashMap<>();

    default Grid getGrid(@Nonnull Location l) {
        return locations.get(l);
    }

    default Grid addGrid(@Nonnull Location l) {
        try {
            Grid grid = GridManager.get(UUID.fromString(BlockStorage.getLocationInfo(l, "owner")));
            locations.put(l, grid);
            return grid;
        } catch (IllegalArgumentException | NullPointerException e) {
            LocationUtils.breakBlock(l);
            return null;
        }
    }

    default void storeGrid(@Nonnull Location l, @Nonnull Player p) {
        BlockStorage.addBlockInfo(l, "owner", p.getUniqueId().toString());
    }

    default Grid removeGrid(@Nonnull Location l) {
        return locations.remove(l);
    }
    
}
