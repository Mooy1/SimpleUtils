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
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.Arrays;

@UtilityClass
public final class Setup {

    public static void setup(@Nonnull SimpleUtils plugin) {

        Category main = new Category(PluginUtils.getKey("main"),
                new CustomItem(Material.COMPOSTER, "&6Simple Utils"), 0);
        Category materials = new Category(PluginUtils.getKey("materials"),
                new CustomItem(Material.ORANGE_DYE, "&6Simple Materials"), 0);

        // blocks
        new EnhancedWorkbench(main).register(plugin);
        new ManualSieve(main).register(plugin);

        // tools
        new Wrench(main, null).register(plugin);
        new MiningHammer(main, Material.IRON_PICKAXE, new ItemStack(Material.IRON_INGOT),
                ChatColor.GRAY, 2, 0, 3).register(plugin);
        new MiningHammer(main, Material.IRON_PICKAXE, SlimefunItems.COPPER_INGOT,
                ChatColor.GOLD, 3, 3, 0).register(plugin);
        new MiningHammer(main, Material.DIAMOND_PICKAXE, new ItemStack(Material.DIAMOND),
                ChatColor.AQUA, 3, 3, 3).register(plugin);
        new MiningHammer(main, Material.IRON_PICKAXE, SlimefunItems.REINFORCED_ALLOY_INGOT,
                ChatColor.GRAY, 4, 5, 5).register(plugin);
        new MiningHammer(main, Material.DIAMOND_PICKAXE, Materials.PLATINUM_INGOT,
                ChatColor.AQUA, 5, 5, 5).register(plugin);

        // misc
        new SlimefunItem(materials, Materials.AUTOMATON_CORE, RecipeType.ENHANCED_CRAFTING_TABLE,
                fillRecipe()).register(plugin);
        new SlimefunItem(materials, Materials.HAMMER_ROD, RecipeType.ENHANCED_CRAFTING_TABLE,
                fillRecipe(
                        new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_INGOT),
                        new ItemStack(Material.IRON_INGOT), new ItemStack(Material.STICK), new ItemStack(Material.IRON_INGOT),
                        new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_INGOT)
                )).register(plugin);

        // crushed blocks
        new SlimefunItem(materials, Materials.CRUSHED_ANDESITE, RecipeType.GRIND_STONE,
                RecipePreset.firstItem(new ItemStack(Material.ANDESITE))).register(plugin);
        new SlimefunItem(materials, Materials.CRUSHED_DIORITE, RecipeType.GRIND_STONE,
                RecipePreset.firstItem(new ItemStack(Material.DIORITE))).register(plugin);
        new SlimefunItem(materials, Materials.CRUSHED_GRANITE, RecipeType.GRIND_STONE,
                RecipePreset.firstItem(new ItemStack(Material.GRANITE))).register(plugin);
        new SlimefunItem(materials, Materials.DUST, RecipeType.GRIND_STONE,
                RecipePreset.firstItem(new ItemStack(Material.SAND))).register(plugin);
        new SlimefunItem(materials, Materials.END_GRAVEL, RecipeType.GRIND_STONE,
                RecipePreset.firstItem(new ItemStack(Material.END_STONE))).register(plugin);
        new SlimefunItem(materials, Materials.NETHER_GRAVEL, RecipeType.GRIND_STONE,
                RecipePreset.firstItem(new ItemStack(Material.NETHERRACK))).register(plugin);

        // ingots
        new SlimefunItem(materials, Materials.PLATINUM_INGOT, RecipeType.SMELTERY,
                RecipePreset.firstItem(Materials.PLATINUM_DUST)).register(plugin);
        RecipeType.SMELTERY.register(RecipePreset.firstItem(Materials.NICKEL_DUST), SlimefunItems.NICKEL_INGOT);
        RecipeType.SMELTERY.register(RecipePreset.firstItem(Materials.COBALT_DUST), SlimefunItems.COBALT_INGOT);

        // dusts
        new SlimefunItem(materials, Materials.PLATINUM_DUST, RecipeType.ORE_CRUSHER,
                RecipePreset.firstItem(Materials.CRUSHED_PLATINUM)).register(plugin);
        new SlimefunItem(materials, Materials.COBALT_DUST, RecipeType.ORE_CRUSHER,
                RecipePreset.firstItem(Materials.CRUSHED_COBALT)).register(plugin);
        new SlimefunItem(materials, Materials.NICKEL_DUST, RecipeType.ORE_CRUSHER,
                RecipePreset.firstItem(Materials.CRUSHED_NICKEL)).register(plugin);

        // crushed ores, null for stuff that already gets its recipe added
        registerCrushedOre(materials, Materials.CRUSHED_PLATINUM, Materials.PLATINUM_INGOT, null,
                Materials.END_GRAVEL);
        registerCrushedOre(materials, Materials.CRUSHED_COBALT, SlimefunItems.COBALT_INGOT, null,
                Materials.END_GRAVEL);
        registerCrushedOre(materials, Materials.CRUSHED_NICKEL, SlimefunItems.NICKEL_INGOT, null,
                Materials.CRUSHED_ANDESITE);
        registerCrushedOre(materials, Materials.CRUSHED_IRON, new ItemStack(Material.IRON_INGOT), SlimefunItems.IRON_DUST,
                new ItemStack(Material.GRAVEL));
        registerCrushedOre(materials, Materials.CRUSHED_COPPER, SlimefunItems.COPPER_INGOT, SlimefunItems.COPPER_DUST,
                new ItemStack(Material.GRAVEL), new ItemStack(Material.SAND));
        registerCrushedOre(materials, Materials.CRUSHED_ZINC, SlimefunItems.ZINC_INGOT, SlimefunItems.ZINC_DUST,
                new ItemStack(Material.GRAVEL));
        registerCrushedOre(materials, Materials.CRUSHED_TIN, SlimefunItems.TIN_INGOT, SlimefunItems.TIN_DUST,
                new ItemStack(Material.GRAVEL));
        registerCrushedOre(materials, Materials.CRUSHED_SILVER, SlimefunItems.SILVER_INGOT, SlimefunItems.SILVER_DUST,
                new ItemStack(Material.SAND));
        registerCrushedOre(materials, Materials.CRUSHED_GOLD, new ItemStack(Material.GOLD_INGOT), SlimefunItems.GOLD_DUST,
                new ItemStack(Material.SAND), Materials.NETHER_GRAVEL);
        registerCrushedOre(materials, Materials.CRUSHED_MAGNESIUM, SlimefunItems.MAGNESIUM_INGOT, SlimefunItems.MAGNESIUM_DUST,
                new ItemStack(Material.SAND));
        registerCrushedOre(materials, Materials.CRUSHED_LEAD, SlimefunItems.LEAD_INGOT, SlimefunItems.LEAD_DUST,
                new ItemStack(Material.GRAVEL));
        registerCrushedOre(materials, Materials.CRUSHED_ALUMINUM, SlimefunItems.ALUMINUM_INGOT, SlimefunItems.ALUMINUM_DUST,
                new ItemStack(Material.GRAVEL));

    }
    
    private static void registerCrushedOre(Category category, SlimefunItemStack stack,
                                           ItemStack ingot, ItemStack dust, ItemStack... recipe) {
        new SlimefunItem(category, stack, ManualSieve.TYPE, fillRecipe(recipe)).register(SimpleUtils.getInstance());
        RecipeType.SMELTERY.register(RecipePreset.firstItem(stack), ingot);
        if (dust != null) {
            RecipeType.ORE_CRUSHER.register(RecipePreset.firstItem(stack), dust);
        }
    }

    private static ItemStack[] fillRecipe(ItemStack... items) {
        return Arrays.copyOf(items, 9);
    }

}
