package io.github.mooy1.simpleutils.blocks.shapedrecipe;

import io.github.mooy1.infinitylib.recipes.AbstractRecipeMap;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.apache.commons.lang.Validate;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A recipe map with 3x3 shaped (or shapeless) recipes. It will detect the shape of recipes on its own.
 * 
 * @author Mooy1
 */
public final class ShapedRecipeMap extends AbstractRecipeMap<ShapedRecipe, ItemStack> {
    
    public void put(@Nonnull ItemStack[] recipe, @Nonnull ItemStack output) {
        Validate.isTrue(recipe.length == 9);
        this.recipes.put(new ShapedRecipe(recipe), output);
    }
    
    public void put(@Nonnull org.bukkit.inventory.ShapelessRecipe recipe) {
        ItemStack[] arr = new ItemStack[9];
        for (int i = 0 ; i < Math.min(9, recipe.getIngredientList().size()) ; i++) {
            arr[i] = recipe.getIngredientList().get(i);
        }
        this.recipes.put(new ShapedRecipe(arr), recipe.getResult());
    }
    
    public void put(@Nonnull org.bukkit.inventory.ShapedRecipe recipe) {
        ItemStack[] arr = new ItemStack[9];
        for (int i = 0 ; i < recipe.getShape().length ; i++) {
            for (int j = 0 ; j < recipe.getShape()[0].length() ; j++) {
                arr[i * 3 + j] = recipe.getIngredientMap().get(recipe.getShape()[i].charAt(j));
            }
        }
        this.recipes.put(new ShapedRecipe(arr), recipe.getResult());
    }
    
    @Nullable
    public ShapedOutput get(@Nonnull ItemStack[] input) {
        Validate.isTrue(input.length == 9);
        
        int[] amounts = new int[9];
        for (int i = 0 ; i < 9 ; i++) {
            if (input[i] != null) {
                amounts[i] = input[i].getAmount();
            }
        }
        
        ItemStack output = this.recipes.get(new ShapedRecipe(input));
        return output == null ? null : new ShapedOutput(output, amounts);
    }
    
    @Nullable
    public ShapedOutput get(@Nonnull BlockMenu menu, @Nonnull int[] slots) {
        Validate.isTrue(slots.length == 9);
        
        ItemStack[] stacks = new ItemStack[9];
        int[] amounts = new int[9];
        for (int i = 0 ; i < 9 ; i++) {
            ItemStack item = menu.getItemInSlot(slots[i]);
            if (item != null) {
                amounts[i] = (stacks[i] = item).getAmount();
            }
        }
        
        ItemStack output = this.recipes.get(new ShapedRecipe(stacks));
        return output == null ? null : new ShapedOutput(output, amounts);
    }

}
