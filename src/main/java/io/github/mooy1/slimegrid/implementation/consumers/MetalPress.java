package io.github.mooy1.slimegrid.implementation.consumers;

import io.github.mooy1.infinitylib.filter.FilterType;
import io.github.mooy1.infinitylib.filter.ItemFilter;
import io.github.mooy1.infinitylib.presets.MenuPreset;
import io.github.mooy1.slimegrid.implementation.grid.PowerGrid;
import io.github.mooy1.slimegrid.lists.Items;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
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

public final class MetalPress extends AbstractGridConsumer implements RecipeDisplayItem {
    
    private static final Map<ItemFilter, Pair<ItemStack, Integer>> recipes = new HashMap<>();
    private static final Map<Location, Pair<MutableInt, ItemStack>> progressing = new HashMap<>();
    private static final List<ItemStack> displayRecipes = new ArrayList<>();
    private static final int outputSlot = MenuPreset.slot3;
    private static final int inputSlot = MenuPreset.slot1;
    private static final int statusSlot = MenuPreset.slot2;
    
    private final int ticks;

    static {
        addRecipe(new SlimefunItemStack(SlimefunItems.COPPER_INGOT, 5), Items.COPPER_GEAR);
        addRecipe(new SlimefunItemStack(SlimefunItems.COBALT_INGOT, 5), Items.COBALT_GEAR);
        addRecipe(new SlimefunItemStack(SlimefunItems.MAGNESIUM_INGOT, 5), Items.MAGNESIUM_GEAR);
        addRecipe(new ItemStack(Material.GOLD_INGOT, 5), Items.GOLDEN_GEAR);
        addRecipe(new SlimefunItemStack(SlimefunItems.LEAD_INGOT, 16), Items.LEAD_PLATE);
        addRecipe(new SlimefunItemStack(SlimefunItems.ALUMINUM_INGOT, 16), Items.ALUMINUM_PLATE);
        addRecipe(new SlimefunItemStack(SlimefunItems.BRONZE_INGOT, 16), Items.BRONZE_PLATE);
        addRecipe(new SlimefunItemStack(SlimefunItems.TIN_INGOT, 16), Items.TIN_PLATE);
        addRecipe(new ItemStack(Material.IRON_INGOT, 16), Items.IRON_PLATE);
    }

    private static void addRecipe(ItemStack input, SlimefunItemStack output) {
        recipes.put(new ItemFilter(input, FilterType.MIN_AMOUNT), new Pair<>(output, input.getAmount()));
        displayRecipes.add(input);
        displayRecipes.add(output);
    }

    public MetalPress(SlimefunItemStack item, int consumption, int ticks, ItemStack[] recipe) {
        super(item, recipe, 4, consumption);
        this.ticks = ticks;
    }

    @Override
    public void onBreak(Player p, Block b, Location l, BlockMenu menu, PowerGrid grid) {
        menu.dropItems(b.getLocation(), inputSlot, outputSlot);
        progressing.remove(l);
    }

    @Override
    public boolean process(@Nonnull BlockMenu menu, @Nonnull Block b) {
        Pair<MutableInt, ItemStack> progress = progressing.computeIfAbsent(b.getLocation(), k -> new Pair<>(new MutableInt(0), null));
        if (progress.getFirstValue().intValue() == 0) {
            tryStart(menu, progress);
        } else if (progress.getFirstValue().intValue() >= this.ticks) {
            if (menu.fits(progress.getSecondValue(), outputSlot)) {
                menu.pushItem(progress.getSecondValue(), outputSlot);
                progress.getFirstValue().setValue(0);
                tryStart(menu, progress);
            }
        } else {
            progress.getFirstValue().increment();
        }
        menu.replaceExistingItem(statusSlot, new CustomItem(), false);
        return false;
    }
    
    private void tryStart(@Nonnull BlockMenu menu, @Nonnull Pair<MutableInt, ItemStack> progress) {
        ItemStack input = menu.getItemInSlot(inputSlot);
        if (input != null) {
            Pair<ItemStack, Integer> output = recipes.get(new ItemFilter(input, FilterType.IGNORE_AMOUNT));
            if (output != null) {
                menu.consumeItem(inputSlot, output.getSecondValue());
                progress.getFirstValue().setValue(1);
                progress.setSecondValue(output.getFirstValue().clone());
            }
        }
    }

    @Override
    public void setupInv(@Nonnull BlockMenuPreset blockMenuPreset) {

    }

    @Nonnull
    @Override
    public List<ItemStack> getDisplayRecipes() {
        return displayRecipes;
    }

}
