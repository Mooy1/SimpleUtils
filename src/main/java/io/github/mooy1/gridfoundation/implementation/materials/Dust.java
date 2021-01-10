package io.github.mooy1.gridfoundation.implementation.materials;

import io.github.mooy1.gridfoundation.implementation.consumers.machines.single.Pulverizer;
import io.github.mooy1.gridfoundation.setup.Categories;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

public class Dust extends SlimefunItem implements NotPlaceable {

    // new
    public static final SlimefunItemStack PLATINUM = make(Material.SUGAR, "&3Platinum");
    
    // slimefun alloys
    public static final SlimefunItemStack NICKEL = make(Material.SUGAR, "&7Nickel");
    public static final SlimefunItemStack COBALT = make(Material.SUGAR, "&9Cobalt");

    public Dust(SlimefunItemStack dust, SlimefunItemStack ore) {
        super(Categories.MATERIALS, dust, Pulverizer.TYPE, new ItemStack[] {
                ore, null, null, null, null, null, null, null, null
        }, new SlimefunItemStack(dust, 2));
    }
    
    // for slimefun alloys only
    public Dust(SlimefunItemStack dust, SlimefunItemStack ore, SlimefunItemStack ingot) {
        this(dust, ore);
        RecipeType.SMELTERY.register(new ItemStack[] {dust}, ingot);
    }
    
    
    private static SlimefunItemStack make(Material material, String  name) {
        return new SlimefunItemStack(
                name.substring(2).toUpperCase(Locale.ROOT) + "_DUST",
                material,
                name + " Dust"
        );
    }
    

}
