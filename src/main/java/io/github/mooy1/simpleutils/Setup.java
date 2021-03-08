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
import java.util.Arrays;

@UtilityClass
public final class Setup {

    public static void setup(@Nonnull SimpleUtils plugin) {
        
        // blocks
        new SimpleWorkbench().register(plugin);
        new SimpleSieve().register(plugin);

        // tools
        new SimpleWrench().register(plugin);
        new MiningHammer(Material.IRON_PICKAXE, new ItemStack(Material.IRON_INGOT), "&fIron", 2, 0, 3).register(plugin);
        new MiningHammer(Material.IRON_PICKAXE, SlimefunItems.COPPER_INGOT, "&6Copper", 3, 3, 0).register(plugin);
        new MiningHammer(Material.DIAMOND_PICKAXE, new ItemStack(Material.DIAMOND), "&bDiamond", 3, 3, 3).register(plugin);
        new MiningHammer(Material.IRON_PICKAXE, SlimefunItems.REINFORCED_ALLOY_INGOT, "&7Reinforced", 4, 4, 5).register(plugin);
        new MiningHammer(Material.NETHERITE_PICKAXE, SlimefunItems.CARBON_CHUNK, "&8Carbon", 5, 5, 7).register(plugin);
        
        // dusts
        new SlimefunItem(Items.CATEGORY, Items.COBALT_DUST, SimpleSieve.TYPE, fill(new ItemStack(Material.GRAVEL))).register(plugin);
        new SlimefunItem(Items.CATEGORY, Items.NICKEL_DUST, SimpleSieve.TYPE, fill(new ItemStack(Material.GRAVEL))).register(plugin);

        // ingots
        RecipeType.SMELTERY.register(fill(Items.NICKEL_DUST), SlimefunItems.NICKEL_INGOT);
        RecipeType.SMELTERY.register(fill(Items.COBALT_DUST), SlimefunItems.COBALT_INGOT);
        
        // misc
        new SlimefunItem(Items.CATEGORY, Items.HAMMER_ROD, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                SlimefunItems.STEEL_INGOT, SlimefunItems.STEEL_INGOT, SlimefunItems.STEEL_INGOT,
                SlimefunItems.STEEL_INGOT, new ItemStack(Material.STICK), SlimefunItems.STEEL_INGOT,
                SlimefunItems.STEEL_INGOT, SlimefunItems.STEEL_INGOT, SlimefunItems.STEEL_INGOT
        }).register(plugin);
    }

    private static ItemStack[] fill(ItemStack... items) {
        return Arrays.copyOf(items, 9);
    }

}
