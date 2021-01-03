package io.github.mooy1.gridfoundation.implementation.consumers.machines;

import io.github.mooy1.infinitylib.filter.FilterType;
import io.github.mooy1.infinitylib.filter.ItemFilter;
import io.github.mooy1.gridfoundation.implementation.upgrades.UpgradeType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.cscorelib2.collections.Pair;
import org.bukkit.Material;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Pulverizer extends AbstractProcessingMachine {

    public static final Pair<List<ItemStack>, Map<ItemFilter, Pair<ItemStack, Integer>>> furnaceRecipes = makeFurnaceRecipes();

    public Pulverizer() {
        super(new SlimefunItemStack(
                
        ), UpgradeType.ULTIMATE, furnaceRecipes, 4, new ItemStack[] {
                
        });
    }

    private static Pair<List<ItemStack>, Map<ItemFilter, Pair<ItemStack, Integer>>> makeFurnaceRecipes() {
        Map<ItemFilter, Pair<ItemStack, Integer>> map = new HashMap<>();
        List<ItemStack> list = new ArrayList<>();

        SlimefunPlugin.getMinecraftRecipeService().subscribe(snapshot -> {
            for (FurnaceRecipe furnaceRecipe : snapshot.getRecipes(FurnaceRecipe.class)) {
                RecipeChoice choice = furnaceRecipe.getInputChoice();
                if (choice instanceof RecipeChoice.MaterialChoice) {
                    for (Material input : ((RecipeChoice.MaterialChoice) choice).getChoices()) {
                        list.add(new ItemStack(input));
                        list.add(furnaceRecipe.getResult());
                        map.put(new ItemFilter(new ItemStack(input, 1), FilterType.IGNORE_AMOUNT), new Pair<>(furnaceRecipe.getResult(), 1));
                    }
                }
            }
        });

        return new Pair<>(list, map);
    }

}
