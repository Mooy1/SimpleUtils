package io.github.mooy1.gridfoundation.implementation.consumers.converters;

import io.github.mooy1.gridfoundation.implementation.consumers.AbstractGridConsumer;
import io.github.mooy1.gridfoundation.implementation.grid.Grid;
import io.github.mooy1.gridfoundation.implementation.upgrades.UpgradeType;
import io.github.mooy1.infinitylib.filter.FilterType;
import io.github.mooy1.infinitylib.filter.ItemFilter;
import io.github.mooy1.infinitylib.presets.MenuPreset;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import me.mrCookieSlime.Slimefun.cscorelib2.collections.Pair;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Map;

public abstract class AbstractConverter extends AbstractGridConsumer implements RecipeDisplayItem {
    
    private static final int inputSlot = MenuPreset.slot1;
    private static final int outputSlot = MenuPreset.slot3;
    
    private final List<ItemStack> displayRecipes;
    private final Map<ItemFilter, Pair<ItemStack, Integer>> recipes;
    
    public AbstractConverter(SlimefunItemStack item, List<ItemStack> displayRecipes, Map<ItemFilter, Pair<ItemStack, Integer>> recipes, int consumption, ItemStack[] recipe) {
        super(item, recipe, MenuPreset.slot2, consumption);
        this.recipes = recipes;
        this.displayRecipes = displayRecipes;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onBreak(BlockBreakEvent e, Location l, BlockMenu menu, Grid grid) {
        super.onBreak(e, l, menu, grid);
        menu.dropItems(l, inputSlot, outputSlot);
    }

    @Override
    public void process(@Nonnull BlockMenu menu, @Nonnull Block b, @Nonnull UpgradeType type) {
        @Nullable ItemStack input = menu.getItemInSlot(inputSlot);
        if (input != null) {
            Pair<ItemStack, Integer> pair = this.recipes.get(new ItemFilter(input, FilterType.MIN_AMOUNT));
            if (pair != null) {
                int amount = Math.min(input.getAmount() / pair.getSecondValue(), type.getLevel());
                ItemStack output = pair.getFirstValue().clone();
                output.setAmount(output.getAmount() * amount);
                if (menu.fits(output, outputSlot)) {
                    menu.pushItem(output, outputSlot);
                    menu.consumeItem(inputSlot, pair.getSecondValue() * amount);
                }
            }
        }
    }
    
    @Override
    public int[] getTransportSlots(@Nonnull ItemTransportFlow flow) {
        if (flow == ItemTransportFlow.WITHDRAW) return new int[] {outputSlot};
        if (flow == ItemTransportFlow.INSERT) return new int[] {inputSlot};
        return new int[0];
    }

    @Override
    public void setupInv(@Nonnull BlockMenuPreset blockMenuPreset) {
        MenuPreset.setupBasicMenu(blockMenuPreset);
    }

    @Nonnull
    @Override
    public List<ItemStack> getDisplayRecipes() {
        return this.displayRecipes;
    }
    
    @Override
    public int getUpgradeSlot() {
        return 4;
    }

}
