package io.github.mooy1.simpleutils.implementation.blocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.mooy1.infinitylib.common.Scheduler;
import io.github.mooy1.infinitylib.common.StackUtils;
import io.github.mooy1.simpleutils.SimpleUtils;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.core.multiblocks.MultiBlockMachine;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.OutputChest;
import io.github.thebusybiscuit.slimefun4.libraries.dough.collections.RandomizedSet;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.ItemUtils;

public final class Sieve extends MultiBlockMachine {

    private final float itemChance = (float) SimpleUtils.config().getDouble("sieve-item-chance", 0, 100);
    private final RandomizedSet<ItemStack> recipes = new RandomizedSet<>();
    private final List<ItemStack> display = new ArrayList<>();

    public Sieve(ItemGroup category, SlimefunItemStack item, ItemStack[] recipe, BlockFace face) {
        super(category, item, recipe, face);

        this.recipes.add(new ItemStack(Material.AIR), 25 * (100 - itemChance));
        addRecipe(SlimefunItems.ALUMINUM_DUST, 2);
        addRecipe(SlimefunItems.COPPER_DUST, 3);
        addRecipe(SlimefunItems.IRON_DUST, 2);
        addRecipe(SlimefunItems.LEAD_DUST, 1);
        addRecipe(SlimefunItems.GOLD_DUST, 2);
        addRecipe(SlimefunItems.ZINC_DUST, 2);
        addRecipe(SlimefunItems.MAGNESIUM_DUST, 1);
        addRecipe(SlimefunItems.SILVER_DUST, 1);
        addRecipe(SlimefunItems.TIN_DUST, 1);
        addRecipe(new ItemStack(Material.CLAY_BALL), 4);
        addRecipe(new ItemStack(Material.FLINT), 3);
        addRecipe(new ItemStack(Material.IRON_NUGGET), 3);
    }

    private void addRecipe(ItemStack item, float chance) {
        float finalChance = chance * itemChance;
        this.recipes.add(item, finalChance);
        this.displayRecipes.add(new ItemStack(Material.GRAVEL));
        this.displayRecipes.add(new CustomItemStack(item, itemMeta -> itemMeta.setLore(Arrays.asList("", "&6Chance: " + finalChance))));
    }

    @Nonnull
    @Override
    public List<ItemStack> getDisplayRecipes() {
        return this.display;
    }

    @Override
    public void onInteract(Player p, Block b) {
        ItemStack input = p.getInventory().getItemInMainHand();

        if (StackUtils.getId(input) != null || input.getType() != Material.GRAVEL) {
            p.sendMessage(ChatColor.RED + "Invalid Recipe!");
            return;
        }

        ItemStack item = this.recipes.getRandom();

        if (p.getGameMode() != GameMode.CREATIVE) {
            ItemUtils.consumeItem(input, 1, false);
        }

        if (item.getType() == Material.AIR) {
            return;
        }

        p.playSound(b.getLocation(), Sound.BLOCK_SAND_BREAK, 1, 1);

        ItemStack output = item.clone();

        Scheduler.run(40, () -> {
            Optional<Inventory> outputChest = OutputChest.findOutputChestFor(b.getRelative(BlockFace.DOWN), output);
            if (outputChest.isPresent()) {
                outputChest.get().addItem(output);
            } else {
                b.getWorld().dropItemNaturally(b.getLocation().add(0, .5, 0), output);
            }
            p.playSound(b.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1);
        });
    }

}
