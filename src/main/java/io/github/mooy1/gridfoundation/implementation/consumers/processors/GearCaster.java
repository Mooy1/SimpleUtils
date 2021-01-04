package io.github.mooy1.gridfoundation.implementation.consumers.processors;

import io.github.mooy1.gridfoundation.utils.BetterRecipeType;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class GearCaster extends AbstractProcessor {

    private static final SlimefunItemStack ITEM = make(6, "Gear Press", "Presses ingots into gears", Material.BLAST_FURNACE);
    public static final BetterRecipeType TYPE = new BetterRecipeType(ITEM);
    
    public GearCaster() {
        super(ITEM, 6, "Casting", TYPE, new ItemStack[] {
                
        });
    }

}
