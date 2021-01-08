package io.github.mooy1.gridfoundation.implementation.consumers.machines.multi;

import io.github.mooy1.gridfoundation.utils.BetterRecipeType;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class AlloySmelter extends AbstractMultiProcessor {
    
    public static final SlimefunItemStack ITEM = make(4, "Alloy Smelter", "Melts metals together to create alloys", Material.BLAST_FURNACE);
    public static final BetterRecipeType TYPE = new BetterRecipeType(ITEM);
    
    public AlloySmelter() {
        super(ITEM, Material.LAVA_BUCKET, TYPE, 4, new ItemStack[] {
                
        });
    }

}
