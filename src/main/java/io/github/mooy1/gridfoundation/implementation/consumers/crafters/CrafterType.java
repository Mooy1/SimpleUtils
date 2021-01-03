package io.github.mooy1.gridfoundation.implementation.consumers.crafters;

import io.github.mooy1.infinitylib.filter.FilterType;
import io.github.mooy1.infinitylib.filter.MultiFilter;
import lombok.AllArgsConstructor;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.cscorelib2.collections.Pair;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

@AllArgsConstructor
public enum CrafterType {
    
    SLIMEFUN(2) {

    },

    MAGIC(4) {

    },

    VANILLA(2) {

    },

    SMELTERY(4) {
        @Override
        Pair<MultiFilter, ItemStack> getRecipe(@Nonnull ItemStack item) {
            SlimefunItem slimefunItem = SlimefunItem.getByItem(item);
            if (slimefunItem != null && slimefunItem.getRecipe().length == 9 && slimefunItem.getRecipeType() == RecipeType.SMELTERY) {
                return new Pair<>(new MultiFilter(FilterType.MIN_AMOUNT, slimefunItem.getRecipe()), slimefunItem.getRecipeOutput());
            }
            return null;
        }
    };


    
}
