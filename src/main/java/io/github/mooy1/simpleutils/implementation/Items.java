package io.github.mooy1.simpleutils.implementation;

import java.util.Arrays;
import javax.annotation.Nonnull;
import lombok.experimental.UtilityClass;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

import io.github.mooy1.simpleutils.SimpleUtils;
import io.github.mooy1.simpleutils.implementation.blocks.Elevator;
import io.github.mooy1.simpleutils.implementation.blocks.Sieve;
import io.github.mooy1.simpleutils.implementation.workbench.Workbench;
import io.github.mooy1.simpleutils.implementation.tools.MiningHammer;
import io.github.mooy1.simpleutils.implementation.tools.Wrench;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;

@UtilityClass
public final class Items {

    public static final SlimefunItemStack WRENCH = new SlimefunItemStack(
            "SIMPLE_WRENCH",
            Material.IRON_HOE,
            "&6Simple Wrench",
            "&eRight-Click to quickly dismantle cargo, capacitors, and machines"
    );
    public static final SlimefunItemStack HAMMER_ROD = new SlimefunItemStack(
            "HAMMER_ROD",
            Material.BLAZE_ROD,
            "&6Hammer Rod",
            "&7Core component of mining hammers"
    );
    public static final SlimefunItemStack SIEVE = new SlimefunItemStack(
            "SIMPLE_SIEVE",
            Material.COMPOSTER,
            "&6Simple Sieve",
            "&7Sifts gravel into dusts and materials"
    );
    public static final SlimefunItemStack ELEVATOR = new SlimefunItemStack(
            "SIMPLE_ELEVATOR",
            Material.QUARTZ_BLOCK,
            "&fSimple Elevator",
            "&7Crouch to go down, Jump to go up"
    );
    public static final SlimefunItemStack WORKBENCH = new SlimefunItemStack(
            "SIMPLE_WORKBENCH",
            Material.CRAFTING_TABLE,
            "&6Simple Workbench",
            "&7Can craft both vanilla and slimefun recipes"
    );

    public static void setup(@Nonnull SimpleUtils plugin) {
        Category category = new Category(SimpleUtils.inst().getKey("main"), new CustomItem(Material.COMPOSTER, "&6Simple Utils"), 0);
        
        // misc
        new SlimefunItem(category, HAMMER_ROD, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                SlimefunItems.STEEL_INGOT, SlimefunItems.STEEL_INGOT, SlimefunItems.STEEL_INGOT,
                SlimefunItems.STEEL_INGOT, new ItemStack(Material.BLAZE_ROD), SlimefunItems.STEEL_INGOT,
                SlimefunItems.STEEL_INGOT, SlimefunItems.STEEL_INGOT, SlimefunItems.STEEL_INGOT
        }).register(plugin);
        
        // blocks
        new Workbench(category, WORKBENCH, RecipeType.ENHANCED_CRAFTING_TABLE,
                Arrays.copyOf(new ItemStack[] {new ItemStack(Material.CRAFTING_TABLE)}, 9)
        ).register(plugin);
        new Sieve(category, SIEVE, new ItemStack[] {
                null, null, null,
                null, new ItemStack(Material.OAK_TRAPDOOR), null,
                null, new ItemStack(Material.COMPOSTER), null
        }, BlockFace.SELF).register(plugin);
        new Elevator(category, ELEVATOR, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                new ItemStack(Material.QUARTZ_BLOCK), new ItemStack(Material.QUARTZ_BLOCK), new ItemStack(Material.QUARTZ_BLOCK),
                new ItemStack(Material.QUARTZ_BLOCK), new ItemStack(Material.ENDER_PEARL), new ItemStack(Material.QUARTZ_BLOCK),
                new ItemStack(Material.QUARTZ_BLOCK), new ItemStack(Material.QUARTZ_BLOCK), new ItemStack(Material.QUARTZ_BLOCK)
        }).register(plugin);

        // tools
        new Wrench(category, WRENCH, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                SlimefunItems.ALUMINUM_INGOT, null, SlimefunItems.ALUMINUM_INGOT,
                null, SlimefunItems.SILVER_INGOT, null,
                null, SlimefunItems.ALUMINUM_INGOT, null
        }).register(plugin);
        new MiningHammer(category, Material.IRON_PICKAXE, "&6Copper", 3, 1).register(plugin);
        new MiningHammer(category, Material.DIAMOND_PICKAXE,  "&bDiamond", 3, 2).register(plugin);
        new MiningHammer(category, Material.IRON_PICKAXE,  "&7Reinforced", 3, 3).register(plugin);
        new MiningHammer(category, Material.NETHERITE_PICKAXE,  "&8Carbonado", 5, 4).register(plugin);
    }

}
