package io.github.mooy1.simpleutils.implementation.tools;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import io.github.mooy1.infinitylib.common.CoolDowns;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;

public final class Wrench extends SimpleSlimefunItem<ItemUseHandler> implements NotPlaceable {

    private final CoolDowns coolDowns = new CoolDowns(50);

    public Wrench(ItemGroup category, SlimefunItemStack item, RecipeType type, ItemStack[] recipe) {
        super(category, item, type, recipe);
    }

    @Nonnull
    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
            e.setUseItem(Event.Result.DENY);
            e.setUseBlock(Event.Result.DENY);

            if (this.coolDowns.check(e.getPlayer().getUniqueId())
                    && e.getClickedBlock().isPresent() && e.getSlimefunBlock().isPresent()) {

                Block b = e.getClickedBlock().get();
                SlimefunItem sfItem = e.getSlimefunBlock().get();

                if (Slimefun.getProtectionManager().hasPermission(e.getPlayer(), b, Interaction.BREAK_BLOCK)
                        && !sfItem.useVanillaBlockBreaking()
                        && (b.getType() == Material.PLAYER_HEAD || b.getType() == Material.PLAYER_WALL_HEAD || sfItem instanceof EnergyNetComponent)) {

                    this.coolDowns.reset(e.getPlayer().getUniqueId());

                    BlockBreakEvent event = new BlockBreakEvent(b, e.getPlayer());

                    Bukkit.getPluginManager().callEvent(event);

                    if (!event.isCancelled()) {
                        b.setType(Material.AIR);
                    }
                }
            }
        };
    }

}
