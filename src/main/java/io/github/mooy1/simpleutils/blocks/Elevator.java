package io.github.mooy1.simpleutils.blocks;

import io.github.mooy1.simpleutils.SimpleUtils;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;

public final class Elevator extends SlimefunItem implements Listener {

    public Elevator(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
        SimpleUtils.inst().registerListener(this);
    }

    @EventHandler
    public void onJump(PlayerMoveEvent e) {
        
    }
    
    @EventHandler
    public void onCrouch(PlayerToggleSneakEvent e) {
        
    }

}
