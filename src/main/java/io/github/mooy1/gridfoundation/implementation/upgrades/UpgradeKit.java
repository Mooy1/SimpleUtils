package io.github.mooy1.gridfoundation.implementation.upgrades;

import io.github.mooy1.gridfoundation.setup.Categories;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import lombok.Getter;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.Locale;

@Getter
public final class UpgradeKit extends SimpleSlimefunItem<ItemUseHandler> implements NotPlaceable {
    
    private final UpgradeType type;
    private final boolean conversion;

    public UpgradeKit(UpgradeType type, ItemStack[] recipe) {
        super(Categories.MAIN, type.getItem(), RecipeType.ENHANCED_CRAFTING_TABLE, recipe);
        this.type = type;
        this.conversion = false;
    }
    
    public UpgradeKit(UpgradeType type) {
        super(Categories.MAIN, makeConversionItem(type), RecipeType.ENHANCED_CRAFTING_TABLE, makeConversionRecipe(type));
        this.type = type;
        this.conversion = true;
    }
    
    private static ItemStack[] makeConversionRecipe(UpgradeType type) {
        ItemStack[] recipe = new ItemStack[9];
        for (int i = 1 ; i < type.tier() ; i++) {
            recipe[i - 1] = UpgradeType.values()[i].getItem();
        }
        return recipe;
    }
        
    private static SlimefunItemStack makeConversionItem(UpgradeType type) {
        return new SlimefunItemStack(
                type.getName().substring(2).toUpperCase(Locale.ROOT) + "_CONVERSION_KIT",
                type.getMaterial(),
                type.getName() + " Conversion Kit",
                "&7Use on a grid machine or generator",
                "&7to upgrade it's speed or generation",
                "&7Does not require any previous level"
        );
    }

    @Nonnull
    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
            e.setUseBlock(Event.Result.DENY);
            if (e.getClickedBlock().isPresent() && e.getSlimefunBlock().isPresent() && e.getSlimefunBlock().get() instanceof UpgradeableBlock) {
                UpgradeManager.onUpgrade(e.getPlayer(), e.getItem(), e.getClickedBlock().get().getLocation(), (UpgradeableBlock) e.getSlimefunBlock().get(), this);
            }
        };
    }

}
