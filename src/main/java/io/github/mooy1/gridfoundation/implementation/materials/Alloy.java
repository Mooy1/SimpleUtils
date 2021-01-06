package io.github.mooy1.gridfoundation.implementation.materials;

import io.github.mooy1.gridfoundation.implementation.consumers.combiners.AlloySmelter;
import io.github.mooy1.gridfoundation.setup.Categories;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.UnplaceableBlock;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class Alloy extends UnplaceableBlock {

    public static final SlimefunItemStack ULTIMATUM = new SlimefunItemStack(
            "ULTIMATUM_ALLOY",
            Material.NETHERITE_INGOT,
            "&dUltimatum Alloy"
    );
    public static final SlimefunItemStack GLASS = new SlimefunItemStack(
            "INFUSED_GLASS",
            Material.WHITE_STAINED_GLASS,
            "&fInfused Glass"
    );
    public static final SlimefunItemStack INFUSED = new SlimefunItemStack(
            "INFUSED_ALLOY",
            Material.IRON_INGOT,
            "&fInfused Alloy"
    );
    
    public Alloy(SlimefunItemStack item, ItemStack a, ItemStack b, ItemStack c) {
        super(Categories.MAIN, item, AlloySmelter.TYPE, new ItemStack[] {
                a, b, c, null, null, null, null, null, null
        });
    }

}
