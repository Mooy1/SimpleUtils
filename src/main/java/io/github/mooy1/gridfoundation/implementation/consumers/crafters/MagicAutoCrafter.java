package io.github.mooy1.gridfoundation.implementation.consumers.crafters;

import io.github.mooy1.infinitylib.filter.FilterType;
import io.github.mooy1.infinitylib.filter.MultiFilter;
import io.github.mooy1.gridfoundation.utils.GridLorePreset;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.cscorelib2.collections.Pair;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public final class MagicAutoCrafter extends AbstractAutoCrafter {

    public MagicAutoCrafter() {
        super(new SlimefunItemStack(
                "MAGIC_AUTO_CRAFTER",
                Material.BOOKSHELF,
                "&eAuto Magic Workbench",
                "&7Automatically crafts magic items and runes using GP",
                "",
                GridLorePreset.consumesGridPower(8)
        ), 8, new ItemStack[] {
                
        });
    }

    @Override
    Pair<MultiFilter, ItemStack> getRecipe(@Nonnull ItemStack item) {
        SlimefunItem slimefunItem = SlimefunItem.getByItem(item);
        if (slimefunItem != null && slimefunItem.getRecipe().length == 9 && (
                slimefunItem.getRecipeType() == RecipeType.MAGIC_WORKBENCH
                        || slimefunItem.getRecipeType() == RecipeType.ANCIENT_ALTAR
        )) {
            return new Pair<>(new MultiFilter(FilterType.MIN_AMOUNT, slimefunItem.getRecipe()), slimefunItem.getRecipeOutput());
        }
        return null;
    }

}
