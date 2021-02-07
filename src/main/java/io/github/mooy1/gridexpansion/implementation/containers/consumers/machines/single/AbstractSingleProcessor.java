package io.github.mooy1.gridexpansion.implementation.containers.consumers.machines.single;

import io.github.mooy1.gridexpansion.implementation.containers.consumers.machines.AbstractProcessor;
import io.github.mooy1.infinitylib.filter.FilterType;
import io.github.mooy1.infinitylib.filter.ItemFilter;
import io.github.mooy1.infinitylib.presets.MenuPreset;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import me.mrCookieSlime.Slimefun.cscorelib2.collections.Pair;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractSingleProcessor extends AbstractProcessor {

    private static final int inputSlot = MenuPreset.slot1;
    protected final Map<ItemFilter, Pair<ItemStack, Integer>> recipes = new HashMap<>();

    public AbstractSingleProcessor(SlimefunItemStack item, Material progressMaterial, int base, ItemStack[] recipe) {
        super(item, new ArrayList<>(), progressMaterial, base,  MenuPreset.slot2, new int[] {MenuPreset.slot3}, recipe);
    }

    @Override
    protected final ItemStack getOutput(BlockMenu menu, int max) {
        ItemStack input = menu.getItemInSlot(inputSlot);
        if (input != null) {
            Pair<ItemStack, Integer> output = this.recipes.get(new ItemFilter(input, FilterType.MIN_AMOUNT));
            if (output != null) {
                int amount = Math.min(max, input.getAmount() / output.getSecondValue());
                menu.consumeItem(inputSlot, output.getSecondValue() * amount);
                ItemStack item = output.getFirstValue().clone();
                item.setAmount(item.getAmount() * amount);
                return item;
            }
        }
        return null;
    }

    @Override
    public int getUpgradeSlot() {
        return 22;
    }

    @Override
    protected int getStatusSlot() {
        return 4;
    }

    @Nonnull
    @Override
    protected int[] getTransportSlots(@Nonnull DirtyChestMenu dirtyChestMenu, @Nonnull ItemTransportFlow flow, ItemStack itemStack) {
        if (flow == ItemTransportFlow.WITHDRAW) return this.outputSlots;
        if (flow == ItemTransportFlow.INSERT) return new int[] {inputSlot};
        return new int[0];
    }

    @Override
    protected void onBreak(@Nonnull BlockBreakEvent e, @Nonnull BlockMenu menu) {
        super.onBreak(e, menu);
        menu.dropItems(e.getBlock().getLocation(), inputSlot);
    }

    @Override
    protected void setupMenu(@Nonnull BlockMenuPreset blockMenuPreset) {
        super.setupMenu(blockMenuPreset);
        MenuPreset.setupBasicMenu(blockMenuPreset);
    }

}
