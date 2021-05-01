package io.github.mooy1.simpleutils.blocks;

import java.util.List;
import javax.annotation.Nonnull;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.github.mooy1.simpleutils.SimpleUtils;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
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
        }, new BlockPlaceHandler(false) {
            @Override
            public void onPlayerPlace(@Nonnull BlockPlaceEvent e) {
                Location start = e.getBlock().getLocation();
                Location loc = start;
                for (int y = (int) loc.getY() + 1 ; y < 256 ; y++) {
                    if (BlockStorage.check(loc = loc.add(0, 1, 0), getId())) {
                        Elevator.this.locations.put(start, loc.add(0, 1, 0));
                        break;
                    }
                }
                loc = start;
                for (int y = (int) loc.getY() - 1 ; y >= 0 ; y--) {
                    if (BlockStorage.check(loc = loc.subtract(0, 1, 0), getId())) {
                        Elevator.this.locations.put(loc.add(0, 1, 0), start);
                        break;
                    }
                }
            }
        });
    }

    @EventHandler
    private void onJump(@Nonnull PlayerMoveEvent e) {
        if (e.getTo() != null && e.getTo().getY() > e.getFrom().getY()
                && e.getFrom().getY() - e.getFrom().getBlockY() < 0.05
                && !e.getFrom().subtract(0, 1, 0).getBlock().isEmpty()) {
            Location l = this.locations.computeIfAbsent(e.getFrom().getBlock().getLocation(), loc -> {
                for (int y = (int) loc.getY() + 1 ; y < 256 ; y++) {
                    if (BlockStorage.check(loc = loc.add(0, 1, 0), getId())) {
                        return loc.add(0, 1, 0);
                    }
                }
                return null;
            });
            if (l != null) {
                e.getPlayer().teleport(l, PlayerTeleportEvent.TeleportCause.PLUGIN);
            }
        }
    }

    @EventHandler
    private void onCrouch(@Nonnull PlayerToggleSneakEvent e) {
        if (e.isSneaking()) {
            Location l = this.locations.inverse().computeIfAbsent(e.getPlayer().getLocation().getBlock().getLocation(), loc -> {
                for (int y = (int) loc.getY() - 1 ; y >= 0 ; y--) {
                    if (BlockStorage.check(loc = loc.subtract(0, 1, 0), getId())) {
                        return loc.add(0, 1, 0);
                    }
                }
                return null;
            });
            if (l != null) {
                e.getPlayer().teleport(l, PlayerTeleportEvent.TeleportCause.PLUGIN);
            }
        }
    }

}
