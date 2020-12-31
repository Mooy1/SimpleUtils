package io.github.mooy1.slimegrid.implementation.generators;

import io.github.mooy1.infinitylib.filter.FilterType;
import io.github.mooy1.infinitylib.filter.ItemFilter;
import io.github.mooy1.infinitylib.items.LoreUtils;
import io.github.mooy1.infinitylib.presets.MenuPreset;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun4.utils.tags.SlimefunTag;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import org.apache.commons.lang.mutable.MutableInt;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratorMachine extends AbstractGridGenerator implements RecipeDisplayItem {

    public static final Map<ItemFilter, MutableInt> survivalRecipes = new HashMap<>();

    static {
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
    }
    
    protected static void addRecipe(Map<ItemFilter, MutableInt> recipes, Material material, int ticks) {
        recipes.put(new ItemFilter(new ItemStack(material, 1), FilterType.IGNORE_AMOUNT), new MutableInt(ticks));
    }

    protected static void addRecipe(Map<ItemFilter, MutableInt> recipes, SlimefunItemStack stack, int ticks) {
        recipes.put(new ItemFilter(stack, FilterType.IGNORE_AMOUNT), new MutableInt(ticks));
    }
    
    private static final int input = MenuPreset.slot2 + 9;

    private final Map<Location, MutableInt> progressing = new HashMap<>();
    private final Map<ItemFilter, MutableInt> recipes;
    private final int power;
    private final int speed;
    private final List<ItemStack> displayRecipes = new ArrayList<>();

    public GeneratorMachine(SlimefunItemStack item, ItemStack[] recipe, int power, int speed, Map<ItemFilter, MutableInt> recipes) {
        super(item, recipe, 4);
        for (MutableInt i : recipes.values()) {
            i.setValue(i.intValue() / speed);
        }
        this.recipes = recipes;
        this.power = power;
        this.speed = speed;
        for (Map.Entry<ItemFilter, MutableInt> entry : this.recipes.entrySet()) {
            LoreUtils.addLore(entry.getKey().getItem(), "", "Duration: " + entry.getValue());
            this.displayRecipes.add(entry.getKey().getItem());
            this.displayRecipes.add(null);
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
            remaining.decrement();
            return this.power;
        } else {
            @Nullable ItemStack input = menu.getItemInSlot(GeneratorMachine.input);
            if (input != null) {
                MutableInt ticks = this.recipes.get(new ItemFilter(input));
                if (ticks != null) {
                    menu.consumeItem(GeneratorMachine.input, 1);
                    remaining.setValue(ticks);
                    return this.power;
                }
            }
        }
        return 0;
    }

    @Override
    public void setupInv(@Nonnull BlockMenuPreset blockMenuPreset) {
        for (int i = 0 ; i < 36 ; i++) {
            if (i == input) i++;
            blockMenuPreset.addItem(i, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }
        for (int i : MenuPreset.slotChunk2) {
            blockMenuPreset.addItem(i + 9, MenuPreset.borderItemInput, ChestMenuUtils.getEmptyClickHandler());
        }
    }

    @Nonnull
    @Override
    public List<ItemStack> getDisplayRecipes() {
        return this.displayRecipes;
    }

}
