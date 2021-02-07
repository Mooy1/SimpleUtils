package io.github.mooy1.gridexpansion.implementation.containers.consumers.machines.single;

import io.github.mooy1.gridexpansion.utils.RecipeCopierService;
import io.github.mooy1.infinitylib.filter.FilterType;
import io.github.mooy1.infinitylib.filter.ItemFilter;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.cscorelib2.collections.Pair;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class Compactor extends AbstractSingleProcessor {

    public static final SlimefunItemStack ITEM = make(6, "Compactor", "Compresses ingots and other materials into blocks", Material.SMOOTH_SANDSTONE);
    
    public Compactor() {
        super(ITEM, Material.STICKY_PISTON, 16, new ItemStack[] {
                
        });
        RecipeCopierService.copyDisplayRecipes(this::addRecipe, () -> {}, SlimefunItems.CARBON_PRESS, SlimefunItems.ELECTRIC_PRESS);
    }
    
    private void addRecipe(ItemStack input, ItemStack stack) {
        this.displayRecipes.add(input);
        this.displayRecipes.add(stack);
        this.recipes.put(new ItemFilter(input, FilterType.MIN_AMOUNT), new Pair<>(stack, input.getAmount()));
    }

    @Override
    protected int getConsumption() {
        return 6;
    }

}
