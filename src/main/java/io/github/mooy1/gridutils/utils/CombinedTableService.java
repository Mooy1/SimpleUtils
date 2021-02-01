package io.github.mooy1.gridutils.utils;

import io.github.mooy1.infinitylib.PluginUtils;
import io.github.mooy1.infinitylib.filter.FilterType;
import io.github.mooy1.infinitylib.filter.MultiFilter;
import io.github.mooy1.infinitylib.filter.RecipeFilter;
import io.github.thebusybiscuit.slimefun4.core.multiblocks.MultiBlockMachine;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import lombok.experimental.UtilityClass;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@UtilityClass
public final class CombinedTableService {

    private static final Map<MultiFilter, ItemStack> RECIPES = new HashMap<>();
    
    public static void setup() {
        SlimefunPlugin.getMinecraftRecipeService().subscribe(recipeSnapshot -> {
            for (ShapedRecipe recipe : recipeSnapshot.getRecipes(ShapedRecipe.class)) {
                RECIPES.put(RecipeFilter.fromRecipe(recipe, FilterType.IGNORE_AMOUNT), recipe.getResult());
            }
            for (ShapelessRecipe recipe : recipeSnapshot.getRecipes(ShapelessRecipe.class)) {
                RECIPES.put(RecipeFilter.fromRecipe(recipe, FilterType.IGNORE_AMOUNT), recipe.getResult());
            }
        });
        PluginUtils.runSync(() -> {
            List<ItemStack[]> list = ((MultiBlockMachine) RecipeType.ENHANCED_CRAFTING_TABLE.getMachine()).getRecipes();
            for (int i = 0 ; i < list.size() ; i+=2) {
                RECIPES.put(new MultiFilter(FilterType.IGNORE_AMOUNT, list.get(i)), list.get(i + 1)[0]);
            }
        }, 100); 
    }
    
    public static Optional<ItemStack> getOutput(@Nonnull MultiFilter input) {
        return Optional.ofNullable(RECIPES.get(input));
    }
    
}
