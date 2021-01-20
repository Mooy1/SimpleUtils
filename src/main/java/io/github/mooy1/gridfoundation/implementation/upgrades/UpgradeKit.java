package io.github.mooy1.gridfoundation.implementation.upgrades;

import io.github.mooy1.gridfoundation.setup.Categories;
import io.github.mooy1.infinitylib.player.MessageUtils;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.Locale;

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
    
    @Nonnull
    private static ItemStack[] makeConversionRecipe(@Nonnull UpgradeType type) {
        ItemStack[] recipe = new ItemStack[9];
        for (int i = 1 ; i < type.tier() ; i++) {
            recipe[i - 1] = UpgradeType.values()[i].getItem();
        }
        return recipe;
    }
        
    private static SlimefunItemStack makeConversionItem(@Nonnull UpgradeType type) {
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
    public final ItemUseHandler getItemHandler() {
        return e -> {
            e.setUseBlock(Event.Result.DENY);
            if (e.getClickedBlock().isPresent() && e.getSlimefunBlock().isPresent() && e.getSlimefunBlock().get() instanceof UpgradeableBlock) {
                
                UpgradeableBlock block = (UpgradeableBlock) e.getSlimefunBlock().get();
                Player p = e.getPlayer();
                
                if (block.getMaxLevel() == UpgradeType.BASIC) {

                    MessageUtils.message(p, "&eThis Block cannot be upgraded!");
                    return;

                }

                Location target = e.getClickedBlock().get().getLocation();

                UpgradeType old = UpgradeManager.getUpgrade(target);

                if (old.tier() >= block.getMaxLevel().ordinal()) {

                    MessageUtils.message(p, "&eThis Block cannot be upgraded any further!");

                } else if (this.conversion || this.type.tier() - old.tier() == 1) {

                    UpgradeManager.setUpgrade(target, this.type, block);

                    if (p.getGameMode() != GameMode.CREATIVE) {
                        e.getItem().setAmount(e.getItem().getAmount() - 1);
                    }

                    if (this.conversion) {
                        MessageUtils.message(p, "&aConverted to " + this.type.getName());
                    } else {
                        MessageUtils.message(p, "&aUpgraded to " + this.type.getName());
                    }

                    p.playSound(target, Sound.BLOCK_ANVIL_USE, 1, 1);

                } else if (old.tier() + 1 < UpgradeType.values().length) {

                    MessageUtils.message(p, "&eThis Block requires an " + UpgradeType.values()[old.tier() + 1].getItem().getDisplayName() + "&e to be upgraded further!");

                }
            }
        };
    }

}
