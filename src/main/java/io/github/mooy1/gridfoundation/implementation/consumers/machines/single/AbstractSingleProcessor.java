package io.github.mooy1.gridfoundation.implementation.consumers.machines.single;

import io.github.mooy1.gridfoundation.implementation.consumers.machines.AbstractProcessor;
import io.github.mooy1.gridfoundation.implementation.powergrid.PowerGrid;
import io.github.mooy1.infinitylib.filter.FilterType;
import io.github.mooy1.infinitylib.filter.ItemFilter;
import io.github.mooy1.infinitylib.presets.MenuPreset;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import me.mrCookieSlime.Slimefun.cscorelib2.collections.Pair;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

public abstract class AbstractSingleProcessor extends AbstractProcessor {

    private static final int inputSlot = MenuPreset.slot1;
    protected final Map<ItemFilter, Pair<ItemStack, Integer>> recipes;

    public AbstractSingleProcessor(SlimefunItemStack item, Material progressMaterial, int base,
                                   Map<ItemFilter, Pair<ItemStack, Integer>> recipes, List<ItemStack> displayRecipes,
                                   int consumption, ItemStack[] recipe
    ) {
        super(item, displayRecipes, progressMaterial, base, consumption, 4, MenuPreset.slot2, new int[] {MenuPreset.slot3}, recipe);
        this.recipes = recipes;
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
    public void onBreak(@Nonnull BlockBreakEvent e, @Nonnull Location l, @Nonnull BlockMenu menu, @Nonnull PowerGrid grid) {
        super.onBreak(e, l, menu, grid);
        menu.dropItems(l, inputSlot);
    }

    @Nonnull
    @Override
    public int[] getTransportSlots(@Nonnull ItemTransportFlow flow) {
        if (flow == ItemTransportFlow.WITHDRAW) return this.outputSlots;
        if (flow == ItemTransportFlow.INSERT) return new int[] {inputSlot};
        return new int[0];
    }

    @Override
    public void setupInv(@Nonnull BlockMenuPreset blockMenuPreset) {
        super.setupInv(blockMenuPreset);
        MenuPreset.setupBasicMenu(blockMenuPreset);
    }

    @Override
    public int getUpgradeSlot() {
        return 22;
    }
    
}
