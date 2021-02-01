package io.github.mooy1.gridutils.implementation.tools;

import io.github.mooy1.gridutils.setup.Categories;
import io.github.mooy1.infinitylib.items.StackUtils;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import io.github.thebusybiscuit.slimefun4.core.handlers.ToolUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import io.github.thebusybiscuit.slimefun4.implementation.items.tools.ExplosivePickaxe;
import io.github.thebusybiscuit.slimefun4.utils.tags.SlimefunTag;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.UnregisterReason;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.cscorelib2.inventory.ItemUtils;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import me.mrCookieSlime.Slimefun.cscorelib2.protection.ProtectableAction;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Hammer extends SimpleSlimefunItem<ToolUseHandler> implements NotPlaceable {
    
    private final int level;
    private final int blocks;
    
    public Hammer(Material material, ItemStack metal, ChatColor color, int size) {
        super(Categories.MAIN, new SlimefunItemStack(
                Objects.requireNonNull(StackUtils.getIDorType(metal)).replace("_ALLOY", "").replace("_INGOT", "") + "_MINING_HAMMER",
                material,
                color + ChatColor.stripColor(ItemUtils.getItemName(metal)).replace("Ingot", "") + " Hammer",
                "&7Mines in a " + size + "x" + size + " area"
        ), RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                metal, metal, metal,
                metal, new ItemStack(Material.STICK), metal,
                null, new ItemStack(Material.STICK), null
        });
        this.level = (size - 1) >>> 1;
        this.blocks = (size * size) - 1;
    }
    
    @Nonnull
    @Override
    public ToolUseHandler getItemHandler() {
        return (e, item, fortune, drops) -> {
            Player p = e.getPlayer();
            
            if (p.isSneaking() || !SlimefunPlugin.getProtectionManager().hasPermission(p, e.getBlock(), ProtectableAction.BREAK_BLOCK)) {
                return;
            }

            List<Block> blocks = getBlocks(e.getBlock(), p.getFacing(), p.getLocation().getPitch());
            
            for (Block b : blocks) {
                if (canBreak(p, b)) {
                    breakBlock(p, item, b, fortune, drops);
                }
            }
        };
    }
    
    private static boolean canBreak(Player p, Block b) {
        return !b.isEmpty()
                && !b.isLiquid()
                && !SlimefunTag.UNBREAKABLE_MATERIALS.isTagged(b.getType())
                && SlimefunPlugin.getProtectionManager().hasPermission(p, b.getLocation(), ProtectableAction.BREAK_BLOCK);
    }

    /**
     * Breaks a block, this is pretty much the same as the {@link ExplosivePickaxe}'s method
     */
    private static void breakBlock(Player p, ItemStack item, Block b, int fortune, List<ItemStack> drops) {
        SlimefunPlugin.getProtectionManager().logAction(p, b, ProtectableAction.BREAK_BLOCK);
        b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getType());

        SlimefunItem sfItem = BlockStorage.check(b);
        
        if (sfItem != null && !sfItem.useVanillaBlockBreaking()) {
            SlimefunBlockHandler handler = SlimefunPlugin.getRegistry().getBlockHandlers().get(sfItem.getId());

            if (handler != null && !handler.onBreak(p, b, sfItem, UnregisterReason.PLAYER_BREAK)) {
                drops.add(BlockStorage.retrieve(b));
            }
            
        } else {
            Material type = b.getType();
            
            if (type == Material.PLAYER_HEAD || SlimefunTag.SHULKER_BOXES.isTagged(type)) {
                b.breakNaturally(item);
            } else {
                boolean applyFortune = SlimefunTag.FORTUNE_COMPATIBLE_ORES.isTagged(type);

                for (ItemStack drop : b.getDrops(item)) {
                    if (drop != null && drop.getType() != Material.AIR) {
                        b.getWorld().dropItemNaturally(b.getLocation(), applyFortune ? new CustomItem(drop, fortune) : drop);
                    }
                }

                b.setType(Material.AIR);
            }
        }
    }
    
    private List<Block> getBlocks(Block middle, BlockFace face, float pitch) {
        List<Block> blocks = new ArrayList<>(this.blocks);
        if (pitch > 45 || pitch < -45) {
            for (int x = -this.level ; x <= this.level ; x++) {
                for (int z = -this.level ; z <= this.level ; z++) {
                    if (x != 0 || z != 0) {
                        blocks.add(middle.getRelative(x, 0, z));
                    }
                }
            }
        } else if (face == BlockFace.NORTH || face == BlockFace.SOUTH) {
            for (int x = -this.level ; x <= this.level ; x++) {
                for (int y = -1 ; y < this.level * 2 ; y++) {
                    if (y != 0 | x != 0) {
                        blocks.add(middle.getRelative(x, y, 0));
                    }
                }
            }
        } else if (face == BlockFace.EAST || face == BlockFace.WEST) {
            for (int z = -this.level ; z <= this.level ; z++) {
                for (int y = -1 ; y < this.level * 2 ; y++) {
                    if (z !=0 || y != 0) {
                        blocks.add(middle.getRelative(0, y, z));
                    }
                }
            }
        }
        return blocks;
    }

}
