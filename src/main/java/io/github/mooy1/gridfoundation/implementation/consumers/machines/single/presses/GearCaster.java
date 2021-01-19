package io.github.mooy1.gridfoundation.implementation.consumers.machines.single.presses;

import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class GearCaster extends AbstractPress {

    private static final SlimefunItemStack ITEM = make(6, "Gear Press", "Presses ingots into gears", Material.BLAST_FURNACE);
    public static final DelayedRecipeType TYPE = new DelayedRecipeType(ITEM);
    
    public GearCaster() {
        super(ITEM, 8, 5, TYPE, new ItemStack[] {
                
        });
    }

}
