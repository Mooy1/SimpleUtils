package io.github.mooy1.gridutils.implementation.components;

import io.github.mooy1.gridutils.implementation.containers.consumers.machines.single.presses.GearCaster;
import io.github.mooy1.gridutils.setup.Categories;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.UnplaceableBlock;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

public final class Gear extends UnplaceableBlock {

    public static final SlimefunItemStack GOLDEN = make("Golden", Material.HORN_CORAL);
    public static final SlimefunItemStack COPPER = make("Copper", Material.FIRE_CORAL);
    public static final SlimefunItemStack TIN = make("Tin", Material.BRAIN_CORAL);
    public static final SlimefunItemStack COBALT = make("Cobalt", Material.TUBE_CORAL);

    public Gear(SlimefunItemStack item, ItemStack input) {
        super(Categories.COMPONENTS, item, GearCaster.TYPE, new ItemStack[] {
                input, null, null, null, null, null, null, null, null,
        });
    }

    private static SlimefunItemStack make(String name, Material material) {
        return new SlimefunItemStack(
                name.toUpperCase(Locale.ROOT) + "_GRID_GEAR",
                material,
                "&6" + name + " Gear",
                "&7Grid Machine Component"
        );
    }

}
