package io.github.mooy1.gridfoundation.implementation.materials;

import io.github.mooy1.gridfoundation.implementation.blocks.ManualSieve;
import io.github.mooy1.gridfoundation.implementation.consumers.converters.Furnace;
import io.github.mooy1.gridfoundation.implementation.consumers.converters.Pulverizer;
import io.github.mooy1.gridfoundation.setup.Categories;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.UnplaceableBlock;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public final class OreChunk extends UnplaceableBlock {
    
    public static final SlimefunItemStack COPPER = make(SlimefunItems.COPPER_DUST, Material.ORANGE_DYE);
    
    public OreChunk(SlimefunItemStack item, SlimefunItemStack dust, SlimefunItemStack ingot, ItemStack... crushedBlocks) {
        super(Categories.COMPONENTS, item, ManualSieve.TYPE, fromSmallerArray(crushedBlocks));
        Pulverizer.addRecipe(item, new CustomItem(dust, 2));
        Furnace.addRecipe(item, ingot);
    }
    
    private static SlimefunItemStack make(SlimefunItemStack dust, Material material) {
        return new SlimefunItemStack(
                dust.getItemId().replace("DUST", "ORE_CHUNK"),
                material,
                "&6" + Objects.requireNonNull(dust.getDisplayName()).replace("Dust", "Ore Chunk"),
                "&7Can be smelted into ingots or pulverized into dusts"
        );
    }
    
    private static ItemStack[] fromSmallerArray(ItemStack[] array) {
        ItemStack[] recipe = new ItemStack[9];
        System.arraycopy(array, 0, recipe, 0, recipe.length);
        return recipe;
    }

}
