package io.github.mooy1.gridfoundation.implementation.components;

import io.github.mooy1.gridfoundation.implementation.consumers.combiners.AlloySmelter;
import io.github.mooy1.gridfoundation.setup.Categories;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class InfusedGlass extends SlimefunItem {
    
    public static final SlimefunItemStack ITEM = new SlimefunItemStack(
            "INFUSED_GLASS",
            Material.WHITE_STAINED_GLASS,
            "&fInfused Glass",
            "&7Grid machine component"
    );

    public InfusedGlass() {
        super(Categories.COMPONENTS, ITEM, AlloySmelter.TYPE, new ItemStack[] {
                
        });
    }
    
}
