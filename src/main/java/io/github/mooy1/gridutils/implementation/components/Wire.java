package io.github.mooy1.gridutils.implementation.components;

import io.github.mooy1.gridutils.implementation.containers.consumers.machines.single.presses.WireDrawer;
import io.github.mooy1.gridutils.setup.Categories;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.UnplaceableBlock;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

public final class Wire extends UnplaceableBlock {
    
    public static final SlimefunItemStack SILVER = make("Silver");

    public Wire(SlimefunItemStack item, ItemStack input) {
        super(Categories.COMPONENTS, item, WireDrawer.TYPE, new ItemStack[] {
                input, null, null, null, null, null, null, null, null,
        }, new SlimefunItemStack(item, 4));
    }

    public static SlimefunItemStack make(String name) {
        return new SlimefunItemStack(
                name.toUpperCase(Locale.ROOT) + "_GRID_WIRE",
                Material.STRING,
                "&f" + name + " Wire",
                "&7Grid Component Material"
        );
    }

}
