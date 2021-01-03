package io.github.mooy1.gridfoundation.implementation.upgrades;

import io.github.mooy1.gridfoundation.setup.Categories;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import org.bukkit.inventory.ItemStack;

public final class UpgradeKit extends SlimefunItem implements NotPlaceable {
    
    final UpgradeType type;

    public UpgradeKit(UpgradeType type, ItemStack[] recipe) {
        super(Categories.MAIN, type.item, RecipeType.ENHANCED_CRAFTING_TABLE, recipe);
        this.type = type;
    }

}
