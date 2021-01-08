package io.github.mooy1.gridfoundation.implementation.consumers.machines.multi;

import io.github.mooy1.gridfoundation.implementation.consumers.machines.AbstractProcessor;
import io.github.mooy1.gridfoundation.implementation.grid.Grid;
import io.github.mooy1.gridfoundation.utils.BetterRecipeType;
import io.github.mooy1.infinitylib.filter.FilterType;
import io.github.mooy1.infinitylib.filter.ItemFilter;
import io.github.mooy1.infinitylib.filter.MultiFilter;
import io.github.mooy1.infinitylib.presets.MenuPreset;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import me.mrCookieSlime.Slimefun.cscorelib2.collections.Pair;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class AbstractMultiProcessor extends AbstractProcessor {

    private final Map<MultiFilter, Pair<ItemStack, int[]>> recipes = new HashMap<>();
    private final Map<ItemFilter, Set<Integer>> slots = new HashMap<>();
    private static final int[] inputSlots = {
            10, 11, 12
    };
    private static final int[] inputBorder = {
            0, 1, 2, 3, 4,
            9, 13,
            18, 19, 20, 21, 22
    };
    private static final int[] extra = {
            5, 23
    };
    private static final int EMPTY = new MultiFilter(FilterType.IGNORE_AMOUNT, new ItemStack[3]).hashCode();

    public AbstractMultiProcessor(SlimefunItemStack item, Material progressMaterial, BetterRecipeType type, int consumption, ItemStack[] recipe) {
        super(item, new ArrayList<>(), progressMaterial, 64, consumption, 5, 14, new int[] {MenuPreset.slot3}, recipe);
        
        type.acceptEach((itemStacks, stack) -> {
            ItemStack[] input = new ItemStack[3];
            System.arraycopy(itemStacks, 0, input, 0, 3);
            MultiFilter filter = new MultiFilter(FilterType.MIN_AMOUNT, input);

            // recipe
            this.recipes.put(filter, new Pair<>(stack, filter.getAmounts()));

            // display recipes
            super.displayRecipes.add(input[0]);
            super.displayRecipes.add(null);
            super.displayRecipes.add(input[1]);
            super.displayRecipes.add(stack);
            super.displayRecipes.add(input[2]);
            super.displayRecipes.add(null);

            // slots
            for (int i = 0 ; i < input.length ; i++) {
                this.slots.computeIfAbsent(new ItemFilter(input[i], FilterType.IGNORE_AMOUNT), k -> new HashSet<>(2)).add(inputSlots[i]);
            }
        });
    }

    @Override
    public void onBreak(@Nonnull BlockBreakEvent e, @Nonnull Location l, @Nonnull BlockMenu menu, @Nonnull Grid grid) {
        super.onBreak(e, l, menu, grid);
        menu.dropItems(l, inputSlots);
    }

    @Override
    protected final ItemStack getOutput(BlockMenu menu, int max) {
        MultiFilter input = MultiFilter.fromMenu(FilterType.MIN_AMOUNT, menu, inputSlots);
        if (input.hashCode() != EMPTY) {
            Pair<ItemStack, int[]> output = this.recipes.get(input);
            if (output != null) {
                for (int i = 0 ; i < output.getSecondValue().length ; i ++) {
                    int amount = output.getSecondValue()[i];
                    if (amount > 0) {
                        menu.consumeItem(inputSlots[i], amount);
                    }
                }
                return output.getFirstValue().clone();
            }
        }
        return null;
    }
    
    @Override
    public int[] getTransportSlots(@Nonnull DirtyChestMenu menu, @Nonnull ItemTransportFlow flow, @Nonnull ItemStack item) {
        if (flow == ItemTransportFlow.WITHDRAW) return this.outputSlots;
        if (flow == ItemTransportFlow.INSERT) {
            ItemFilter filter = new ItemFilter(item, FilterType.IGNORE_AMOUNT);
            for (int i : inputSlots) {
                if (filter.fits(new ItemFilter(menu.getItemInSlot(i), FilterType.IGNORE_AMOUNT))) {
                    return new int[] {i};
                }
            }
            Set<Integer> slots = this.slots.get(filter);
            if (slots != null) {
                for (int i : slots) {
                    if (menu.getItemInSlot(i) != null) return new int[] {i};
                }
            }
        }
        return new int[0];
    }
    
    @Override
    public void setupInv(@Nonnull BlockMenuPreset blockMenuPreset) {
        super.setupInv(blockMenuPreset);
        for (int i : MenuPreset.slotChunk3) {
            blockMenuPreset.addItem(i, MenuPreset.borderItemOutput, ChestMenuUtils.getEmptyClickHandler());
        }
        for (int i : inputBorder) {
            blockMenuPreset.addItem(i, MenuPreset.borderItemInput, ChestMenuUtils.getEmptyClickHandler());
        }
        for (int i : extra) {
            blockMenuPreset.addItem(i, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }
    }
    
    @Override
    public int getUpgradeSlot() {
        return 23;
    }
    
}
