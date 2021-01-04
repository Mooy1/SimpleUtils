package io.github.mooy1.gridfoundation.implementation.consumers.combiners;

import io.github.mooy1.gridfoundation.implementation.consumers.AbstractGridConsumer;
import io.github.mooy1.gridfoundation.implementation.grid.PowerGrid;
import io.github.mooy1.gridfoundation.implementation.upgrades.UpgradeType;
import io.github.mooy1.gridfoundation.utils.BetterRecipeType;
import io.github.mooy1.infinitylib.filter.FilterType;
import io.github.mooy1.infinitylib.filter.ItemFilter;
import io.github.mooy1.infinitylib.filter.MultiFilter;
import io.github.mooy1.infinitylib.presets.MenuPreset;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import me.mrCookieSlime.Slimefun.cscorelib2.collections.Pair;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import org.apache.commons.lang.mutable.MutableInt;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AbstractCombiner extends AbstractGridConsumer implements RecipeDisplayItem {

    private static final Map<Location, Pair<MutableInt, ItemStack>> progressing = new HashMap<>();
    private static final int[] inputSlots = {
            10, 11, 12
    };
    private static final int[] inputBorder = {
            0, 1, 2, 3, 4,
            9, 13,
            18, 19, 20, 21, 22
    };
    private static final int status = 14;
    private static final int[] extra = {
            5, 23
    };
    private static final int time = 16;
    private static final int outputSlot = MenuPreset.slot3;

    private final Map<MultiFilter, Pair<ItemStack, int[]>> recipes = new HashMap<>();
    private final Map<ItemFilter, Set<Integer>> slots = new HashMap<>();
    private final List<ItemStack> displayRecipes = new ArrayList<>();
    private final String processing;

    public AbstractCombiner(SlimefunItemStack item, BetterRecipeType type, String processing, int consumption, ItemStack[] recipe) {
        super(item, recipe, 5, consumption);
        this.processing = processing;
        type.accept((itemStacks, stack) -> {
            ItemStack[] input = new ItemStack[3];
            System.arraycopy(itemStacks, 0, input, 0, 3);
            MultiFilter filter = new MultiFilter(FilterType.MIN_AMOUNT, input);

            // recipe
            this.recipes.put(filter, new Pair<>(stack, filter.getAmounts()));

            // display recipes
            this.displayRecipes.add(input[0]);
            this.displayRecipes.add(null);
            this.displayRecipes.add(input[1]);
            this.displayRecipes.add(stack);
            this.displayRecipes.add(input[2]);
            this.displayRecipes.add(null);

            // slots
            for (int i = 0 ; i < input.length ; i++) {
                this.slots.computeIfAbsent(new ItemFilter(input[i], FilterType.IGNORE_AMOUNT), k -> new HashSet<>(2)).add(inputSlots[i]);
            }
        });
    }
    
    @Override
    public void onBreak(Player p, Block b, BlockMenu menu, PowerGrid grid) {
        menu.dropItems(b.getLocation(), inputSlots);
        menu.dropItems(b.getLocation(), outputSlot);
        progressing.remove(b.getLocation());
    }

    @Override
    public boolean process(@Nonnull BlockMenu menu, @Nonnull Block b) {
        Pair<MutableInt, ItemStack> progress = progressing.computeIfAbsent(b.getLocation(), k -> new Pair<>(new MutableInt(0), null));
        int ticks = time / getLevel(b);
        if (progress.getFirstValue().intValue() == 0) {
            return tryStart(menu, progress, ticks);
        } else if (progress.getFirstValue().intValue() >= ticks) {
            if (menu.fits(progress.getSecondValue(), outputSlot)) {
                menu.pushItem(progress.getSecondValue(), outputSlot);
                progress.getFirstValue().setValue(0);
                progress.setSecondValue(null);
                return tryStart(menu, progress, ticks);
            }
        } else {
            progress.getFirstValue().increment();
            setStatus(menu, progress.getFirstValue().intValue(), ticks);
            return true;
        }
        setStatus(menu, 0, ticks);
        return false;
    }
    
    private static final int EMPTY = new MultiFilter(FilterType.IGNORE_AMOUNT, new ItemStack[3]).hashCode();

    private boolean tryStart(@Nonnull BlockMenu menu, @Nonnull Pair<MutableInt, ItemStack> progress, int ticks) {
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
                progress.getFirstValue().setValue(1);
                progress.setSecondValue(output.getFirstValue().clone());
                setStatus(menu, 1, ticks);
                return true;
            }
        }
        setStatus(menu, 0, ticks);
        return false;
    }

    private void setStatus(BlockMenu menu, int current, int ticks) {
        if (menu.hasViewer())  {
            menu.replaceExistingItem(status, getStatus(current, ticks), false);
        }
    }

    private ItemStack getStatus(int current, int ticks) {
        if (current == 0) {
            return new CustomItem(Material.BARRIER, "&cIdle...");
        } else {
            return new CustomItem(Material.PISTON, "&a" + this.processing + "... " + current + "/" + ticks);
        }
    }
    
    @Override
    public int[] getTransportSlots(@Nonnull DirtyChestMenu menu, @Nonnull ItemTransportFlow flow, @Nonnull ItemStack item) {
        if (flow == ItemTransportFlow.WITHDRAW) return new int[] {outputSlot};
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
        for (int i : MenuPreset.slotChunk3) {
            blockMenuPreset.addItem(i, MenuPreset.borderItemOutput, ChestMenuUtils.getEmptyClickHandler());
        }
        for (int i : inputBorder) {
            blockMenuPreset.addItem(i, MenuPreset.borderItemInput, ChestMenuUtils.getEmptyClickHandler());
        }
        for (int i : extra) {
            blockMenuPreset.addItem(i, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }
        blockMenuPreset.addItem(status, getStatus(0, time), ChestMenuUtils.getEmptyClickHandler());
    }
    
    @Override
    public int getUpgradeSlot() {
        return 23;
    }

    @Nonnull
    @Override
    public List<ItemStack> getDisplayRecipes() {
        return this.displayRecipes;
    }

    @Nonnull
    @Override
    public UpgradeType getMaxLevel() {
        return UpgradeType.REINFORCED;
    }

}
