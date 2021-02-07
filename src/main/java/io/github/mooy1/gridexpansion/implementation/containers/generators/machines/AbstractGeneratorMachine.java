package io.github.mooy1.gridexpansion.implementation.containers.generators.machines;

import io.github.mooy1.gridexpansion.implementation.containers.generators.AbstractGridGenerator;
import io.github.mooy1.gridexpansion.implementation.upgrades.UpgradeType;
import io.github.mooy1.gridexpansion.utils.GridLorePreset;
import io.github.mooy1.infinitylib.filter.FilterType;
import io.github.mooy1.infinitylib.filter.ItemFilter;
import io.github.mooy1.infinitylib.items.LoreUtils;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import org.apache.commons.lang.mutable.MutableInt;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public abstract class AbstractGeneratorMachine extends AbstractGridGenerator implements RecipeDisplayItem {
    
    static void addRecipe(Map<ItemFilter, Integer> recipes, Material material, int ticks) {
        recipes.put(new ItemFilter(material, FilterType.IGNORE_AMOUNT), ticks);
    }

    static void addRecipe(Map<ItemFilter, Integer> recipes, SlimefunItemStack stack, int ticks) {
        recipes.put(new ItemFilter(stack, FilterType.IGNORE_AMOUNT), ticks);
    }
    
    private static final int INPUT = 13;
    private static final int STATUS = 4;

    private final Map<Location, MutableInt> progressing = new HashMap<>();
    private final Map<ItemFilter, Integer> recipes;
    private final int power;
    private final List<ItemStack> displayRecipes = new ArrayList<>();

    public AbstractGeneratorMachine(SlimefunItemStack item, int power,  Map<ItemFilter, Integer> recipes, ItemStack[] recipe) {
        super(item, recipe);
        this.recipes = recipes;
        this.power = power;
        for (Map.Entry<ItemFilter, Integer> entry : this.recipes.entrySet()) {
            ItemStack stack = entry.getKey().toItem();
            LoreUtils.addLore(stack, "", ChatColor.YELLOW + "Lasts: " + entry.getValue() + " ticks");
            this.displayRecipes.add(stack);
        }
    }

    private static ItemStack getStatus(int ticks) {
        if (ticks == 0) {
            return new CustomItem(Material.BARRIER, "&cIdle...");
        } else {
            return new CustomItem(Material.FLINT_AND_STEEL, "&6Remaining Ticks: " + ticks);
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
        return UpgradeType.ULTIMATE;
    }

    @Override
    public int getUpgradeSlot() {
        return 3;
    }
    
    static SlimefunItemStack make(@Nonnull String name, @Nonnull String source, int gp, @Nonnull Material material) {
        return new SlimefunItemStack(
                name.toUpperCase(Locale.ROOT) + "_GRID_GENERATOR",
                material,
                "&e" + name + " Generator &6",
                "&7Generates power from " + source,
                "",
                GridLorePreset.generatesGridPower(gp)
        );
    }

    @Override
    public void getStats(@Nonnull List<String> stats, int tier) {
        super.getStats(stats, tier);
        stats.add("&6Consumption: &e" + (1 << Math.min(0, tier - 1)) + "x");
    }
    
    @Override
    protected void onBreak(@Nonnull BlockBreakEvent e, @Nonnull BlockMenu menu) {
        super.onBreak(e, menu);
        this.progressing.remove(e.getBlock().getLocation());
    }

    @Override
    protected void setupMenu(@Nonnull BlockMenuPreset blockMenuPreset) {
        for (int i = 0 ; i < 18 ; i++) {
            if (i == INPUT || i == STATUS) i++;
            blockMenuPreset.addItem(i, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }
        blockMenuPreset.addItem(STATUS, getStatus(0), ChestMenuUtils.getEmptyClickHandler());
    }

    @Nonnull
    @Override
    protected int[] getTransportSlots(@Nonnull DirtyChestMenu dirtyChestMenu, @Nonnull ItemTransportFlow itemTransportFlow, ItemStack itemStack) {
        if (itemTransportFlow == ItemTransportFlow.INSERT) return new int[] {INPUT};
        return new int[0];
    }

    @Override
    protected int generate(BlockMenu menu, Block b, Config config, int tier) {
        MutableInt remaining = this.progressing.computeIfAbsent(b.getLocation(), k -> new MutableInt(0));
        if (remaining.intValue() > 0) {
            remaining.decrement();
            if (menu.hasViewer()) {
                menu.replaceExistingItem(STATUS, getStatus(remaining.intValue()));
            }
            return this.power;
        } else {
            @Nullable ItemStack input = menu.getItemInSlot(AbstractGeneratorMachine.INPUT);
            int speed = 1 << Math.min(0, tier - 1);
            if (input != null && input.getAmount() >= speed) {
                Integer ticks = this.recipes.get(new ItemFilter(input, FilterType.IGNORE_AMOUNT));
                if (ticks != null) {
                    menu.consumeItem(AbstractGeneratorMachine.INPUT, speed);
                    remaining.setValue(ticks);
                    if (menu.hasViewer()) {
                        menu.replaceExistingItem(STATUS, getStatus(ticks));
                    }
                    return this.power;
                }
            }
        }
        if (menu.hasViewer()) {
            menu.replaceExistingItem(STATUS, getStatus(0));
        }
        return 0;
    }

    @Override
    protected int getStatusSlot() {
        return 4;
    }

}
