package io.github.mooy1.simpleutils.blocks;

import io.github.mooy1.simpleutils.Items;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;

public final class SimpleElevator extends SlimefunItem implements Listener {
    
    public static final SlimefunItemStack ITEM = new SlimefunItemStack(
            "SIMPLE_ELEVATOR",
            Material.QUARTZ_BLOCK,
            "&fSimple Elevator",
            "&7Crouch to go down, Jump to go up"
    );

    public SimpleElevator() {
        super(Items.CATEGORY, ITEM, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                
        });
    }

    @EventHandler
    public void onJump(PlayerMoveEvent e) {
        
    }
    
    @EventHandler
    public void onCrouch(PlayerToggleSneakEvent e) {
        
    }

}
