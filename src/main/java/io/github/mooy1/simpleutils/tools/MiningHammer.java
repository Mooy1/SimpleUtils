package io.github.mooy1.simpleutils.tools;

import io.github.mooy1.infinitylib.items.StackUtils;
import io.github.mooy1.simpleutils.Materials;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import io.github.thebusybiscuit.slimefun4.core.handlers.ToolUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import io.github.thebusybiscuit.slimefun4.utils.tags.SlimefunTag;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
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
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.Objects;

public final class MiningHammer extends SimpleSlimefunItem<ToolUseHandler> implements NotPlaceable {
    
    private final int start;
    private final int side;
    private final int top;
    private final int blocks;
    
    public MiningHammer(Category category, Material material, ItemStack metal, ChatColor color, int size, int eff, int unb) {
        super(category, new SlimefunItemStack(
                Objects.requireNonNull(StackUtils.getIDorType(metal)).replace("_INGOT", "") + "_MINING_HAMMER",
                material,
                color + ChatColor.stripColor(ItemUtils.getItemName(metal)).replace(" Ingot", "").concat(" Hammer"),
                "&7Mines in a " + size + "x" + size + " area"
        ), RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                metal, metal, metal,
                metal, Materials.HAMMER_ROD, metal,
                null, Materials.HAMMER_ROD, null
        });
        if (eff > 0) {
            getItem().addUnsafeEnchantment(Enchantment.DIG_SPEED, eff);
        }
        if (unb > 0) {
            getItem().addUnsafeEnchantment(Enchantment.DURABILITY, unb);
        }
        
        // # of extra blocks that will be mined
        this.blocks = size * size - 1;
        
        // corners for mining
        this.start = -((size - 1) >> 1);
        this.side = size >> 1;
        this.top = size - 2;
    }
    
    @Nonnull
    @Override
    public ToolUseHandler getItemHandler() {
        return (e, item, fortune, drops) -> {
            Player p = e.getPlayer();
            
            if (p.isSneaking() || !SlimefunPlugin.getProtectionManager().hasPermission(p, e.getBlock(), ProtectableAction.BREAK_BLOCK)) {
                return;
            }
            
            for (Block b : getBlocks(e.getBlock(), p.getFacing(), p.getLocation().getPitch())) {
                if (canBreak(p, b)) {
                    breakBlock(p, item, b, fortune);
                }
            }
        };
    }
    
    private static boolean canBreak(Player p, Block b) {
        return !b.isEmpty()
                && !b.isLiquid()
                && !SlimefunTag.UNBREAKABLE_MATERIALS.isTagged(b.getType())
                && SlimefunPlugin.getProtectionManager().hasPermission(p, b.getLocation(), ProtectableAction.BREAK_BLOCK)
                && !BlockStorage.hasBlockInfo(b);
    }

    private static void breakBlock(Player p, ItemStack item, Block b, int fortune) {
        SlimefunPlugin.getProtectionManager().logAction(p, b, ProtectableAction.BREAK_BLOCK);
        b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getType());

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
    
    private Block[] getBlocks(Block middle, BlockFace face, float pitch) {
        Block[] arr = new Block[this.blocks];
        int index = 0;
        if (pitch > 45 || pitch < -45) {
            for (int x = this.start ; x <= this.side ; x++) {
                for (int z = -1 ; z <= this.top ; z++) {
                    if (x != 0 || z != 0) {
                        arr[index++] = middle.getRelative(x, 0, z);
                    }
                }
            }
        } else if (face == BlockFace.NORTH || face == BlockFace.SOUTH) {
            for (int x = this.start ; x <= this.side ; x++) {
                for (int y = -1 ; y <= this.top ; y++) {
                    if (y != 0 || x != 0) {
                        arr[index++] = middle.getRelative(x, y, 0);
                    }
                }
            }
        } else if (face == BlockFace.EAST || face == BlockFace.WEST) {
            for (int z = this.start ; z <= this.side ; z++) {
                for (int y = -1 ; y <= this.top ; y++) {
                    if (z !=0 || y != 0) {
                        arr[index++] = middle.getRelative(0, y, z);
                    }
                }
            }
        }
        return arr;
    }

}
