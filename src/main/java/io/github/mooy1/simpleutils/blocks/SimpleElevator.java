package io.github.mooy1.simpleutils.blocks;

import io.github.mooy1.infinitylib.PluginUtils;
import io.github.mooy1.simpleutils.Items;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public final class SimpleElevator extends SlimefunItem implements Listener {
    
    public static final SlimefunItemStack ITEM = new SlimefunItemStack(
            "SIMPLE_ELEVATOR",
            Material.QUARTZ_BLOCK,
            "&fSimple Elevator",
            "&7Crouch to go down, Jump to go up"
    );

    private final Map<Location, Location> up = new HashMap<>();
    private final Map<Location, Location> down = new HashMap<>();

    public SimpleElevator() {
        super(Items.CATEGORY, ITEM, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                
        });
        PluginUtils.registerListener(this);
    }

    @EventHandler
    public void onJump(PlayerMoveEvent e) {
        
    }
    
    @EventHandler
    public void onCrouch(PlayerToggleSneakEvent e) {
        if (!e.isSneaking()) {
            return;
        }
        Block ground = e.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN);
        if (e.getPlayer().getLocation().getY() - ground.getLocation().getY() > .05 || !isElevator(ground, ground.getLocation())) {
            return;
        }
        Location destination = this.down.get(ground.getLocation());
        if (destination == null || !isElevator(destination.getBlock(), destination)) {
            
        }
    }
    
    private boolean isElevator(Block b, Location l) {
        return b.getType() != getItem().getType() && getId().equals(BlockStorage.getLocationInfo(l, "id"));
    }

}
