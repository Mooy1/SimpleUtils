package io.github.mooy1.gridexpansion.implementation.tools;

import io.github.mooy1.gridexpansion.setup.Categories;
import io.github.mooy1.infinitylib.PluginUtils;
import io.github.mooy1.infinitylib.menus.TransferUtils;
import io.github.mooy1.infinitylib.player.LeaveListener;
import io.github.mooy1.infinitylib.player.MessageUtils;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.UnregisterReason;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.cscorelib2.collections.Pair;
import me.mrCookieSlime.Slimefun.cscorelib2.protection.ProtectableAction;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Wrench extends SlimefunItem implements NotPlaceable, Listener {

    public static final SlimefunItemStack ITEM = new SlimefunItemStack(
            "GRID_WRENCH",
            Material.DIAMOND_HOE,
            "&9Grid Wrench",
            "&e > &6Left-Click to quickly dismantle a slimefun block",
            "&e > &6Right-Click to rotate a block"
    );
    
    private final Map<UUID, Long> coolDowns = new HashMap<>();
    
    public Wrench() {
        super(Categories.MAIN, ITEM, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                
        });
        PluginUtils.registerListener(this);
        LeaveListener.add(this.coolDowns);
    }
    
    @EventHandler
    public void onInteract(@Nonnull PlayerInteractEvent e) {
        if (e.getClickedBlock() != null) {
            
            SlimefunItem sfItem = SlimefunItem.getByItem(e.getItem());

            if (sfItem instanceof Wrench
                    && SlimefunPlugin.getProtectionManager().hasPermission(e.getPlayer(), e.getClickedBlock(), ProtectableAction.BREAK_BLOCK)
                    && System.currentTimeMillis() - this.coolDowns.getOrDefault(e.getPlayer().getUniqueId(), 0L) > 200
            ) {
                this.coolDowns.put(e.getPlayer().getUniqueId(), System.currentTimeMillis());
                
                e.setCancelled(true);
                e.setUseInteractedBlock(Event.Result.DENY);
                e.setUseItemInHand(Event.Result.DENY);

                if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    rotateBlock(e.getClickedBlock());
                } else if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
                    breakSfBlock(e.getPlayer(), e.getClickedBlock());
                }
            }
        }
    }
    
    private static void breakSfBlock(Player p, Block b) {
        MessageUtils.broadcast("BREAKING");
        
        SlimefunItem sfItem = BlockStorage.check(b);

        if (sfItem != null && !sfItem.useVanillaBlockBreaking()) {
            SlimefunBlockHandler handler = SlimefunPlugin.getRegistry().getBlockHandlers().get(sfItem.getId());

            if (handler == null || handler.onBreak(p, b, sfItem, UnregisterReason.PLAYER_BREAK)) {
                SlimefunPlugin.getProtectionManager().logAction(p, b, ProtectableAction.BREAK_BLOCK);
                
                ItemStack drop = BlockStorage.retrieve(b);
                if (drop != null) {
                    b.getWorld().dropItemNaturally(b.getLocation(), drop);
                }
            }
        }
    }
    
    private static void rotateBlock(Block b) {
        
        // double chests should both be turned opposite directions
        Inventory doubleChest = TransferUtils.getInventory(b);
        if (doubleChest != null) {
            Pair<Location, Location> pair = TransferUtils.getBothChests(doubleChest);
            if (pair != null) {
                reverseFace(pair.getFirstValue().getBlock(), Chest.Type.LEFT);
                reverseFace(pair.getSecondValue().getBlock(), Chest.Type.RIGHT);
                return;
            }
        }

        BlockData blockData = b.getBlockData();

        if (blockData instanceof Directional) {
            Directional data = (Directional) b.getBlockData();
            BlockFace facing = data.getFacing();
            BlockFace[] array = data.getFaces().toArray(new BlockFace[0]);
            
            for (int i = 0 ; i < array.length - 1 ; i++) {
                if (array[i] == facing) {
                    data.setFacing(array[i + 1]);
                    b.setBlockData(data, true);
                    return;
                }
            }
            
            // if current is last face then set to first
            data.setFacing(array[0]);
            b.setBlockData(data, true);
        }
    }
    
    private static void reverseFace(@Nonnull Block b, @Nonnull Chest.Type type) {
        BlockData blockData = b.getBlockData();
        if (blockData instanceof Chest) {
            Chest data = (Chest) blockData;
            data.setFacing(data.getFacing().getOppositeFace());
            data.setType(type);
            b.setBlockData(data, true);
        }
    }
    
}
