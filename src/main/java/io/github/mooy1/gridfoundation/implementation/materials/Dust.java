package io.github.mooy1.gridfoundation.implementation.materials;

import io.github.mooy1.gridfoundation.implementation.consumers.machines.single.Pulverizer;
import io.github.mooy1.gridfoundation.setup.Categories;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

public class Dust extends SlimefunItem implements NotPlaceable {

    // new
    public static final SlimefunItemStack PLATINUM = make(Material.SUGAR, "Platinum");
    
    // slimefun alloys
    public static final SlimefunItemStack NICKEL = make(Material.SUGAR, "Nickel");
    public static final SlimefunItemStack COBALT = make(Material.SUGAR, "Cobalt");

    public Dust(SlimefunItemStack dust, SlimefunItemStack ore, SlimefunItemStack ingot) {
        super(Categories.MATERIALS, dust, Pulverizer.TYPE, new ItemStack[] {
                ore, null, null, null, null, null, null, null, null
        }, new SlimefunItemStack(dust, 2));
        Pulverizer.addRecipe(ingot, dust, true);
    }
    
    private static SlimefunItemStack make(Material material, String  name) {
        return new SlimefunItemStack(
                name.toUpperCase(Locale.ROOT) + "_DUST",
                material,
                name + " Dust"
        );
    }
    

}
