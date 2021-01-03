package io.github.mooy1.gridfoundation.implementation.generators;

import io.github.mooy1.infinitylib.filter.FilterType;
import io.github.mooy1.infinitylib.filter.ItemFilter;
import io.github.mooy1.infinitylib.items.LoreUtils;
import io.github.mooy1.infinitylib.presets.MenuPreset;
import io.github.mooy1.gridfoundation.implementation.grid.PowerGrid;
import io.github.mooy1.gridfoundation.implementation.upgrades.UpgradeManager;
import io.github.mooy1.gridfoundation.implementation.upgrades.UpgradeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun4.utils.tags.SlimefunTag;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import org.apache.commons.lang.mutable.MutableInt;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class PowerGenerator extends AbstractGridGenerator implements RecipeDisplayItem {

    public static final int SURVIVAL = 2;
    public static final int OVERCLOCKED = 48;

    public static final Map<ItemFilter, Integer> survivalRecipes = makeSurvivalRecipes();
    
    private static Map<ItemFilter, Integer> makeSurvivalRecipes() {
        Map<ItemFilter, Integer> survivalRecipes = new HashMap<>();
        
        for (Material material : SlimefunTag.PLANKS.getValues()) {
            addRecipe(survivalRecipes, material, 1);
        }
        for (Material material : SlimefunTag.LOGS.getValues()) {
            addRecipe(survivalRecipes, material, 4);
        }
        addRecipe(survivalRecipes, Material.CHARCOAL, 6);
        addRecipe(survivalRecipes, Material.COAL, 8);
        addRecipe(survivalRecipes, Material.COAL_BLOCK, 72);
        addRecipe(survivalRecipes, Material.LAVA_BUCKET, 100);
        addRecipe(survivalRecipes, SlimefunItems.CARBON, 64);
        addRecipe(survivalRecipes, SlimefunItems.COMPRESSED_CARBON, 256);
        addRecipe(survivalRecipes, SlimefunItems.CARBON_CHUNK, 2048);
        addRecipe(survivalRecipes, SlimefunItems.CARBONADO, 4096);
        
        return survivalRecipes;
    }
    
    protected static void addRecipe(Map<ItemFilter, Integer> recipes, Material material, int ticks) {
        recipes.put(new ItemFilter(new ItemStack(material, 1), FilterType.IGNORE_AMOUNT), ticks - 1);
    }

    protected static void addRecipe(Map<ItemFilter, Integer> recipes, SlimefunItemStack stack, int ticks) {
        recipes.put(new ItemFilter(stack, FilterType.IGNORE_AMOUNT), ticks - 1);
    }
    
    private static final int input = MenuPreset.slot2 + 9;
    private static final int status = MenuPreset.slot2;

    private final Map<Location, MutableInt> progressing = new HashMap<>();
    private final Map<ItemFilter, Integer> recipes;
    private final int power;
    private final int speed;
    private final UpgradeType max;
    private final List<ItemStack> displayRecipes = new ArrayList<>();

    public PowerGenerator(SlimefunItemStack item, int power, int speed, UpgradeType max, Map<ItemFilter, Integer> recipes, ItemStack[] recipe) {
        super(item, recipe, 4);
        this.recipes = recipes;
        this.power = power;
        this.max = max;
        this.speed = speed;
        for (Map.Entry<ItemFilter, Integer> entry : this.recipes.entrySet()) {
            ItemStack stack = entry.getKey().getItem().clone();
            LoreUtils.addLore(stack, "", "&6Lasts: " + (entry.getValue() + 1) + " ticks");
            this.displayRecipes.add(stack);
        }
    }
    @Override
    public int[] getTransportSlots(@Nonnull ItemTransportFlow flow) {
        if (flow == ItemTransportFlow.INSERT) return new int[] {input};
        return new int[0];
    }

    @Override
    public int getGeneration(@Nonnull BlockMenu menu, @Nonnull Block b) {
        MutableInt remaining = this.progressing.computeIfAbsent(b.getLocation(), k -> new MutableInt(0));
        if (remaining.intValue() > 0) {
            int speed = UpgradeManager.getLevel(b);
            if (speed > remaining.intValue()) {
                remaining.setValue(0);
            } else {
                remaining.subtract(speed);
            }
            if (menu.hasViewer()) {
                menu.replaceExistingItem(status, getStatus(remaining.intValue()));
            }
            return this.power * UpgradeManager.getLevel(b);
        } else {
            @Nullable ItemStack input = menu.getItemInSlot(PowerGenerator.input);
            if (input != null && input.getAmount() >= this.speed) {
                Integer ticks = this.recipes.get(new ItemFilter(input, FilterType.IGNORE_AMOUNT));
                if (ticks != null) {
                    menu.consumeItem(PowerGenerator.input, this.speed);
                    remaining.setValue(ticks);
                    if (menu.hasViewer()) {
                        menu.replaceExistingItem(status, getStatus(ticks));
                    }
                    return this.power * UpgradeManager.getLevel(b);
                }
            }
        }
        if (menu.hasViewer()) {
            menu.replaceExistingItem(status, getStatus(0));
        }
        return 0;
    }

    @Override
    public void setupInv(@Nonnull BlockMenuPreset blockMenuPreset) {
        for (int i = 0 ; i < 27 ; i++) {
            if (i == input || i == status) i++;
            blockMenuPreset.addItem(i, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }
        blockMenuPreset.addItem(status, getStatus(0), ChestMenuUtils.getEmptyClickHandler());
    }
    
    private ItemStack getStatus(int ticks) {
        return new CustomItem(Material.FLINT_AND_STEEL, "&6Remaining Ticks: " + ticks);
    }

    @Override
    public final void onBreak(Player p, Block b, BlockMenu menu, PowerGrid grid) {
        super.onBreak(p, b, menu, grid);
        this.progressing.remove(b.getLocation());
    }

    @Nonnull
    @Override
    public List<ItemStack> getDisplayRecipes() {
        return this.displayRecipes;
    }

    @Nonnull
    @Override
    public UpgradeType getMaxLevel() {
        return this.max;
    }

    @Override
    public int getUpgradeSlot() {
        return 0;
    }

}
