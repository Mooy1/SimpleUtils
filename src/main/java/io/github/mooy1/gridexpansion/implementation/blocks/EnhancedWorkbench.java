package io.github.mooy1.gridexpansion.implementation.blocks;

import io.github.mooy1.gridexpansion.setup.Categories;
import io.github.mooy1.infinitylib.PluginUtils;
import io.github.mooy1.infinitylib.abstracts.AbstractContainer;
import io.github.mooy1.infinitylib.filter.FilterType;
import io.github.mooy1.infinitylib.filter.MultiFilter;
import io.github.mooy1.infinitylib.filter.RecipeFilter;
import io.github.mooy1.infinitylib.menus.TransferUtils;
import io.github.mooy1.infinitylib.presets.MenuPreset;
import io.github.mooy1.infinitylib.presets.RecipePreset;
import io.github.thebusybiscuit.slimefun4.core.multiblocks.MultiBlockMachine;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import me.mrCookieSlime.Slimefun.cscorelib2.inventory.ItemUtils;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Crafts vanilla and slimefun recipes O.o
 */
public final class EnhancedWorkbench extends AbstractContainer {
    
    private static final int[] INPUT_SLOTS = MenuPreset.craftingInput;
    private static final int OUTPUT_SLOT = MenuPreset.craftingOutput[0];
    private static final ItemStack NO_OUTPUT = new CustomItem(Material.BARRIER, "");
    public static final SlimefunItemStack ITEM = new SlimefunItemStack(
            "ENHANCED_WORKBENCH",
            Material.CRAFTING_TABLE,
            "&6Enhanced Workbench",
            "&7Can craft both vanilla and slimefun recipes"
    );
    
    private final Map<MultiFilter, ItemStack> recipes = new HashMap<>();

    public EnhancedWorkbench() {
        super(Categories.MAIN, ITEM, RecipeType.ENHANCED_CRAFTING_TABLE, RecipePreset.firstItem(new ItemStack(Material.CRAFTING_TABLE)));
        
        SlimefunPlugin.getMinecraftRecipeService().subscribe(recipeSnapshot -> {
            for (ShapedRecipe recipe : recipeSnapshot.getRecipes(ShapedRecipe.class)) {
                this.recipes.put(RecipeFilter.fromRecipe(recipe, FilterType.IGNORE_AMOUNT), recipe.getResult());
            }
            for (ShapelessRecipe recipe : recipeSnapshot.getRecipes(ShapelessRecipe.class)) {
                this.recipes.put(RecipeFilter.fromRecipe(recipe, FilterType.IGNORE_AMOUNT), recipe.getResult());
            }
        });
        
        PluginUtils.runSync(() -> {
            List<ItemStack[]> list = ((MultiBlockMachine) RecipeType.ENHANCED_CRAFTING_TABLE.getMachine()).getRecipes();
            for (int i = 0 ; i < list.size() ; i+=2) {
                ItemStack[] recipe = list.get(i);
                if (!ArrayUtils.isEmpty(recipe)) {
                    this.recipes.put(new MultiFilter(FilterType.IGNORE_AMOUNT, recipe), list.get(i + 1)[0]);
                }
            }
        }, 50);
    }

    @Override
    protected void onBreak(@Nonnull BlockBreakEvent e, @Nonnull BlockMenu menu) {
        menu.dropItems(e.getBlock().getLocation(), INPUT_SLOTS);
    }

    private void craft(Player p, BlockMenu menu) {
        ItemStack output = this.recipes.get(MultiFilter.fromMenu(FilterType.IGNORE_AMOUNT, menu, INPUT_SLOTS));
        if (output == null) {
            return;
        }
        ItemStack hand = p.getItemOnCursor();
        if (hand.getType() == Material.AIR) {
           p.setItemOnCursor(output.clone());
        } else if (ItemUtils.canStack(output, hand)
                && output.getItemMeta().getPersistentDataContainer()
                .equals(hand.getItemMeta().getPersistentDataContainer())) {
            hand.setAmount(hand.getAmount() + output.getAmount());
        } else {
            return;
        }
        for (int i : INPUT_SLOTS) {
            menu.consumeItem(i, 1);
        }
    }

    private void craftMax(Player p, BlockMenu menu) { 
        MultiFilter input = MultiFilter.fromMenu(FilterType.IGNORE_AMOUNT, menu, INPUT_SLOTS);
        ItemStack output = this.recipes.get(input);
        if (output == null) {
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
        while (TransferUtils.tryInsertToVanillaInventory(output.clone(), inv) && consume < amount) {
            consume++;
        }
        // consume correct amount
        for (int i = 0 ; i < 9 ; i++) {
            if (input.getAmounts()[i] != 0) {
                menu.consumeItem(i, consume, true);
            }
        }
        refreshOutput(menu);
    }
    
    private void refreshOutput(@Nonnull BlockMenu menu) {
        ItemStack output = this.recipes.get(MultiFilter.fromMenu(FilterType.IGNORE_AMOUNT, menu, INPUT_SLOTS));
        if (output == null) {
            menu.replaceExistingItem(OUTPUT_SLOT, NO_OUTPUT);
        } else {
            menu.replaceExistingItem(OUTPUT_SLOT, output);
        }
    }

    @Override
    protected void setupMenu(@Nonnull BlockMenuPreset blockMenuPreset) {
        blockMenuPreset.drawBackground(MenuPreset.borderItemInput, MenuPreset.craftingInputBorder);
        blockMenuPreset.drawBackground(MenuPreset.borderItemOutput, MenuPreset.craftingOutputBorder);
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
        for (int i : INPUT_SLOTS) {
            menu.addMenuClickHandler(i, update);
        }
        menu.addMenuClickHandler(OUTPUT_SLOT, (player, i, itemStack, clickAction) -> {
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
