package io.github.mooy1.gridfoundation.implementation.consumers.gens;

import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CobbleGen extends AbstractItemGen {

    public static final SlimefunItemStack ITEM = make("Cobble", 1, Material.STONE_BRICKS);

    public CobbleGen() {
        super(ITEM, 1, Material.COBBLESTONE, new ItemStack[] {
                
        });
    }

}
