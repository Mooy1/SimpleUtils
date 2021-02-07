package io.github.mooy1.gridexpansion.implementation.containers.consumers.gens;

import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CobbleGen extends AbstractItemGen {

    public static final SlimefunItemStack ITEM = make("Cobble", 1, Material.STONE_BRICKS);

    public CobbleGen() {
        super(ITEM, Material.COBBLESTONE, new ItemStack[] {
                
        });
    }

    @Override
    public int getConsumption() {
        return 1;
    }

}
