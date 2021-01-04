package io.github.mooy1.gridfoundation.implementation.consumers.converters;

import io.github.mooy1.gridfoundation.implementation.consumers.AbstractGridConsumer;
import io.github.mooy1.gridfoundation.implementation.grid.PowerGrid;
import io.github.mooy1.infinitylib.filter.FilterType;
import io.github.mooy1.infinitylib.filter.ItemFilter;
import io.github.mooy1.infinitylib.presets.MenuPreset;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import me.mrCookieSlime.Slimefun.cscorelib2.collections.Pair;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractConverter extends AbstractGridConsumer implements RecipeDisplayItem {
    
    private static final int inputSlot = MenuPreset.slot1;
    private static final int outputSlot = MenuPreset.slot3;
    
    private final List<ItemStack> displayRecipes;
    private final Map<ItemFilter, Pair<ItemStack, Integer>> recipes;
    
    public AbstractConverter(SlimefunItemStack item, Pair<List<ItemStack>, Map<ItemFilter, Pair<ItemStack, Integer>>> recipes, int consumption, ItemStack[] recipe) {
        super(item, recipe, MenuPreset.slot2, consumption);
        this.recipes = recipes.getSecondValue();
        this.displayRecipes = recipes.getFirstValue();
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onBreak(Player p, Block b, BlockMenu menu, PowerGrid grid) {
        menu.dropItems(b.getLocation(), inputSlot, outputSlot);
    }

    @Override
    public boolean process(@Nonnull BlockMenu menu, @Nonnull Block b) {
            @Nullable ItemStack input = menu.getItemInSlot(inputSlot);
            if (input != null) {
                Pair<ItemStack, Integer> pair = this.recipes.get(new ItemFilter(input, FilterType.IGNORE_AMOUNT));
                if (pair != null) {
                    int amount = Math.min(input.getAmount() / pair.getSecondValue(), getLevel(b));
                    ItemStack output = pair.getFirstValue().clone();
                    output.setAmount(output.getAmount() * amount);
                    if (menu.fits(output, outputSlot)) {
                        menu.pushItem(output, outputSlot);
                        menu.consumeItem(inputSlot, pair.getSecondValue() * amount);
                        return true;
                    }
                }
            }
        return false;
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

    static Pair<List<ItemStack>, Map<ItemFilter, Pair<ItemStack, Integer>>> makeRecipes(boolean inverse, ItemStack... items) {
        List<ItemStack> list = new ArrayList<>();
        Map<ItemFilter, Pair<ItemStack, Integer>> map = new HashMap<>();
        
        if (inverse) {
            for (int i = 0 ; i < items.length ; i +=2) {
                list.add(items[i + 1]);
                list.add(items[i]);
                map.put(new ItemFilter(items[i + 1], FilterType.IGNORE_AMOUNT), new Pair<>(items[i], items[i + 1].getAmount()));
            }
        } else {
            for (int i = 0 ; i < items.length ; i +=2) {
                list.add(items[i]);
                list.add(items[i + 1]);
                map.put(new ItemFilter(items[i], FilterType.IGNORE_AMOUNT), new Pair<>(items[i + 1], items[i].getAmount()));
            }
        }
        
        return new Pair<>(list, map);
    }

    @Override
    public int getUpgradeSlot() {
        return 4;
    }

}
