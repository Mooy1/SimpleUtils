package io.github.mooy1.gridutils.implementation.containers.consumers.machines.single.presses;

import io.github.mooy1.infinitylib.misc.DelayedRecipeType;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class PlatePress extends AbstractPress {

    private static final SlimefunItemStack ITEM = make(8, "Plate Press", "Presses ingots into plates", Material.BLAST_FURNACE);
    public static final DelayedRecipeType TYPE = new DelayedRecipeType(ITEM);
    
    public PlatePress() {
        super(ITEM, 16, 8, TYPE, new ItemStack[] {

        });
    }
    
}
