package io.github.mooy1.gridfoundation.implementation.materials;

import io.github.mooy1.gridfoundation.implementation.consumers.machines.single.Pulverizer;
import io.github.mooy1.gridfoundation.setup.Categories;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.UnplaceableBlock;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

public final class CrushedBlock extends UnplaceableBlock {

    public static final SlimefunItemStack DUST = make("Dust", "Sand", Material.SAND);
    public static final SlimefunItemStack NETHER = make("Nether Gravel", "Netherrack", Material.NETHERRACK);
    public static final SlimefunItemStack END = make("Ender Gravel", "Endstone", Material.END_STONE);
    public static final SlimefunItemStack DIORITE = make("Crushed Diorite", "Diorite", Material.DIORITE);
    public static final SlimefunItemStack ANDESITE = make("Crushed Andesite", "Andesite", Material.ANDESITE);
    public static final SlimefunItemStack GRANITE = make("Crushed Granite", "Granite", Material.GRANITE);
    
    public CrushedBlock(SlimefunItemStack item) {
        super(Categories.COMPONENTS, item, Pulverizer.TYPE, new ItemStack[] {
                new ItemStack(item.getType()), null,null, null,null, null,null, null,null
        });
    }
    
    private static SlimefunItemStack make(String name, String source, Material material) {
        return new SlimefunItemStack(
                "CRUSHED_" + source.toUpperCase(Locale.ROOT),
                material,
                "&6" + name
        );
    }

}
