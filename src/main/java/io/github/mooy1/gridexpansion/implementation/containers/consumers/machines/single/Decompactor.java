package io.github.mooy1.gridexpansion.implementation.containers.consumers.machines.single;

import io.github.mooy1.gridexpansion.utils.RecipeCopierService;
import io.github.mooy1.infinitylib.filter.FilterType;
import io.github.mooy1.infinitylib.filter.ItemFilter;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.cscorelib2.collections.Pair;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class Decompactor extends AbstractSingleProcessor {
    
    public static final SlimefunItemStack ITEM = make(6,"Decompactor", "Decompresses blocks into ingots and materials", Material.SMOOTH_RED_SANDSTONE);

    public Decompactor() {
        super(ITEM, Material.STICKY_PISTON,16, new ItemStack[] {
                
        });
        RecipeCopierService.copyDisplayRecipes(this::addRecipe, () -> {}, SlimefunItems.ELECTRIC_PRESS);
    }

    private void addRecipe(ItemStack output, ItemStack input) {
        if (input.getAmount() == 1 && input.getType() != Material.COBBLESTONE) {
            this.displayRecipes.add(input);
            this.displayRecipes.add(output);
            this.recipes.put(new ItemFilter(input, FilterType.MIN_AMOUNT), new Pair<>(output, input.getAmount()));
        }
    }

    @Override
    protected int getConsumption() {
        return 6;
    }

}
