package io.github.mooy1.gridfoundation.implementation.consumers.converters;

import io.github.mooy1.gridfoundation.implementation.upgrades.UpgradeType;
import io.github.mooy1.infinitylib.filter.ItemFilter;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.cscorelib2.collections.Pair;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

public final class Decompactor extends AbstractConverter {
    
    public static final Pair<List<ItemStack>, Map<ItemFilter, Pair<ItemStack, Integer>>> recipes = makeRecipes(true,
            new ItemStack(Material.COAL, 9), new ItemStack(Material.COAL_BLOCK), new ItemStack(Material.DIAMOND, 9), new ItemStack(Material.DIAMOND_BLOCK),
            new ItemStack(Material.EMERALD, 9), new ItemStack(Material.EMERALD_BLOCK), new ItemStack(Material.REDSTONE, 9), new ItemStack(Material.REDSTONE_BLOCK),
            new ItemStack(Material.IRON_INGOT, 9), new ItemStack(Material.IRON_BLOCK), new ItemStack(Material.NETHERITE_INGOT, 9), new ItemStack(Material.NETHERITE_BLOCK),
            new ItemStack(Material.GOLD_INGOT, 9), new ItemStack(Material.GOLD_BLOCK), new ItemStack(Material.LAPIS_LAZULI, 9), new ItemStack(Material.LAPIS_BLOCK),
            new ItemStack(Material.QUARTZ, 4), new ItemStack(Material.QUARTZ_BLOCK), new ItemStack(Material.SAND, 4), new ItemStack(Material.SANDSTONE)
    );
    public static final SlimefunItemStack ITEM = make(6,"Decompactor", "Decompresses blocks into ingots and materials", Material.SMOOTH_RED_SANDSTONE);

    public Decompactor() {
        super(ITEM, recipes, 6, new ItemStack[] {
                
        });
    }

    @Nonnull
    @Override
    public UpgradeType getMaxLevel() {
        return UpgradeType.HARDENED;
    }
}
