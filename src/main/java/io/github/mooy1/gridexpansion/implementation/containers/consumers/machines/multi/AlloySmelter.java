package io.github.mooy1.gridexpansion.implementation.containers.consumers.machines.multi;

import io.github.mooy1.infinitylib.misc.DelayedRecipeType;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class AlloySmelter extends AbstractMultiProcessor {
    
    public static final SlimefunItemStack ITEM = make(4, "Alloy Smelter", "Melts metals together to create alloys", Material.BLAST_FURNACE);
    public static final DelayedRecipeType TYPE = new DelayedRecipeType(ITEM);
    
    public AlloySmelter() {
        super(ITEM, Material.LAVA_BUCKET, TYPE, new ItemStack[] {
                
        });
    }

    @Override
    protected int getConsumption() {
        return 4;
    }

}
