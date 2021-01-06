package io.github.mooy1.gridfoundation.implementation.consumers.converters;

import io.github.mooy1.gridfoundation.implementation.upgrades.UpgradeType;
import io.github.mooy1.gridfoundation.utils.BetterRecipeType;
import io.github.mooy1.gridfoundation.utils.MachineRecipeService;
import io.github.mooy1.infinitylib.filter.FilterType;
import io.github.mooy1.infinitylib.filter.ItemFilter;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.utils.tags.SlimefunTag;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.cscorelib2.collections.Pair;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Pulverizer extends AbstractConverter {

    public static final Map<ItemFilter, Pair<ItemStack, Integer>> recipes = new HashMap<>();
    private static final List<ItemStack> displayRecipes = new ArrayList<>();
    
    public static final SlimefunItemStack ITEM = make(4,"Pulverizer", "Pulverizes ores and ingots, into dusts", Material.GRAY_CONCRETE);
    public static final BetterRecipeType TYPE = new BetterRecipeType(ITEM);

    public Pulverizer() {
        super(ITEM, displayRecipes, recipes, 4, new ItemStack[] {
                
        });
        TYPE.acceptEach(((stacks, stack) -> addRecipe(stacks[0], stack)));
        MachineRecipeService.accept(SlimefunItems.ELECTRIC_INGOT_PULVERIZER, Pulverizer::addRecipe);
        MachineRecipeService.accept(SlimefunItems.ELECTRIC_ORE_GRINDER, Pulverizer::addRecipe);
        addRecipe(new ItemStack(Material.COBBLESTONE), new ItemStack(Material.GRAVEL));
    }
    
    public static void addRecipe(ItemStack input, ItemStack stack) {
        if (SlimefunTag.STONE_VARIANTS.isTagged(stack.getType())) {
            return;
        }
        displayRecipes.add(input);
        displayRecipes.add(stack);
        recipes.put(new ItemFilter(input, FilterType.MIN_AMOUNT), new Pair<>(stack, input.getAmount()));
    }

    @Nonnull
    @Override
    public UpgradeType getMaxLevel() {
        return UpgradeType.ULTIMATE;
    }
    
}
