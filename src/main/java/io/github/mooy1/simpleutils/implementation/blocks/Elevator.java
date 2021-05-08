package io.github.mooy1.simpleutils.implementation.blocks;

import java.util.List;
import javax.annotation.Nonnull;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.github.mooy1.simpleutils.SimpleUtils;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public final class Elevator extends SlimefunItem implements Listener {

    private final BiMap<Location, Location> locations = HashBiMap.create();

    public Elevator(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
        SimpleUtils.inst().registerListener(this);
        addItemHandler(new BlockBreakHandler(false, false) {
            @Override
            public void onPlayerBreak(@Nonnull BlockBreakEvent e, @Nonnull ItemStack item, @Nonnull List<ItemStack> drops) {
                Location l = e.getBlock().getLocation();
                Elevator.this.locations.remove(l);
                Elevator.this.locations.inverse().remove(l);
            }
        });
    }

    @EventHandler
    private void onJump(@Nonnull PlayerMoveEvent e) {
        if (e.getTo() != null && e.getTo().getY() > e.getFrom().getY() && e.getFrom().getY() - e.getFrom().getBlockY() < 0.05) {
            Location check = elevatorUnder(e.getFrom());
            if (BlockStorage.check(check, getId())) {
                teleport(e, this.locations.computeIfAbsent(check, elev -> {
                    elev.add(0, 2, 0);
                    while (elev.add(0, 1, 0).getY() < 256) {
                        if (BlockStorage.check(elev, getId())) {
                            return elev;
                        }
                    }
                    return null;
                }));
            }
        }
    }

    @EventHandler
    private void onCrouch(@Nonnull PlayerToggleSneakEvent e) {
        if (e.isSneaking()) {
            Location check = elevatorUnder(e.getPlayer().getLocation());
            if (BlockStorage.check(check, getId())) {
                teleport(e, this.locations.inverse().computeIfAbsent(check, elev -> {
                    elev.subtract(0, 2, 0);
                    while (elev.subtract(0, 1, 0).getY() > -1) {
                        if (BlockStorage.check(elev, getId())) {
                            return elev;
                        }
                    }
                    return null;
                }));
            }
        }
    }
    
    private static Location elevatorUnder(Location l) {
        return l.subtract(0, 1, 0).getBlock().getLocation();
    }
    
    private static void teleport(PlayerEvent e, Location to) {
        if (to != null) {
            to.setPitch(e.getPlayer().getLocation().getPitch());
            to.setYaw(e.getPlayer().getLocation().getYaw());
            e.getPlayer().teleport(to.add(.5, 1, .5));
            e.getPlayer().playSound(to, Sound.BLOCK_PISTON_CONTRACT, 1.0F, 1.8F);
        }
    }

}
