package io.github.mooy1.gridfoundation.implementation.consumers.presses;

import io.github.mooy1.infinitylib.filter.FilterType;
import io.github.mooy1.infinitylib.filter.ItemFilter;
import io.github.mooy1.infinitylib.presets.MenuPreset;
import io.github.mooy1.gridfoundation.implementation.consumers.AbstractGridConsumer;
import io.github.mooy1.gridfoundation.implementation.grid.PowerGrid;
import io.github.mooy1.gridfoundation.implementation.upgrades.UpgradeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractPress extends AbstractGridConsumer implements RecipeDisplayItem {

    private static final Map<Location, Pair<MutableInt, ItemStack>> progressing = new HashMap<>();
    private static final int outputSlot = MenuPreset.slot3;
    private static final int inputSlot = MenuPreset.slot1;
    private static final int statusSlot = MenuPreset.slot2;
    private static final int max = 16;
    
    private final Map<ItemFilter, Pair<ItemStack, Integer>> recipes;
    private final List<ItemStack> displayRecipes;

    public static void addRecipe(Pair<Map<ItemFilter, Pair<ItemStack, Integer>>, List<ItemStack>> recipes, ItemStack input, ItemStack output) {
        recipes.getFirstValue().put(new ItemFilter(input, FilterType.MIN_AMOUNT), new Pair<>(output, input.getAmount()));
        recipes.getSecondValue().add(input);
        recipes.getSecondValue().add(output);
    }

    public AbstractPress(SlimefunItemStack item, int consumption, Pair<Map<ItemFilter, Pair<ItemStack, Integer>>, List<ItemStack>> recipes, ItemStack[] recipe) {
        super(item, recipe, 4, consumption);
        this.recipes = recipes.getFirstValue();
        this.displayRecipes = recipes.getSecondValue();
    }

    @Override
    public void onBreak(Player p, Block b, BlockMenu menu, PowerGrid grid) {
        menu.dropItems(b.getLocation(), inputSlot, outputSlot);
        progressing.remove(b.getLocation());
    }

    @Override
    public boolean process(@Nonnull BlockMenu menu, @Nonnull Block b) {
        Pair<MutableInt, ItemStack> progress = progressing.computeIfAbsent(b.getLocation(), k -> new Pair<>(new MutableInt(0), null));
        int ticks = max / getLevel(b);
        if (progress.getFirstValue().intValue() == 0) {
            tryStart(menu, progress);
        } else if (progress.getFirstValue().intValue() >= ticks) {
            if (menu.fits(progress.getSecondValue(), outputSlot)) {
                menu.pushItem(progress.getSecondValue(), outputSlot);
                progress.getFirstValue().setValue(0);
                tryStart(menu, progress);
            }
        } else {
            progress.getFirstValue().increment();
        }
        menu.replaceExistingItem(statusSlot, getStatus(progress.getFirstValue().intValue(), ticks), false);
        return false;
    }
    
    private void tryStart(@Nonnull BlockMenu menu, @Nonnull Pair<MutableInt, ItemStack> progress) {
        ItemStack input = menu.getItemInSlot(inputSlot);
        if (input != null) {
            Pair<ItemStack, Integer> output = this.recipes.get(new ItemFilter(input, FilterType.IGNORE_AMOUNT));
            if (output != null) {
                menu.consumeItem(inputSlot, output.getSecondValue());
                progress.getFirstValue().setValue(1);
                progress.setSecondValue(output.getFirstValue().clone());
            }
        }
    }

    @Override
    public void setupInv(@Nonnull BlockMenuPreset blockMenuPreset) {
        MenuPreset.setupBasicMenu(blockMenuPreset);
        blockMenuPreset.addItem(statusSlot, getStatus(0, max), ChestMenuUtils.getEmptyClickHandler());
    }
    
    private ItemStack getStatus(int ticks, int max) {
        if (ticks == 0) {
            return new CustomItem(Material.BARRIER, "&cIdle...");
        } else {
            return new CustomItem(Material.PISTON, "&aPressing... " + ticks + "/" + max);
        }
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
        return 0;
    }

}
