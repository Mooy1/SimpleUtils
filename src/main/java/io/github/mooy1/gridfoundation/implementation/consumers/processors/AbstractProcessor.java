package io.github.mooy1.gridfoundation.implementation.consumers.processors;

import io.github.mooy1.gridfoundation.implementation.consumers.AbstractGridConsumer;
import io.github.mooy1.gridfoundation.implementation.grid.PowerGrid;
import io.github.mooy1.gridfoundation.implementation.upgrades.UpgradeType;
import io.github.mooy1.gridfoundation.utils.BetterRecipeType;
import io.github.mooy1.infinitylib.filter.FilterType;
import io.github.mooy1.infinitylib.filter.ItemFilter;
import io.github.mooy1.infinitylib.presets.MenuPreset;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
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
import java.util.List;
import java.util.Map;

public abstract class AbstractProcessor extends AbstractGridConsumer implements RecipeDisplayItem {
    
    private static final Map<Location, Pair<MutableInt, ItemStack>> progressing = new HashMap<>();
    private static final int outputSlot = MenuPreset.slot3;
    private static final int inputSlot = MenuPreset.slot1;
    private static final int statusSlot = MenuPreset.slot2;
    
    private static final int time = 16;
    private final Map<ItemFilter, Pair<ItemStack, Integer>> recipes = new HashMap<>();
    private final List<ItemStack> displayRecipes = new ArrayList<>();
    private final String processing;
    
    public AbstractProcessor(SlimefunItemStack item, int consumption, String processing, BetterRecipeType type, ItemStack[] recipe) {
        super(item, recipe, 4, consumption);
        this.processing = processing;
        type.accept((stacks, stack) -> {
            ItemStack input = stacks[0];
            AbstractProcessor.this.recipes.put(new ItemFilter(input, FilterType.MIN_AMOUNT), new Pair<>(stack, input.getAmount()));
            AbstractProcessor.this.displayRecipes.add(input);
            AbstractProcessor.this.displayRecipes.add(stack);
        });
    }

    @Override
    public void onBreak(Player p, Block b, BlockMenu menu, PowerGrid grid) {
        menu.dropItems(b.getLocation(), inputSlot, outputSlot);
        progressing.remove(b.getLocation());
    }

    @Override
    public boolean process(@Nonnull BlockMenu menu, @Nonnull Block b) {
        Pair<MutableInt, ItemStack> progress = progressing.computeIfAbsent(b.getLocation(), k -> new Pair<>(new MutableInt(0), null));
        int ticks = AbstractProcessor.time / getLevel(b);
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
    
    private boolean tryStart(@Nonnull BlockMenu menu, @Nonnull Pair<MutableInt, ItemStack> progress, int ticks) {
        ItemStack input = menu.getItemInSlot(inputSlot);
        if (input != null) {
            Pair<ItemStack, Integer> output = this.recipes.get(new ItemFilter(input, FilterType.MIN_AMOUNT));
            if (output != null) {
                menu.consumeItem(inputSlot, output.getSecondValue());
                progress.getFirstValue().setValue(1);
                progress.setSecondValue(output.getFirstValue().clone());
                setStatus(menu, 1, ticks);
                return true;
            }
        }
        setStatus(menu, 0, ticks);
        return false;
    }

    @Override
    public void setupInv(@Nonnull BlockMenuPreset blockMenuPreset) {
        MenuPreset.setupBasicMenu(blockMenuPreset);
        blockMenuPreset.addItem(statusSlot, getStatus(0, time), ChestMenuUtils.getEmptyClickHandler());
    }
    
    private void setStatus(BlockMenu menu, int current, int ticks) {
         if (menu.hasViewer())  {
             menu.replaceExistingItem(statusSlot, getStatus(current, ticks), false);
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
    public int[] getTransportSlots(@Nonnull ItemTransportFlow flow) {
        if (flow == ItemTransportFlow.WITHDRAW) return new int[] {outputSlot};
        if (flow == ItemTransportFlow.INSERT) return new int[] {inputSlot};
        return new int[0];
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

    @Override
    public int getUpgradeSlot() {
        return 22;
    }

}
