package io.github.mooy1.gridfoundation.implementation.materials;

import io.github.mooy1.gridfoundation.implementation.blocks.ManualSieve;
import io.github.mooy1.gridfoundation.setup.Categories;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class InfusionShard extends SlimefunItem implements NotPlaceable {

    public static final SlimefunItemStack ITEM = new SlimefunItemStack(
            "INFUSION_SHARD",
            Material.QUARTZ,
            "&fInfusion Shard"
    );

    public InfusionShard() {
        super(Categories.MATERIALS, ITEM, ManualSieve.TYPE, new ItemStack[] {
                
        });
    }

}
