package io.github.mooy1.gridfoundation.implementation.consumers.crafters;

import io.github.mooy1.infinitylib.filter.FilterType;
import io.github.mooy1.infinitylib.filter.MultiFilter;
import io.github.mooy1.gridfoundation.utils.GridLorePreset;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.cscorelib2.collections.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import javax.annotation.Nonnull;
import java.util.List;

public final class VanillaAutoCrafter extends AbstractAutoCrafter {

    public static final SlimefunItemStack ITEM = new SlimefunItemStack(
            "VANILLA_AUTO_CRAFTER",
            Material.CRAFTING_TABLE,
            "&eAuto Vanilla Workbench",
            "&7Automatically crafts vanilla items using GP",
            "",
            GridLorePreset.consumesGridPower(2)
    );

    public VanillaAutoCrafter() {
        super(ITEM, 2, new ItemStack[] {
                
        });
    }

    @Override
    Pair<MultiFilter, ItemStack> getRecipe(@Nonnull ItemStack item) {
        List<Recipe> recipes = Bukkit.getRecipesFor(item);
        for (Recipe recipe : recipes) {
            if (recipe instanceof ShapedRecipe) {
                ShapedRecipe shapedRecipe = (ShapedRecipe) recipe;
                return new Pair<>(MultiFilter.fromRecipe(shapedRecipe, FilterType.MIN_AMOUNT), shapedRecipe.getResult());
            } else if (recipe instanceof ShapelessRecipe) {
                ShapelessRecipe shapelessRecipe = (ShapelessRecipe) recipe;
                return new Pair<>(MultiFilter.fromRecipe(shapelessRecipe, FilterType.MIN_AMOUNT), shapelessRecipe.getResult());
            }
        }
        return null;
    }

}
