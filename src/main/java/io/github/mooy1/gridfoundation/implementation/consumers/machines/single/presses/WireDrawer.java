package io.github.mooy1.gridfoundation.implementation.consumers.machines.single.presses;

import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class WireDrawer extends AbstractPress {

    private static final SlimefunItemStack ITEM = make(4, "Wire Drawer", "Draws ingots into wires", Material.BLAST_FURNACE);
    public static final DelayedRecipeType TYPE = new DelayedRecipeType(ITEM);
    public WireDrawer() {
        super(ITEM, 8, 3, TYPE, new ItemStack[] {

        });
    }

}
