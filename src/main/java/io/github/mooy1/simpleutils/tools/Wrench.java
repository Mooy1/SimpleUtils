package io.github.mooy1.simpleutils.tools;

import io.github.mooy1.infinitylib.players.CoolDownMap;
import io.github.mooy1.simpleutils.SimpleUtils;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import javax.annotation.Nonnull;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.cscorelib2.protection.ProtectableAction;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public final class Wrench extends SimpleSlimefunItem<ItemUseHandler> implements NotPlaceable {
    
    private final CoolDownMap coolDowns = new CoolDownMap(SimpleUtils.inst());

    public Wrench(Category category, SlimefunItemStack item, RecipeType type, ItemStack[] recipe) {
        super(category, item, type, recipe);
    }

    @Nonnull
    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
            e.setUseItem(Event.Result.DENY);
            e.setUseBlock(Event.Result.DENY);
            
            if (this.coolDowns.check(e.getPlayer().getUniqueId(), 50)
                    && e.getClickedBlock().isPresent() && e.getSlimefunBlock().isPresent()) {
                
                Block b = e.getClickedBlock().get();
                SlimefunItem sfItem = e.getSlimefunBlock().get();

                if (SlimefunPlugin.getProtectionManager().hasPermission(e.getPlayer(), b, ProtectableAction.BREAK_BLOCK)
                        && !sfItem.useVanillaBlockBreaking()
                        && (b.getType() == Material.PLAYER_HEAD || b.getType() == Material.PLAYER_WALL_HEAD || sfItem instanceof EnergyNetComponent)) {
                    
                    this.coolDowns.put(e.getPlayer().getUniqueId());

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
