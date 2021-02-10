package io.github.mooy1.simpleutils;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import lombok.experimental.UtilityClass;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Material;

import java.util.Locale;
import java.util.Objects;

@UtilityClass
public final class Materials {

    // dusts
    public static final SlimefunItemStack PLATINUM_DUST = dust(Material.SUGAR, "&3Platinum");
    public static final SlimefunItemStack NICKEL_DUST = dust(Material.SUGAR, "&7Nickel");
    public static final SlimefunItemStack COBALT_DUST = dust(Material.SUGAR, "&9Cobalt");

    private static SlimefunItemStack dust(Material material, String  name) {
        return new SlimefunItemStack(
                name.substring(2).toUpperCase(Locale.ROOT) + "_DUST",
                material,
                name + " Dust"
        );
    }

    // crushed ores
    public static final SlimefunItemStack CRUSHED_NICKEL = crushedOre(NICKEL_DUST, Material.LIGHT_BLUE_DYE);
    public static final SlimefunItemStack CRUSHED_COBALT = crushedOre(COBALT_DUST, Material.BLUE_DYE);
    public static final SlimefunItemStack CRUSHED_PLATINUM = crushedOre(PLATINUM_DUST, Material.CYAN_DYE);
    public static final SlimefunItemStack CRUSHED_COPPER = crushedOre(SlimefunItems.COPPER_DUST, Material.ORANGE_DYE);
    public static final SlimefunItemStack CRUSHED_GOLD = crushedOre(SlimefunItems.GOLD_DUST, Material.YELLOW_DYE);
    public static final SlimefunItemStack CRUSHED_SILVER = crushedOre(SlimefunItems.SILVER_DUST, Material.WHITE_DYE);
    public static final SlimefunItemStack CRUSHED_ALUMINUM = crushedOre(SlimefunItems.ALUMINUM_DUST, Material.LIGHT_GRAY_DYE);
    public static final SlimefunItemStack CRUSHED_LEAD = crushedOre(SlimefunItems.LEAD_DUST, Material.GRAY_DYE);
    public static final SlimefunItemStack CRUSHED_MAGNESIUM = crushedOre(SlimefunItems.MAGNESIUM_DUST, Material.GRAY_DYE);
    public static final SlimefunItemStack CRUSHED_IRON = crushedOre(SlimefunItems.IRON_DUST, Material.LIGHT_GRAY_DYE);
    public static final SlimefunItemStack CRUSHED_ZINC = crushedOre(SlimefunItems.ZINC_DUST, Material.LIGHT_GRAY_DYE);
    public static final SlimefunItemStack CRUSHED_TIN = crushedOre(SlimefunItems.TIN_DUST, Material.LIGHT_GRAY_DYE);

    private static SlimefunItemStack crushedOre(SlimefunItemStack dust, Material material) {
        return new SlimefunItemStack(
                "CRUSHED_" + dust.getItemId().replace("DUST", "ORE"),
                material,
                "&6Crushed " + Objects.requireNonNull(dust.getDisplayName()).replace(" Dust", "").concat(" Ore"),
                "&7Can be smelted into ingots or pulverized into dust"
        );
    }
    
    // crushed blocks
    public static final SlimefunItemStack DUST = crushedBlock("Dust", "Sand", Material.SAND);
    public static final SlimefunItemStack NETHER_GRAVEL = crushedBlock("Nether Gravel", "Netherrack", Material.NETHERRACK);
    public static final SlimefunItemStack END_GRAVEL = crushedBlock("Ender Gravel", "Endstone", Material.END_STONE);
    public static final SlimefunItemStack CRUSHED_DIORITE = crushedBlock("Crushed Diorite", "Diorite", Material.DIORITE);
    public static final SlimefunItemStack CRUSHED_ANDESITE = crushedBlock("Crushed Andesite", "Andesite", Material.ANDESITE);
    public static final SlimefunItemStack CRUSHED_GRANITE = crushedBlock("Crushed Granite", "Granite", Material.GRANITE);
    
    private static SlimefunItemStack crushedBlock(String name, String source, Material material) {
        return new SlimefunItemStack(
                "CRUSHED_" + source.toUpperCase(Locale.ROOT),
                material,
                "&6" + name
        );
    }
    
    // ingots
    public static final SlimefunItemStack PLATINUM_INGOT = ingot(Material.IRON_INGOT, "&3Platinum");

    private static SlimefunItemStack ingot(Material material, String name) {
        return new SlimefunItemStack(
                name.substring(2).toUpperCase(Locale.ROOT) + "_INGOT",
                material,
                name + " Ingot"
        );
    }

    
}
