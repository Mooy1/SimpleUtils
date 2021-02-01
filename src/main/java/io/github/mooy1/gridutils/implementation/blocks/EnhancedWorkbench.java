package io.github.mooy1.gridutils.implementation.blocks;

import io.github.mooy1.gridutils.setup.Categories;
import io.github.mooy1.gridutils.utils.CombinedTableService;
import io.github.mooy1.infinitylib.abstracts.AbstractContainer;
import io.github.mooy1.infinitylib.filter.FilterType;
import io.github.mooy1.infinitylib.filter.MultiFilter;
import io.github.mooy1.infinitylib.menus.TransferUtils;
import io.github.mooy1.infinitylib.presets.MenuPreset;
import io.github.mooy1.infinitylib.presets.RecipePreset;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import me.mrCookieSlime.Slimefun.cscorelib2.inventory.ItemUtils;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Crafts vanilla and slimefun recipes O.o
 */
public final class EnhancedWorkbench extends AbstractContainer {

    private static final int[] inputSlots = MenuPreset.craftingInput;
    private static final int[] outputSlot = MenuPreset.craftingOutput;
    private static final ItemStack NO_OUTPUT = new CustomItem(Material.BARRIER, "");
    public static final SlimefunItemStack ITEM = new SlimefunItemStack(
            "ENHANCED_WORKBENCH",
            Material.CRAFTING_TABLE,
            "&6Enhanced Workbench",
            "&7Can craft both vanilla and slimefun recipes"
    );
    
    public EnhancedWorkbench() {
        super(Categories.MAIN, ITEM, RecipeType.ENHANCED_CRAFTING_TABLE, RecipePreset.firstItem(new ItemStack(Material.CRAFTING_TABLE)));
        
        registerBlockHandler(getId(), (p, b, item, reason) -> {
            BlockMenu menu = BlockStorage.getInventory(b);
            if (menu != null) {
                menu.dropItems(b.getLocation(), inputSlots);
            }
            return true;
        });
    }

    private static void craft(Player p, BlockMenu menu) {
        Optional<ItemStack> output = CombinedTableService.getOutput(MultiFilter.fromMenu(FilterType.IGNORE_AMOUNT, menu, inputSlots));
        if (!output.isPresent()) {
            return;
        }
        ItemStack hand = p.getItemOnCursor();
        if (hand.getType() == Material.AIR) {
           p.setItemOnCursor(output.get().clone());
        } else if (ItemUtils.canStack(output.get(), hand)) { // check for pdc
            hand.setAmount(hand.getAmount() + output.get().getAmount());
        } else {
            return;
        }
        for (int i : inputSlots) {
            menu.consumeItem(i, 1);
        }
    }

    private static void craftMax(Player p, BlockMenu menu) {
        MultiFilter input = MultiFilter.fromMenu(FilterType.IGNORE_AMOUNT, menu, inputSlots);
        Optional<ItemStack> output = CombinedTableService.getOutput(input);
        if (!output.isPresent()) {
            return;
        }
        int amount = 65;
        // find smalled amount greater than 0
        for (int i : input.getAmounts()) {
            if (i > 0 && i < amount) {
                amount = i;
            }
        }
        if (amount == 65) {
            // this would only happen if there was a registered empty recipe
            return;
        }
        int consume = 0;
        Inventory inv = p.getInventory();
        // output as many as possible
        while (TransferUtils.tryInsertToVanillaInventory(output.get().clone(), inv) && consume < amount) {
            consume++;
        }
        // consume correct amount
        for (int i : inputSlots) {
            menu.consumeItem(i, consume);
        }
        refreshOutput(menu);
    }
    
    private static void refreshOutput(@Nonnull BlockMenu menu) {
        menu.replaceExistingItem(outputSlot[0], CombinedTableService.getOutput(MultiFilter.fromMenu(FilterType.IGNORE_AMOUNT, menu, inputSlots)).orElse(NO_OUTPUT));
    }

    @Override
    protected void setupMenu(@Nonnull BlockMenuPreset blockMenuPreset) {
        blockMenuPreset.drawBackground(MenuPreset.borderItemInput, inputSlots);
        blockMenuPreset.drawBackground(MenuPreset.borderItemOutput, outputSlot);
        blockMenuPreset.drawBackground(MenuPreset.craftingBackground);
    }

    @Override
    protected void onNewInstance(@Nonnull BlockMenu menu, @Nonnull Block b) {
        ChestMenu.MenuClickHandler update = (player, i, itemStack, clickAction) -> {
            if (clickAction.isShiftClicked()) {
                refreshOutput(menu);
            }
            return true;
        };
        menu.addPlayerInventoryClickHandler(update);
        for (int i : inputSlots) {
            menu.addMenuClickHandler(i, update);
        }
        menu.addMenuClickHandler(outputSlot[0], (player, i, itemStack, clickAction) -> {
            if (clickAction.isShiftClicked()) {
                craftMax(player, menu);
            } else {
                craft(player, menu);
            }
            return false;
        });
        refreshOutput(menu);
    }

    @Nonnull
    @Override
    protected int[] getTransportSlots(@Nonnull DirtyChestMenu dirtyChestMenu, @Nonnull ItemTransportFlow itemTransportFlow, ItemStack itemStack) {
        return new int[0];
    }

}
