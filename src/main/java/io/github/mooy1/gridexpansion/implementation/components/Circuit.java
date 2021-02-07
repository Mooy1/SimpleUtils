package io.github.mooy1.gridexpansion.implementation.components;

import io.github.mooy1.gridexpansion.setup.Categories;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.UnplaceableBlock;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public final class Circuit extends UnplaceableBlock {

    public static final SlimefunItemStack I = make("I");
    public static final SlimefunItemStack II = make("II");
    public static final SlimefunItemStack III = make("III");
    public static final SlimefunItemStack IV = make("IV");
    public static final SlimefunItemStack V = make("V");
    public static final SlimefunItemStack VI = make("VI");
    public static final SlimefunItemStack VII = make("VII");
    
    public Circuit(SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(Categories.COMPONENTS, item, recipeType, recipe);
    }

    @Nonnull
    private static SlimefunItemStack make(String tier) {
        return new SlimefunItemStack(
                "GRID_CIRCUIT_" + tier,
                Material.HONEYCOMB,
                "&eGrid Circuit &6" + tier,
                "&7Grid Machine component"
        );
    }
}
