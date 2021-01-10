package io.github.mooy1.gridfoundation.implementation.blocks;

import io.github.mooy1.gridfoundation.setup.Categories;
import io.github.mooy1.infinitylib.objects.AbstractContainer;
import io.github.mooy1.infinitylib.presets.MenuPreset;
import io.github.mooy1.infinitylib.presets.RecipePreset;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.UnregisterReason;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import javax.annotation.Nonnull;

/**
 * Crafts vanilla and slimefun recipes O.o
 */
public class EnhancedWorkbench extends SlimefunItem {

    private static final int[] inputSlots = MenuPreset.craftingInput;
    private static final int outputSlot = MenuPreset.craftingOutput;
    
    public static final SlimefunItemStack ITEM = new SlimefunItemStack(
            "ENHANCED_WORKBENCH",
            Material.CRAFTING_TABLE,
            "&6Enhanced Workbench",
            "&7Can craft both vanilla and slimefun recipes"
    );
    
    public EnhancedWorkbench() {
        super(Categories.MAIN, ITEM, RecipeType.ENHANCED_CRAFTING_TABLE, RecipePreset.firstItem(new ItemStack(Material.CRAFTING_TABLE)));
        
        new BlockMenuPreset(getId(), getItemName()) {

            @Override
            public void init() {
                drawBackground(MenuPreset.borderItemInput, inputSlots);
                drawBackground(MenuPreset.borderItemOutput, new int[] {outputSlot});
                drawBackground(MenuPreset.craftingBackground);
            }

            @Override
            public void newInstance(@Nonnull BlockMenu menu, @Nonnull Block b) {
                ChestMenu.MenuClickHandler update = (player, i, itemStack, clickAction) -> {
                    if (clickAction.isShiftClicked()) {
                        refreshOutput(menu);
                    }
                    return true;
                };
                addPlayerInventoryClickHandler(update);
                for (int i : inputSlots) {
                    addMenuClickHandler(i, update);
                }
                addMenuClickHandler(outputSlot, (player, i, itemStack, clickAction) -> craft(player, menu, clickAction.isShiftClicked()));
            }

            @Override
            public boolean canOpen(@Nonnull Block b, @Nonnull Player p) {
                return false;
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
                return new int[0];
            }
        };
        
        registerBlockHandler(getId(), (p, b, item, reason) -> {
            BlockMenu menu = BlockStorage.getInventory(b);
            if (menu != null) {
                menu.dropItems(b.getLocation(), inputSlots);
            }
            return true;
        });
    }

    private static boolean craft(Player p, BlockMenu menu, boolean max) {
        
    }
    
    private static void refreshOutput(BlockMenu menu) {
        
    }
    
    private static ItemStack getOutput(BlockMenu menu) {
        
    }
    
}
