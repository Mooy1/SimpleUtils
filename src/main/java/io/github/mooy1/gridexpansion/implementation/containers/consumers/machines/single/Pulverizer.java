package io.github.mooy1.gridexpansion.implementation.containers.consumers.machines.single;

import io.github.mooy1.gridexpansion.utils.RecipeCopierService;
import io.github.mooy1.infinitylib.filter.FilterType;
import io.github.mooy1.infinitylib.filter.ItemFilter;
import io.github.mooy1.infinitylib.misc.DelayedRecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.utils.tags.SlimefunTag;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.cscorelib2.collections.Pair;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Pulverizer extends AbstractSingleProcessor {

    public static final Map<ItemFilter, Pair<ItemStack, Integer>> recipes = new HashMap<>();
    private static final List<ItemStack> displayRecipes = new ArrayList<>();
    
    public static final SlimefunItemStack ITEM = make(4,"Pulverizer", "Pulverizes ores and ingots, into dusts", Material.GRAY_CONCRETE);
    public static final DelayedRecipeType TYPE = new DelayedRecipeType(ITEM);

    public Pulverizer() {
        super(ITEM,Material.IRON_PICKAXE, 4, recipes, displayRecipes, new ItemStack[] {
                
        });
        TYPE.acceptEach(((stacks, stack) -> addRecipe(stacks[0], stack, true)));
        RecipeCopierService.copyDisplayRecipes((input, stack) -> addRecipe(input, stack, false),
                () -> addRecipe(new ItemStack(Material.COBBLESTONE), new ItemStack(Material.GRAVEL), true),
                SlimefunItems.ELECTRIC_INGOT_PULVERIZER, SlimefunItems.ELECTRIC_ORE_GRINDER
        );
    }
    
    public static void addRecipe(ItemStack input, ItemStack stack, boolean force) {
        if (!force && (SlimefunTag.STONE_VARIANTS.isTagged(input.getType()) || input.getType() == Material.COBBLESTONE || input.getType() == Material.NETHERRACK)) {
            return;
        }
        displayRecipes.add(input);
        displayRecipes.add(stack);
        recipes.put(new ItemFilter(input, FilterType.MIN_AMOUNT), new Pair<>(stack, input.getAmount()));
    }

    @Override
    protected int getConsumption() {
        return 4;
    }

}
