package io.github.mooy1.gridfoundation.implementation.blocks;

import io.github.mooy1.gridfoundation.setup.Categories;
import io.github.mooy1.infinitylib.objects.AbstractContainer;
import io.github.mooy1.infinitylib.presets.MenuPreset;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * Crafts vanilla and slimefun recipes O.o
 */
public class EnhancedWorkbench extends AbstractContainer {

    private static final int[] inputSlots = MenuPreset.craftingInput;
    
    public static final SlimefunItemStack ITEM = new SlimefunItemStack(
            "ENHANCED_WORKBENCH",
            Material.CRAFTING_TABLE,
            "&6Enhanced Workbench",
            "&7Can craft both vanilla and slimefun recipes"
    );
    
    public EnhancedWorkbench() {
        super(Categories.MAIN, ITEM, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                
        });
    }

    @Override
    public void onNewInstance(@Nonnull BlockMenu blockMenu, @Nonnull Block block) {
        
    }

    @Override
    public void tick(@Nonnull Block b, @Nonnull BlockMenu inv) {
        
    }

    @Override
    public void setupInv(@Nonnull BlockMenuPreset blockMenuPreset) {

    }

}
