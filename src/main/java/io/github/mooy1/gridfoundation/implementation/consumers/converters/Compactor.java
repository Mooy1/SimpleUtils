package io.github.mooy1.gridfoundation.implementation.consumers.converters;

import io.github.mooy1.gridfoundation.implementation.upgrades.UpgradeType;
import io.github.mooy1.gridfoundation.utils.MachineRecipeService;
import io.github.mooy1.infinitylib.filter.FilterType;
import io.github.mooy1.infinitylib.filter.ItemFilter;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.cscorelib2.collections.Pair;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Compactor extends AbstractConverter {

    public static final Map<ItemFilter, Pair<ItemStack, Integer>> recipes = new HashMap<>();
    private static final List<ItemStack> displayRecipes = new ArrayList<>();
    public static final SlimefunItemStack ITEM = make(6, "Compactor", "Compresses ingots and other materials into blocks", Material.SMOOTH_SANDSTONE);
    
    public Compactor() {
        super(ITEM, displayRecipes, recipes, 6, new ItemStack[] {
                
        });
        MachineRecipeService.accept(SlimefunItems.CARBON_PRESS, Compactor::addRecipe);
        MachineRecipeService.accept(SlimefunItems.ELECTRIC_PRESS, Compactor::addRecipe);
    }
    
    private static void addRecipe(ItemStack input, ItemStack stack) {
        displayRecipes.add(input);
        displayRecipes.add(stack);
        recipes.put(new ItemFilter(input, FilterType.MIN_AMOUNT), new Pair<>(stack, input.getAmount()));
    }
    
    @Nonnull
    @Override
    public UpgradeType getMaxLevel() {
        return UpgradeType.HARDENED;
    }

}
