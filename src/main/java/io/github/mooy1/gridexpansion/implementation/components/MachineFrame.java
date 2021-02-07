package io.github.mooy1.gridexpansion.implementation.components;

import io.github.mooy1.gridexpansion.setup.Categories;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.UnplaceableBlock;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class MachineFrame extends UnplaceableBlock {

    public static final SlimefunItemStack ITEM = new SlimefunItemStack(
            "MACHINE_FRAME",
            Material.SMOOTH_STONE,
            "&8Machine Frame",
            "&7Core Component of Grid Machines"
    );

    public MachineFrame() {
        super(Categories.COMPONENTS, ITEM, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                
        });
    }

}
