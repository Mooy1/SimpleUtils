package io.github.mooy1.gridfoundation.implementation.generators.machines;

import io.github.mooy1.infinitylib.filter.ItemFilter;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.utils.tags.SlimefunTag;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public final class SurvivalGenerator extends AbstractGeneratorMachine{
    
    public static final Map<ItemFilter, Integer> survivalRecipes = makeSurvivalRecipes();

    private static Map<ItemFilter, Integer> makeSurvivalRecipes() {
        Map<ItemFilter, Integer> survivalRecipes = new HashMap<>();
        
        addRecipe(survivalRecipes, Material.CHARCOAL, 6);
        addRecipe(survivalRecipes, Material.COAL, 8);
        addRecipe(survivalRecipes, Material.COAL_BLOCK, 72);
        addRecipe(survivalRecipes, Material.LAVA_BUCKET, 100);
        addRecipe(survivalRecipes, SlimefunItems.CARBON, 64);
        addRecipe(survivalRecipes, SlimefunItems.COMPRESSED_CARBON, 256);
        addRecipe(survivalRecipes, SlimefunItems.CARBON_CHUNK, 2048);
        addRecipe(survivalRecipes, SlimefunItems.CARBONADO, 4096);

        for (Material material : SlimefunTag.PLANKS.getValues()) {
            addRecipe(survivalRecipes, material, 1);
        }
        for (Material material : SlimefunTag.LOGS.getValues()) {
            addRecipe(survivalRecipes, material, 4);
        }
        
        return survivalRecipes;
    }
    
    private static final SlimefunItemStack ITEM = make("Survival", "flammable fuels", 1, Material.FURNACE);
    
    public SurvivalGenerator() {
        super(ITEM, 2, survivalRecipes, new ItemStack[] {
                
        });
    }

}
