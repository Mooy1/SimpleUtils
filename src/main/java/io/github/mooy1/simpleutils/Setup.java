package io.github.mooy1.simpleutils;

import io.github.mooy1.infinitylib.PluginUtils;
import io.github.mooy1.infinitylib.presets.RecipePreset;
import io.github.mooy1.simpleutils.blocks.EnhancedWorkbench;
import io.github.mooy1.simpleutils.blocks.ManualSieve;
import io.github.mooy1.simpleutils.tools.MiningHammer;
import io.github.mooy1.simpleutils.tools.Wrench;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import lombok.experimental.UtilityClass;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.Arrays;

@UtilityClass
public final class Setup {

    public static void setup(@Nonnull SimpleUtils plugin) {

        Category category = new Category(PluginUtils.getKey("main"), new CustomItem(Material.COMPOSTER), 0);

        // blocks
        new EnhancedWorkbench(category).register(plugin);
        new ManualSieve(category).register(plugin);

        // tools
        new Wrench(category, null).register(plugin);
        new MiningHammer(category, Material.IRON_PICKAXE, new ItemStack(Material.IRON_INGOT),
                ChatColor.GRAY, 3, 0, 3).register(plugin);
        new MiningHammer(category, Material.IRON_PICKAXE, SlimefunItems.COPPER_INGOT,
                ChatColor.GOLD, 3, 3, 0).register(plugin);
        new MiningHammer(category, Material.DIAMOND_PICKAXE, new ItemStack(Material.DIAMOND),
                ChatColor.AQUA, 3, 3, 3).register(plugin);
        new MiningHammer(category, Material.IRON_PICKAXE, SlimefunItems.REINFORCED_ALLOY_INGOT,
                ChatColor.GRAY, 3, 5, 5).register(plugin);
        new MiningHammer(category, Material.DIAMOND_PICKAXE, Materials.PLATINUM_INGOT,
                ChatColor.AQUA, 5, 5, 5).register(plugin);

        // mist
        new SlimefunItem(category, Materials.AUTOMATON_CORE, RecipeType.ENHANCED_CRAFTING_TABLE,
                fillRecipe()).register(plugin);
        new SlimefunItem(category, Materials.HAMMER_ROD, RecipeType.ENHANCED_CRAFTING_TABLE,
                fillRecipe(
                        new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_INGOT),
                        new ItemStack(Material.IRON_INGOT), new ItemStack(Material.STICK), new ItemStack(Material.IRON_INGOT),
                        new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_INGOT)
                )).register(plugin);

        // crushed blocks
        new SlimefunItem(category, Materials.CRUSHED_ANDESITE, RecipeType.ORE_CRUSHER,
                RecipePreset.firstItem(new ItemStack(Material.ANDESITE))).register(plugin);
        new SlimefunItem(category, Materials.CRUSHED_DIORITE, RecipeType.ORE_CRUSHER,
                RecipePreset.firstItem(new ItemStack(Material.DIORITE))).register(plugin);
        new SlimefunItem(category, Materials.CRUSHED_GRANITE, RecipeType.ORE_CRUSHER,
                RecipePreset.firstItem(new ItemStack(Material.GRANITE))).register(plugin);
        new SlimefunItem(category, Materials.DUST, RecipeType.ORE_CRUSHER,
                RecipePreset.firstItem(new ItemStack(Material.SAND))).register(plugin);
        new SlimefunItem(category, Materials.END_GRAVEL, RecipeType.ORE_CRUSHER,
                RecipePreset.firstItem(new ItemStack(Material.END_STONE))).register(plugin);
        new SlimefunItem(category, Materials.NETHER_GRAVEL, RecipeType.ORE_CRUSHER,
                RecipePreset.firstItem(new ItemStack(Material.NETHERRACK))).register(plugin);

        // ingots
        new SlimefunItem(category, Materials.PLATINUM_INGOT, RecipeType.SMELTERY,
                RecipePreset.firstItem(Materials.PLATINUM_DUST)).register(plugin);
        RecipeType.SMELTERY.register(RecipePreset.firstItem(Materials.NICKEL_DUST), SlimefunItems.NICKEL_INGOT);
        RecipeType.SMELTERY.register(RecipePreset.firstItem(Materials.COBALT_DUST), SlimefunItems.COBALT_INGOT);

        // dusts
        new SlimefunItem(category, Materials.PLATINUM_DUST, RecipeType.ORE_CRUSHER,
                RecipePreset.firstItem(Materials.CRUSHED_COBALT)).register(plugin);
        new SlimefunItem(category, Materials.PLATINUM_DUST, RecipeType.ORE_CRUSHER,
                RecipePreset.firstItem(Materials.CRUSHED_COBALT)).register(plugin);
        new SlimefunItem(category, Materials.PLATINUM_DUST, RecipeType.ORE_CRUSHER,
                RecipePreset.firstItem(Materials.CRUSHED_COBALT)).register(plugin);

        // crushed ores
        new SlimefunItem(category, Materials.CRUSHED_IRON, ManualSieve.TYPE,
                fillRecipe()).register(plugin);
        new SlimefunItem(category, Materials.CRUSHED_COPPER, ManualSieve.TYPE,
                fillRecipe()).register(plugin);
        new SlimefunItem(category, Materials.CRUSHED_ZINC, ManualSieve.TYPE,
                fillRecipe()).register(plugin);
        new SlimefunItem(category, Materials.CRUSHED_LEAD, ManualSieve.TYPE,
                fillRecipe()).register(plugin);
        new SlimefunItem(category, Materials.CRUSHED_GOLD, ManualSieve.TYPE,
                fillRecipe()).register(plugin);
        new SlimefunItem(category, Materials.CRUSHED_ALUMINUM, ManualSieve.TYPE,
                fillRecipe()).register(plugin);
        new SlimefunItem(category, Materials.CRUSHED_SILVER, ManualSieve.TYPE,
                fillRecipe()).register(plugin);
        new SlimefunItem(category, Materials.CRUSHED_TIN, ManualSieve.TYPE,
                fillRecipe()).register(plugin);
        new SlimefunItem(category, Materials.CRUSHED_MAGNESIUM, ManualSieve.TYPE,
                fillRecipe()).register(plugin);
        new SlimefunItem(category, Materials.CRUSHED_COBALT, ManualSieve.TYPE,
                fillRecipe()).register(plugin);
        new SlimefunItem(category, Materials.CRUSHED_NICKEL, ManualSieve.TYPE,
                fillRecipe()).register(plugin);
        new SlimefunItem(category, Materials.CRUSHED_PLATINUM, ManualSieve.TYPE,
                fillRecipe()).register(plugin);

    }

    private static ItemStack[] fillRecipe(ItemStack... items) {
        return Arrays.copyOf(items, 9);
    }

}
