package io.github.mooy1.gridexpansion.implementation.containers.consumers.machines.single.presses;

import io.github.mooy1.infinitylib.misc.DelayedRecipeType;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class WireDrawer extends AbstractPress {

    private static final SlimefunItemStack ITEM = make(4, "Wire Drawer", "Draws ingots into wires", Material.BLAST_FURNACE);
    public static final DelayedRecipeType TYPE = new DelayedRecipeType(ITEM);
    public WireDrawer() {
        super(ITEM, 8,TYPE, new ItemStack[] {

        });
    }

    @Override
    protected int getConsumption() {
        return 3;
    }

}
