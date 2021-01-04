package io.github.mooy1.gridfoundation.implementation.components;

import io.github.mooy1.gridfoundation.implementation.consumers.combiners.AlloySmelter;
import io.github.mooy1.gridfoundation.setup.Categories;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.UnplaceableBlock;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class InfusedAlloy extends UnplaceableBlock {

    public static final SlimefunItemStack ITEM = new SlimefunItemStack(
            "INFUSED_ALLOY",
            Material.IRON_INGOT,
            "&fInfused Alloy",
            "&7Grid Component Material"
    );

    public InfusedAlloy() {
        super(Categories.COMPONENTS, ITEM, AlloySmelter.TYPE, new ItemStack[] {
                
        });
    }

}
