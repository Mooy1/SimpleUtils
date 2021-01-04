package io.github.mooy1.gridfoundation.implementation.components;

import io.github.mooy1.gridfoundation.implementation.consumers.combiners.AlloySmelter;
import io.github.mooy1.gridfoundation.setup.Categories;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.UnplaceableBlock;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class UltimatumAlloy extends UnplaceableBlock {

    public static final SlimefunItemStack ITEM = new SlimefunItemStack(
            "ULTIMATUM_ALLOY",
            Material.NETHERITE_INGOT,
            "&dUltimatum Alloy",
            "&7Grid Component Material"
    );
    
    public UltimatumAlloy() {
        super(Categories.MAIN, ITEM, AlloySmelter.TYPE, new ItemStack[] {
                
        });
    }

}
