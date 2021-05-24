package io.github.mooy1.simpleutils.implementation.blocks.workbench;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import io.github.mooy1.simpleutils.SimpleUtils;
import io.github.thebusybiscuit.slimefun4.core.multiblocks.MultiBlockMachine;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.tags.SlimefunTag;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

final class RecipeRegistry {

    private final Map<ShapedInput, ItemStack> recipes = new HashMap<>();

    RecipeRegistry() {
        SlimefunPlugin.getMinecraftRecipeService().subscribe(recipeSnapshot -> {
            for (ShapedRecipe recipe : recipeSnapshot.getRecipes(ShapedRecipe.class)) {

                Map<Character, ItemStack> choices = recipe.getIngredientMap();
                ItemStack[] arr = new ItemStack[9];
                String[] shape = recipe.getShape();

                List<Integer> plankSlots = null;
                boolean couldHaveAltPlankRecipes = true;

                for (int r = 0 ; r < shape.length ; r++) {
                    for (int c = 0 ; c < shape[r].length() ; c++) {
                        char ch = shape[r].charAt(c);
                        ItemStack stack = choices.get(ch);
                        if (stack != null) {
                            int slot = r * 3 + c;
                            arr[slot] = stack;

                            // add plank slots
                            if (couldHaveAltPlankRecipes && stack.getType() == Material.OAK_PLANKS) {
                                if (plankSlots != null) {
                                    plankSlots.add(slot);
                                } else if (recipe.getChoiceMap().get(ch).test(new ItemStack(Material.SPRUCE_PLANKS))) {
                                    plankSlots = new ArrayList<>();
                                    plankSlots.add(slot);
                                } else {
                                    couldHaveAltPlankRecipes = false;
                                }
                            }
                        }
                    }
                }

                // add alternate plank recipes
                if (couldHaveAltPlankRecipes && plankSlots != null) {
                    for (Material material : SlimefunTag.PLANKS.getValues()) {
                        if (material != Material.OAK_PLANKS) {
                            ItemStack plank = new ItemStack(material);
                            ItemStack[] newLogRecipe = new ItemStack[9];
                            for (int i = 0 ; i < 9 ; i++) {
                                if (plankSlots.contains(i)) {
                                    newLogRecipe[i] = plank;
                                } else {
                                    newLogRecipe[i] = arr[i];
                                }
                            }
                            this.recipes.put(new ShapedInput(newLogRecipe), recipe.getResult());
                        }
                    }
                }

                this.recipes.put(new ShapedInput(arr), recipe.getResult());
            }

            for (ShapelessRecipe recipe : recipeSnapshot.getRecipes(ShapelessRecipe.class)) {
                List<ItemStack> ingredientList = recipe.getIngredientList();
                ItemStack[] arr = new ItemStack[9];
                for (int i = 0 ; i < Math.min(9, ingredientList.size()) ; i++) {
                    arr[i] = ingredientList.get(i);
                }
                this.recipes.put(new ShapedInput(arr), recipe.getResult());
            }
        });

        SimpleUtils.inst().runSync(() -> {
            for (RecipeType type : Arrays.asList(RecipeType.ENHANCED_CRAFTING_TABLE, RecipeType.ARMOR_FORGE, RecipeType.MAGIC_WORKBENCH)) {
                List<ItemStack[]> list = ((MultiBlockMachine) type.getMachine()).getRecipes();
                for (int i = 0 ; i < list.size() ; i += 2) {
                    ItemStack[] recipe = list.get(i);
                    if (recipe.length != 9) {
                        continue;
                    }
                    for (int j = 0 ; j < 9 ; j++) {
                        // make sure recipe isn't empty
                        if (recipe[j] != null) {
                            ItemStack out = list.get(i + 1)[0];
                            SlimefunItem item = SlimefunItem.getByItem(out);
                            if (item != null && item.hasResearch()) {
                                this.recipes.put(new ShapedInput(recipe), new ResearchableItemStack(item.getResearch(), out));
                            } else {
                                this.recipes.put(new ShapedInput(recipe), out);
                            }
                            break;
                        }
                    }
                }
            }
        }, 100);
    }

    @Nullable
    ItemStack get(@Nonnull ShapedInput input) {
        return this.recipes.get(input);
    }

}
