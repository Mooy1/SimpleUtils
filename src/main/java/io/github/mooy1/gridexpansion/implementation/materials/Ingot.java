package io.github.mooy1.gridexpansion.implementation.materials;

import io.github.mooy1.gridexpansion.setup.Categories;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

public class Ingot extends SlimefunItem implements NotPlaceable {
    
    public static final SlimefunItemStack PLATINUM = make(Material.IRON_INGOT, "&3Platinum");

    public Ingot(SlimefunItemStack item, SlimefunItemStack dust) {
        super(Categories.MATERIALS, item, RecipeType.SMELTERY, new ItemStack[] {
                dust, null, null, null, null, null, null, null, null
        });
    }
    
    private static SlimefunItemStack make(Material material, String name) {
        return new SlimefunItemStack(
                name.substring(2).toUpperCase(Locale.ROOT) + "_INGOT",
                material,
                name + " Ingot"
        );
    }

}
