package io.github.mooy1.gridfoundation.implementation.consumers.machines.single.presses;

import io.github.mooy1.gridfoundation.utils.BetterRecipeType;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class PlatePress extends AbstractPress {

    private static final SlimefunItemStack ITEM = make(8, "Plate Press", "Presses ingots into plates", Material.BLAST_FURNACE);
    public static final BetterRecipeType TYPE = new BetterRecipeType(ITEM);
    
    public PlatePress() {
        super(ITEM, 16, 8, TYPE, new ItemStack[] {

        });
    }
    
}
