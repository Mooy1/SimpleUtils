package io.github.mooy1.gridfoundation.implementation.consumers.converters;

import io.github.mooy1.gridfoundation.implementation.upgrades.UpgradeType;
import io.github.mooy1.gridfoundation.utils.MachineRecipeService;
import io.github.mooy1.infinitylib.filter.FilterType;
import io.github.mooy1.infinitylib.filter.ItemFilter;
import io.github.mooy1.infinitylib.items.StackUtils;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.cscorelib2.collections.Pair;
import org.bukkit.Material;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class Furnace extends AbstractConverter {

    public static final Map<ItemFilter, Pair<ItemStack, Integer>> furnaceRecipes = new HashMap<>();
    private static final List<ItemStack> displayRecipes = new ArrayList<>();
    public static final SlimefunItemStack ITEM = make(4, "Furnace", "Smelts materials using GP", Material.FURNACE);
    
    public Furnace() {
        super(ITEM, displayRecipes, furnaceRecipes, 4, new ItemStack[] {
                
        });

        SlimefunPlugin.getMinecraftRecipeService().subscribe(snapshot -> {
            for (FurnaceRecipe furnaceRecipe : snapshot.getRecipes(FurnaceRecipe.class)) {
                RecipeChoice choice = furnaceRecipe.getInputChoice();
                if (choice instanceof RecipeChoice.MaterialChoice) {
                    for (Material input : ((RecipeChoice.MaterialChoice) choice).getChoices()) {
                        displayRecipes.add(new ItemStack(input));
                        displayRecipes.add(furnaceRecipe.getResult());
                        furnaceRecipes.put(new ItemFilter(new ItemStack(input, 1), FilterType.IGNORE_AMOUNT), new Pair<>(furnaceRecipe.getResult(), 1));
                    }
                }
            }
        });

        MachineRecipeService.accept(SlimefunItems.ELECTRIC_INGOT_FACTORY, Furnace::addRecipe);
        addRecipe(SlimefunItems.GOLD_DUST, new ItemStack(Material.GOLD_INGOT));
        
    }
    
    public static void addRecipe(ItemStack input, ItemStack output) {
        if (Objects.equals("GOLD_DUST", StackUtils.getItemID(input, false))) {
            return;
        }
        displayRecipes.add(0, input);
        displayRecipes.add(0, output);
        furnaceRecipes.put(new ItemFilter(input, FilterType.MIN_AMOUNT), new Pair<>(output, input.getAmount()));
    }
    
    @Nonnull
    @Override
    public UpgradeType getMaxLevel() {
        return UpgradeType.ULTIMATE;
    }

}
