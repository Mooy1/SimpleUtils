package io.github.mooy1.simpleutils.tools;

import io.github.mooy1.infinitylib.player.LeaveListener;
import io.github.mooy1.simpleutils.Items;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.UnregisterReason;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.cscorelib2.protection.ProtectableAction;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class SimpleWrench extends SimpleSlimefunItem<ItemUseHandler> implements NotPlaceable {
    
    public static final SlimefunItemStack ITEM =new SlimefunItemStack(
            "SIMPLE_WRENCH",
            Material.DIAMOND_HOE,
            "&6Simple Wrench",
            "&eRight-Click to quickly dismantle cargo, capacitors, and machines"
    );
    
    private final Map<UUID, Long> coolDowns = new HashMap<>();

    public SimpleWrench() {
        super(Items.CATEGORY, ITEM, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                SlimefunItems.ALUMINUM_INGOT, null, SlimefunItems.ALUMINUM_INGOT,
                null, SlimefunItems.SILVER_INGOT, null,
                null, SlimefunItems.ALUMINUM_INGOT, null
        });
        LeaveListener.add(this.coolDowns);
    }

    @Nonnull
    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
            e.setUseItem(Event.Result.DENY);
            e.setUseBlock(Event.Result.DENY);
            
            if (System.currentTimeMillis() - this.coolDowns.getOrDefault(e.getPlayer().getUniqueId(), 0L) > 20 
                    && e.getClickedBlock().isPresent() && e.getSlimefunBlock().isPresent()) {
                
                Block b = e.getClickedBlock().get();
                SlimefunItem sfItem = e.getSlimefunBlock().get();

                if (SlimefunPlugin.getProtectionManager().hasPermission(e.getPlayer(), e.getClickedBlock().get(), ProtectableAction.BREAK_BLOCK)
                        && !sfItem.useVanillaBlockBreaking()
                        && (b.getType() == Material.PLAYER_HEAD || b.getType() == Material.PLAYER_WALL_HEAD || sfItem instanceof EnergyNetComponent)) {
                    
                    SlimefunBlockHandler handler = SlimefunPlugin.getRegistry().getBlockHandlers().get(sfItem.getId());
                    
                    this.coolDowns.put(e.getPlayer().getUniqueId(), System.currentTimeMillis());

                    if (handler == null || handler.onBreak(e.getPlayer(), b, sfItem, UnregisterReason.PLAYER_BREAK)) {
                        SlimefunPlugin.getProtectionManager().logAction(e.getPlayer(), b, ProtectableAction.BREAK_BLOCK);

                        b.setType(Material.AIR);
                        BlockStorage.clearBlockInfo(b);
                        b.getWorld().dropItemNaturally(b.getLocation(), sfItem.getItem().clone());
                    }
                }
            }
        };
    }
    
}
