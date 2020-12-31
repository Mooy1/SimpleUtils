package io.github.mooy1.slimegrid.implementation.consumers.crafters;

import io.github.mooy1.infinitylib.filter.ItemFilter;
import io.github.mooy1.infinitylib.filter.MultiFilter;
import io.github.mooy1.slimegrid.lists.Items;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.cscorelib2.collections.Pair;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class SlimefunAutoCrafter extends AbstractAutoCrafter {
    
    public static final int GP = 16;

    public SlimefunAutoCrafter() {
        super(GP, Items.SLIMEFUN_AUTO_CRAFTER, new ItemStack[] {
                Items.SILVER_WIRE, Items.INFUSED_CRYSTAL, Items.SILVER_WIRE,
                Items.GRID_CIRCUIT_II, new ItemStack(Material.CRAFTING_TABLE), Items.GRID_CIRCUIT_II,
                Items.SILVER_WIRE, Items.INFUSED_CRYSTAL, Items.SILVER_WIRE
        });
    }

    @Override
    public boolean updateCache(@Nonnull Block b, @Nonnull ItemStack item) {
        if (item.getType() == Material.AIR) {
            this.cache.put(b.getLocation(), null);
        } else {
            SlimefunItem slimefunItem = SlimefunItem.getByItem(item);

            if (slimefunItem != null) {
                Pair<MultiFilter, ItemStack> pair = getSlimefunRecipe(slimefunItem);

                if (pair != null) {
                    this.cache.put(b.getLocation(), pair);
                    return true;
                }
            }
        }
        return false;
    }

    @Nullable
    private Pair<MultiFilter, ItemStack> getSlimefunRecipe(@Nonnull SlimefunItem slimefunItem) {
        if (slimefunItem.getRecipeType() == RecipeType.ENHANCED_CRAFTING_TABLE || slimefunItem.getRecipeType() == RecipeType.ARMOR_FORGE) {
            ItemFilter[] array = new ItemFilter[9];
            for (int i = 0 ; i < 9 ; i ++) {
                ItemStack item = slimefunItem.getRecipe()[i];
                if (item != null) {
                    array[i] = new ItemFilter(item);
                }
            }
            return new Pair<>(new MultiFilter(array), slimefunItem.getRecipeOutput());
        }
        return null;
    }

}
