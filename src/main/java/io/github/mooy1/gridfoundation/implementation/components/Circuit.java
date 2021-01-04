package io.github.mooy1.gridfoundation.implementation.components;

import io.github.mooy1.gridfoundation.GridFoundation;
import io.github.mooy1.gridfoundation.setup.Categories;
import io.github.mooy1.infinitylib.presets.LorePreset;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.UnplaceableBlock;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class Circuit extends UnplaceableBlock {

    public static final SlimefunItemStack I = make(1);
    public static final SlimefunItemStack II = make(2);
    public static final SlimefunItemStack III = make(3);
    public static final SlimefunItemStack IV = make(4);
    public static final SlimefunItemStack V = make(5);
    public static final SlimefunItemStack VI = make(6);
    public static final SlimefunItemStack VII = make(7);
    
    public Circuit(SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(Categories.COMPONENTS, item, recipeType, recipe);
    }

    private static SlimefunItemStack make(int tier) {
        return new SlimefunItemStack(
                "GRID_CIRCUIT_" + LorePreset.romanNumeral(tier),
                Material.HONEYCOMB,
                "&eGrid Circuit &6" + LorePreset.romanNumeral(tier),
                "&7Grid Machine component"
        );
    }

}
