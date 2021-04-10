package io.github.mooy1.simpleutils.blocks;

import io.github.mooy1.infinitylib.items.LoreUtils;
import io.github.mooy1.infinitylib.items.StackUtils;
import io.github.mooy1.simpleutils.SimpleUtils;
import io.github.thebusybiscuit.slimefun4.core.multiblocks.MultiBlockMachine;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.cscorelib2.collections.RandomizedSet;
import me.mrCookieSlime.Slimefun.cscorelib2.inventory.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public final class Sieve extends MultiBlockMachine {

    private final RandomizedSet<ItemStack> recipes = new RandomizedSet<>();
    private final List<ItemStack> display = new ArrayList<>();
    
    public Sieve(Category category, SlimefunItemStack item, ItemStack[] recipe, BlockFace face) {
        super(category, item, recipe, face);

        this.recipes.add(new ItemStack(Material.AIR), 72);
        addRecipe(SlimefunItems.ALUMINUM_DUST, 2);
        addRecipe(SlimefunItems.COPPER_DUST, 3);
        addRecipe(SlimefunItems.IRON_DUST, 2);
        addRecipe(SlimefunItems.LEAD_DUST, 1);
        addRecipe(SlimefunItems.GOLD_DUST, 2);
        addRecipe(SlimefunItems.ZINC_DUST, 2);
        addRecipe(SlimefunItems.MAGNESIUM_DUST, 1);
        addRecipe(SlimefunItems.SILVER_DUST, 1);
        addRecipe(SlimefunItems.TIN_DUST, 1);
        addRecipe(new ItemStack(Material.CLAY_BALL), 5);
        addRecipe(new ItemStack(Material.FLINT), 5);
        addRecipe(new ItemStack(Material.IRON_NUGGET), 3);
    }
    
    private void addRecipe(ItemStack item, int chance) {
        this.recipes.add(item, chance);
        this.displayRecipes.add(new ItemStack(Material.GRAVEL));
        ItemStack clone = item.clone();
        LoreUtils.addLore(clone, "", "&6Chance: " + chance);
        this.displayRecipes.add(clone);
    }

    @Nonnull
    @Override
    public List<ItemStack> getDisplayRecipes() {
        return this.display;
    }

    @Override
    public void onInteract(Player p, Block b) {
        ItemStack input = p.getInventory().getItemInMainHand();
        
        if (StackUtils.getID(input) != null || input.getType() != Material.GRAVEL) {
            p.sendMessage(ChatColor.RED + "Invalid Recipe!");
            return;
        }
        
        ItemStack item = this.recipes.getRandom();
        
        if (p.getGameMode() != GameMode.CREATIVE) {
            ItemUtils.consumeItem(input, 1,false);
        }
        
        if (item.getType() == Material.AIR) {
            return;
        }

        p.playSound(b.getLocation(), Sound.BLOCK_SAND_BREAK, 1, 1);
        
        ItemStack output = item.clone();
        
        Inventory outputChest = findOutputChest(b.getRelative(BlockFace.DOWN), output);

        if (outputChest != null) {
            SimpleUtils.inst().runSync(() -> {
                outputChest.addItem(output);
                p.playSound(b.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1);
            }, 40);
        } else {
            SimpleUtils.inst().runSync(() -> {
                b.getWorld().dropItemNaturally(b.getLocation().add(0, .5, 0), output);
                p.playSound(b.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1);
            }, 40);
        }
    }

}
