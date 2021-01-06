package io.github.mooy1.gridfoundation.implementation.upgrades;

import io.github.mooy1.gridfoundation.setup.Categories;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public final class UpgradeKit extends SimpleSlimefunItem<ItemUseHandler> implements NotPlaceable {
    
    final UpgradeType type;

    public UpgradeKit(UpgradeType type, ItemStack[] recipe) {
        super(Categories.MAIN, type.getItem(), RecipeType.ENHANCED_CRAFTING_TABLE, recipe);
        this.type = type;
    }

    @Nonnull
    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
            e.setUseBlock(Event.Result.DENY);
            if (e.getClickedBlock().isPresent() && e.getSlimefunBlock().isPresent() && e.getSlimefunBlock().get() instanceof UpgradeableBlock) {
                UpgradeManager.onUpgrade(e.getPlayer(), e.getItem(), e.getClickedBlock().get().getLocation(), (UpgradeableBlock) e.getSlimefunBlock().get(), this.type);
            }
        };
    }

}
