package io.github.mooy1.gridfoundation.implementation.components;

import io.github.mooy1.gridfoundation.implementation.consumers.machines.single.presses.PlatePress;
import io.github.mooy1.gridfoundation.setup.Categories;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.UnplaceableBlock;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

public final class Plate extends UnplaceableBlock {

    public static final SlimefunItemStack ALUMINUM = make("Aluminum", Material.GRAY_CARPET);
    public static final SlimefunItemStack LEAD = make("Lead", Material.GRAY_CARPET);
    public static final SlimefunItemStack IRON = make("Iron", Material.LIGHT_GRAY_CARPET);
    public static final SlimefunItemStack BRONZE = make("Bronze", Material.ORANGE_CARPET);
    
    public Plate(SlimefunItemStack item, ItemStack input) {
        super(Categories.COMPONENTS, item, PlatePress.TYPE, new ItemStack[] {
             input, null, null, null, null, null, null, null, null,
        });
    }
    
    public static SlimefunItemStack make(String name, Material material) {
        return new SlimefunItemStack(
                name.toUpperCase(Locale.ROOT) + "_GRID_PLATE",
                material,
                "&6" + name + " Plate",
                "&7Grid Machine Component"
        );
    } 

}
