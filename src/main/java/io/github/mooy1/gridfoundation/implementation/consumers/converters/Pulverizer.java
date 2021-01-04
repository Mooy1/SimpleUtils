package io.github.mooy1.gridfoundation.implementation.consumers.converters;

import io.github.mooy1.gridfoundation.implementation.upgrades.UpgradeType;
import io.github.mooy1.infinitylib.filter.ItemFilter;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.cscorelib2.collections.Pair;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

public final class Pulverizer extends AbstractConverter {

    public static final Pair<List<ItemStack>, Map<ItemFilter, Pair<ItemStack, Integer>>> recipes = makeRecipes( false,
            new ItemStack(Material.COBBLESTONE), new ItemStack(Material.GRAVEL), new ItemStack(Material.GRAVEL), new ItemStack(Material.SAND),
            new ItemStack(Material.IRON_INGOT), SlimefunItems.IRON_DUST, new ItemStack(Material.IRON_ORE), new SlimefunItemStack(SlimefunItems.IRON_DUST, 2),
            new ItemStack(Material.GOLD_INGOT), SlimefunItems.GOLD_DUST, new ItemStack(Material.GOLD_ORE), new SlimefunItemStack(SlimefunItems.GOLD_DUST, 2),
            SlimefunItems.GOLD_4K, SlimefunItems.GOLD_DUST, new ItemStack(Material.NETHER_GOLD_ORE), SlimefunItems.GOLD_DUST,
            new ItemStack(Material.COAL_ORE), new ItemStack(Material.COAL, 4), new ItemStack(Material.NETHER_QUARTZ_ORE), new ItemStack(Material.QUARTZ, 4),
            new ItemStack(Material.GLOWSTONE), new ItemStack(Material.GLOWSTONE_DUST, 4),
            new ItemStack(Material.DIAMOND_ORE), new ItemStack(Material.DIAMOND, 2), new ItemStack(Material.EMERALD_ORE), new ItemStack(Material.EMERALD, 2),
            new ItemStack(Material.LAPIS_ORE), new ItemStack(Material.LAPIS_LAZULI, 8), new ItemStack(Material.REDSTONE_ORE), new ItemStack(Material.REDSTONE, 8),
            new ItemStack(Material.BLAZE_ROD), new ItemStack(Material.BLAZE_POWDER, 5), new ItemStack(Material.BONE), new ItemStack(Material.BONE_MEAL, 5)
    );
    public static final SlimefunItemStack ITEM = make(4,"Pulverizer", "Pulverizes ores and ingots, into dusts", Material.GRAY_CONCRETE);

    public Pulverizer() {
        super(ITEM, recipes, 4, new ItemStack[] {
                
        });
    }

    @Nonnull
    @Override
    public UpgradeType getMaxLevel() {
        return UpgradeType.ULTIMATE;
    }
    
}
