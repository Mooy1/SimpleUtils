package io.github.mooy1.gridexpansion.implementation.materials;

import io.github.mooy1.gridexpansion.implementation.blocks.ManualSieve;
import io.github.mooy1.gridexpansion.implementation.containers.consumers.machines.single.Pulverizer;
import io.github.mooy1.gridexpansion.setup.Categories;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import io.github.thebusybiscuit.slimefun4.core.handlers.EntityInteractHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;


public final class CrushedOre extends SlimefunItem implements NotPlaceable {
    
    // main
    public static final SlimefunItemStack COPPER = make(SlimefunItems.COPPER_DUST, Material.ORANGE_DYE);
    public static final SlimefunItemStack GOLD = make(SlimefunItems.GOLD_DUST, Material.YELLOW_DYE);
    public static final SlimefunItemStack SILVER = make(SlimefunItems.SILVER_DUST, Material.WHITE_DYE);
    public static final SlimefunItemStack ALUMINUM = make(SlimefunItems.ALUMINUM_DUST, Material.LIGHT_GRAY_DYE);
    public static final SlimefunItemStack LEAD = make(SlimefunItems.LEAD_DUST, Material.GRAY_DYE);
    public static final SlimefunItemStack MAGNESIUM = make(SlimefunItems.MAGNESIUM_DUST, Material.GRAY_DYE);
    public static final SlimefunItemStack IRON = make(SlimefunItems.IRON_DUST, Material.LIGHT_GRAY_DYE);
    public static final SlimefunItemStack ZINC = make(SlimefunItems.ZINC_DUST, Material.LIGHT_GRAY_DYE);
    public static final SlimefunItemStack TIN = make(SlimefunItems.TIN_DUST, Material.LIGHT_GRAY_DYE);
    
    // alloys
    public static final SlimefunItemStack NICKEL = make(Dust.NICKEL, Material.LIGHT_BLUE_DYE);
    public static final SlimefunItemStack COBALT = make(Dust.COBALT, Material.BLUE_DYE);
    
    // new
    public static final SlimefunItemStack PLATINUM = make(Dust.PLATINUM, Material.CYAN_DYE);
    
    public CrushedOre(SlimefunItemStack ore, ItemStack ingot, ItemStack... crushedBlocks) {
        super(Categories.MATERIALS, ore, ManualSieve.TYPE, fromSmallerArray(crushedBlocks));

        RecipeType.SMELTERY.register(new ItemStack[] {ore, null, null, null, null, null, null, null, null}, ingot);

        addItemHandler((EntityInteractHandler) (e, item1, offHand) -> e.setCancelled(true));
    }

    /**
     * For Slimefun ingot ores only
     */
    public CrushedOre(SlimefunItemStack ore, SlimefunItemStack dust, ItemStack ingot, ItemStack... crushedBlocks) {
        this(ore , ingot, crushedBlocks);
        Pulverizer.addRecipe(ore, new SlimefunItemStack(dust, 2), true);
    }
    
    private static SlimefunItemStack make(SlimefunItemStack dust, Material material) {
        return new SlimefunItemStack(
                "CRUSHED_" + dust.getItemId().replace("DUST", "ORE"),
                material,
                "&6Crushed " + Objects.requireNonNull(dust.getDisplayName()).replace(" Dust", "").concat(" Ore"),
                "&7Can be smelted into ingots or pulverized into dust"
        );
    }
    
    private static ItemStack[] fromSmallerArray(ItemStack[] array) {
        ItemStack[] recipe = new ItemStack[9];
        if (array.length > 0) {
            System.arraycopy(array, 0, recipe, 0, recipe.length);
        }
        return recipe;
    }

}
