package io.github.mooy1.simpleutils;

import io.github.mooy1.simpleutils.blocks.SimpleSieve;
import io.github.mooy1.simpleutils.blocks.SimpleWorkbench;
import io.github.mooy1.simpleutils.tools.MiningHammer;
import io.github.mooy1.simpleutils.tools.SimpleWrench;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import lombok.experimental.UtilityClass;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

@UtilityClass
public final class Setup {

    public static void setup(@Nonnull SimpleUtils plugin) {
        
        // blocks
        new SimpleWorkbench().register(plugin);
        new SimpleSieve().register(plugin);

        // tools
        new SimpleWrench().register(plugin);
        new MiningHammer(Material.IRON_PICKAXE, SlimefunItems.COPPER_INGOT, "&6Copper", 3, 1).register(plugin);
        new MiningHammer(Material.DIAMOND_PICKAXE, new ItemStack(Material.DIAMOND), "&bDiamond", 3, 1).register(plugin);
        new MiningHammer(Material.IRON_PICKAXE, SlimefunItems.REINFORCED_ALLOY_INGOT, "&7Reinforced", 3, 5).register(plugin);
        new MiningHammer(Material.NETHERITE_PICKAXE, SlimefunItems.CARBONADO, "&8Carbonado", 5, 5).register(plugin);
        
        // misc
        new SlimefunItem(Items.CATEGORY, Items.HAMMER_ROD, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                SlimefunItems.STEEL_INGOT, SlimefunItems.STEEL_INGOT, SlimefunItems.STEEL_INGOT,
                SlimefunItems.STEEL_INGOT, new ItemStack(Material.BLAZE_ROD), SlimefunItems.STEEL_INGOT,
                SlimefunItems.STEEL_INGOT, SlimefunItems.STEEL_INGOT, SlimefunItems.STEEL_INGOT
        }).register(plugin);
    }
}
