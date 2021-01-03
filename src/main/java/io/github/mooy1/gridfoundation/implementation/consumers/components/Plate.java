package io.github.mooy1.gridfoundation.implementation.consumers.components;

import io.github.mooy1.gridfoundation.implementation.consumers.presses.PlatePress;
import io.github.mooy1.gridfoundation.setup.Categories;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.UnplaceableBlock;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class Plate extends UnplaceableBlock {

    public Plate(SlimefunItemStack item, ItemStack input) {
        super(Categories.COMPONENTS, item, PlatePress.TYPE, new ItemStack[] {
             input, null, null, null, null, null, null, null, null,
        });
        PlatePress.addRecipe(PlatePress.plateRecipes, input , item);
    }
    
    public static SlimefunItemStack make(String name, Material material) {
        return new SlimefunItemStack(
                
        );
    } 

}
