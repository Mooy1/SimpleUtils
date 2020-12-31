package io.github.mooy1.slimegrid.implementation.consumers.crafters;

import io.github.mooy1.infinitylib.filter.MultiFilter;
import io.github.mooy1.infinitylib.items.RecipeUtils;
import io.github.mooy1.infinitylib.items.StackUtils;
import io.github.mooy1.slimegrid.lists.Items;
import me.mrCookieSlime.Slimefun.cscorelib2.collections.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public final class VanillaAutoCrafter extends AbstractAutoCrafter {

    public static final int GP = 16;
    public VanillaAutoCrafter() {
        super(GP, Items.VANILLA_AUTO_CRAFTER, new ItemStack[] {
                Items.SILVER_WIRE, Items.INFUSED_CRYSTAL, Items.SILVER_WIRE,
                Items.GRID_CIRCUIT_II, new ItemStack(Material.CRAFTING_TABLE), Items.GRID_CIRCUIT_II,
                Items.SILVER_WIRE, Items.INFUSED_CRYSTAL, Items.SILVER_WIRE
        });
    }

    @Override
    public boolean updateCache(@Nonnull Block b, @Nonnull ItemStack item) {
        if (item.getType() == Material.AIR) {
            super.cache.put(b.getLocation(), null);
        } else if (StackUtils.getItemID(item, false) == null) {
            
            Pair<MultiFilter, ItemStack> pair = getVanillaRecipe(item);

            if (pair != null) {
                this.cache.put(b.getLocation(), pair);
                return true;
            }
        }
        return false;
    }

    @Nullable
    private Pair<MultiFilter, ItemStack> getVanillaRecipe(@Nonnull ItemStack item) {
        List<Recipe> recipes = Bukkit.getRecipesFor(item);
        for (Recipe recipe : recipes) {
            if (recipe instanceof ShapedRecipe) {
                ShapedRecipe shapedRecipe = (ShapedRecipe) recipe;
                return new Pair<>(RecipeUtils.getRecipeFilter(shapedRecipe), shapedRecipe.getResult()) ;
            } else if (recipe instanceof ShapelessRecipe) {
                ShapelessRecipe shapelessRecipe = (ShapelessRecipe) recipe;
                return new Pair<>(RecipeUtils.getRecipeFilter((shapelessRecipe)), shapelessRecipe.getResult());
            }
        }
        return null;
    }

}
