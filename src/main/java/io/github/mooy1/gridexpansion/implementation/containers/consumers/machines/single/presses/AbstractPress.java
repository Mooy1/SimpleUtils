package io.github.mooy1.gridexpansion.implementation.containers.consumers.machines.single.presses;

import io.github.mooy1.gridexpansion.implementation.containers.consumers.machines.single.AbstractSingleProcessor;
import io.github.mooy1.infinitylib.filter.FilterType;
import io.github.mooy1.infinitylib.filter.ItemFilter;
import io.github.mooy1.infinitylib.misc.DelayedRecipeType;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.cscorelib2.collections.Pair;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class AbstractPress extends AbstractSingleProcessor {
    
    public AbstractPress(SlimefunItemStack item, int ticks, DelayedRecipeType type, ItemStack[] recipe) {
        super(item, Material.PISTON, ticks, new HashMap<>(), new ArrayList<>(), recipe);
        type.acceptEach((stacks, stack) -> {
            ItemStack input = stacks[0];
            super.recipes.put(new ItemFilter(input, FilterType.MIN_AMOUNT), new Pair<>(stack, input.getAmount()));
            super.displayRecipes.add(input);
            super.displayRecipes.add(stack);
        });
    }

}
