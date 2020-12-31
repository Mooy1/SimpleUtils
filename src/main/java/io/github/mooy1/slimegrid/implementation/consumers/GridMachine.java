package io.github.mooy1.slimegrid.implementation.consumers;

import io.github.mooy1.infinitylib.filter.FilterType;
import io.github.mooy1.infinitylib.filter.ItemFilter;
import io.github.mooy1.infinitylib.presets.MenuPreset;
import io.github.mooy1.slimegrid.implementation.grid.PowerGrid;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.core.multiblocks.MultiBlockMachine;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import me.mrCookieSlime.Slimefun.cscorelib2.collections.Pair;
import org.apache.commons.lang.mutable.MutableInt;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GridMachine extends AbstractGridConsumer implements RecipeDisplayItem {

    public static final int P_I = 8;
    public static final int P_II = 32;
    public static final int P_III = 128;

    public static final int F_I = 4;
    public static final int F_II = 16;
    public static final int F_III = 64;
    public static final int F_IV = 4;
    public static final int F_V = 16;
    public static final int F_VI = 64;
    public static final int F_VII = 4;

    public static final int C_I = 6;
    public static final int C_II = 36;
    
    public static final int D_I = 6;
    public static final int D_II = 36;

    public static final Pair<List<ItemStack>, Map<ItemFilter, Pair<ItemStack, Integer>>> furnaceRecipes = makeFurnaceRecipes();
    public static final Pair<List<ItemStack>, Map<ItemFilter, Pair<ItemStack, Integer>>> pulverizerRecipes = makeRecipes(false, RecipeType.ORE_CRUSHER, RecipeType.GRIND_STONE);
    public static final Pair<List<ItemStack>, Map<ItemFilter, Pair<ItemStack, Integer>>> compressorRecipes = makeRecipes(false, RecipeType.COMPRESSOR);
    public static final Pair<List<ItemStack>, Map<ItemFilter, Pair<ItemStack, Integer>>> decompressorRecipes = makeRecipes(true, RecipeType.COMPRESSOR);

    private static final int input = MenuPreset.slot1;
    private static final int output = MenuPreset.slot3;
    
    private final List<ItemStack> displayRecipes;
    private final Map<ItemFilter, Pair<ItemStack, Integer>> recipes;
    private final int speed;

    public GridMachine(SlimefunItemStack item, Pair<List<ItemStack>, Map<ItemFilter, Pair<ItemStack, Integer>>> recipes, int consumption, int speed, ItemStack[] recipe) {
        super(item, recipe, MenuPreset.slot2, consumption);
        this.speed = speed;
        this.recipes = recipes.getSecondValue();
        this.displayRecipes = recipes.getFirstValue();
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onBreak(Player p, Block b, Location l, BlockMenu menu, PowerGrid grid) {
        menu.dropItems(l, input, output);
    }

    @Override
    public boolean process(@Nonnull BlockMenu menu, @Nonnull Block b) {
        MutableInt progress = progressing.computeIfAbsent(b.getLocation(), k -> new MutableInt(this.ticks));
        if (progress.intValue() < this.ticks) {
            progress.increment();
            return true;
        } else {
            @Nullable ItemStack input = menu.getItemInSlot(GridMachine.input);
            if (input != null) {
                Pair<ItemStack, Integer> pair = this.recipes.get(new ItemFilter(input));
                if (pair != null && input.getAmount() >= pair.getSecondValue() && menu.fits(pair.getFirstValue(), output)) {
                    menu.pushItem(pair.getFirstValue().clone(), output);
                    menu.consumeItem(GridMachine.input, pair.getSecondValue());
                    progress.setValue(1);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void setupInv(@Nonnull BlockMenuPreset blockMenuPreset) {
        for (int i : MenuPreset.slotChunk1) {
            blockMenuPreset.addItem(i, MenuPreset.borderItemInput, ChestMenuUtils.getEmptyClickHandler());
        }
        for (int i : MenuPreset.slotChunk2) {
            blockMenuPreset.addItem(i, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }
        for (int i : MenuPreset.slotChunk3) {
            blockMenuPreset.addItem(i, MenuPreset.borderItemInput, ChestMenuUtils.getEmptyClickHandler());
        }
    }

    @Nonnull
    @Override
    public List<ItemStack> getDisplayRecipes() {
        return this.displayRecipes;
    }

    private static Pair<List<ItemStack>, Map<ItemFilter, Pair<ItemStack, Integer>>> makeRecipes(boolean inverse, RecipeType... types) {
        Map<ItemFilter, Pair<ItemStack, Integer>> map = new HashMap<>();
        List<ItemStack> list = new ArrayList<>();
        
        for (RecipeType type : types) {
            List<ItemStack> recipes = ((MultiBlockMachine) type.getMachine()).getDisplayRecipes();
            list.addAll(recipes);
            for (int i = 0 ; i < recipes.size() ; i+=2) {
                if (inverse) {
                    ItemStack input = recipes.get(i + 1);
                    map.put(new ItemFilter(input, FilterType.MIN_AMOUNT), new Pair<>(recipes.get(i), input.getAmount()));
                } else {
                    ItemStack input = recipes.get(i);
                    map.put(new ItemFilter(input, FilterType.MIN_AMOUNT), new Pair<>(recipes.get(i + 1), input.getAmount()));
                }
            }
        }
        
        return new Pair<>(list, map);
    }
    
    private static Pair<List<ItemStack>, Map<ItemFilter, Pair<ItemStack, Integer>>> makeFurnaceRecipes() {
        Map<ItemFilter, Pair<ItemStack, Integer>> map = new HashMap<>();
        List<ItemStack> list = new ArrayList<>();
        
        SlimefunPlugin.getMinecraftRecipeService().subscribe(snapshot -> {
            for (FurnaceRecipe furnaceRecipe : snapshot.getRecipes(FurnaceRecipe.class)) {
                RecipeChoice choice = furnaceRecipe.getInputChoice();
                if (choice instanceof RecipeChoice.MaterialChoice) {
                    list.add(furnaceRecipe.getInput());
                    list.add(furnaceRecipe.getResult());
                    for (Material input : ((RecipeChoice.MaterialChoice) choice).getChoices()) {
                        map.put(new ItemFilter(new ItemStack(input, 1), FilterType.MIN_AMOUNT), new Pair<>(furnaceRecipe.getResult(), 1));
                    }
                }
            }
        });
        
        return new Pair<>(list, map);
    }

    @Override
    public int[] getTransportSlots(@Nonnull ItemTransportFlow flow) {
        if (flow == ItemTransportFlow.WITHDRAW) return new int[] {output};
        if (flow == ItemTransportFlow.INSERT) return new int[] {input};
        return new int[0];
    }

}
