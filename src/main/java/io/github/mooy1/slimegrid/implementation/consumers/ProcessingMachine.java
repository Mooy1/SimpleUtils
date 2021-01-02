package io.github.mooy1.slimegrid.implementation.consumers;

import io.github.mooy1.infinitylib.filter.FilterType;
import io.github.mooy1.infinitylib.filter.ItemFilter;
import io.github.mooy1.infinitylib.presets.MenuPreset;
import io.github.mooy1.slimegrid.implementation.grid.PowerGrid;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import me.mrCookieSlime.Slimefun.cscorelib2.collections.Pair;
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

public final class ProcessingMachine extends AbstractGridConsumer implements RecipeDisplayItem {

    public static final int P_I = 8;
    public static final int P_II = 32;
    public static final int P_III = 128;

    public static final int F_I = 4;
    public static final int F_II = 8;
    public static final int F_III = 16;
    public static final int F_IV = 32;
    public static final int F_V = 64;
    public static final int F_VI = 128;
    public static final int F_VII = 256;

    public static final int C_I = 6;
    public static final int C_II = 36;
    
    public static final int D_I = 6;
    public static final int D_II = 36;

    public static final Pair<List<ItemStack>, Map<ItemFilter, Pair<ItemStack, Integer>>> furnaceRecipes = makeFurnaceRecipes();
    public static final Pair<List<ItemStack>, Map<ItemFilter, Pair<ItemStack, Integer>>> pulverizerRecipes = makeRecipes( false,
            new ItemStack(Material.COBBLESTONE), new ItemStack(Material.GRAVEL), new ItemStack(Material.GRAVEL), new ItemStack(Material.SAND),
            new ItemStack(Material.IRON_INGOT), SlimefunItems.IRON_DUST, new ItemStack(Material.IRON_ORE), new SlimefunItemStack(SlimefunItems.IRON_DUST, 2),
            new ItemStack(Material.GOLD_INGOT), SlimefunItems.GOLD_DUST, new ItemStack(Material.GOLD_ORE), new SlimefunItemStack(SlimefunItems.GOLD_DUST, 2),
            SlimefunItems.GOLD_4K, SlimefunItems.GOLD_DUST, new ItemStack(Material.NETHER_GOLD_ORE), SlimefunItems.GOLD_DUST,
            new ItemStack(Material.COAL_ORE), new ItemStack(Material.COAL, 4), new ItemStack(Material.NETHER_QUARTZ_ORE), new ItemStack(Material.QUARTZ, 4),
            new ItemStack(Material.GLOWSTONE), new ItemStack(Material.GLOWSTONE_DUST, 4), 
            new ItemStack(Material.DIAMOND_ORE), new ItemStack(Material.DIAMOND, 2), new ItemStack(Material.EMERALD_ORE), new ItemStack(Material.EMERALD, 2),
            new ItemStack(Material.LAPIS_ORE), new ItemStack(Material.LAPIS_LAZULI, 8), new ItemStack(Material.REDSTONE_ORE), new ItemStack(Material.REDSTONE, 8),
            new ItemStack(Material.BLAZE_ROD), new ItemStack(Material.BLAZE_POWDER, 5), new ItemStack(Material.BONE), new ItemStack(Material.BONE_MEAL, 5)
            );
    public static final Pair<List<ItemStack>, Map<ItemFilter, Pair<ItemStack, Integer>>> compressorRecipes = makeRecipes( false,
            new ItemStack(Material.COAL, 9), new ItemStack(Material.COAL_BLOCK), new ItemStack(Material.DIAMOND, 9), new ItemStack(Material.DIAMOND_BLOCK),
            new ItemStack(Material.EMERALD, 9), new ItemStack(Material.EMERALD_BLOCK), new ItemStack(Material.REDSTONE, 9), new ItemStack(Material.REDSTONE_BLOCK),
            new ItemStack(Material.IRON_INGOT, 9), new ItemStack(Material.IRON_BLOCK), new ItemStack(Material.NETHERITE_INGOT, 9), new ItemStack(Material.NETHERITE_BLOCK),
            new ItemStack(Material.GOLD_INGOT, 9), new ItemStack(Material.GOLD_BLOCK), new ItemStack(Material.LAPIS_LAZULI, 9), new ItemStack(Material.LAPIS_BLOCK),
            new ItemStack(Material.QUARTZ, 4), new ItemStack(Material.QUARTZ_BLOCK), new ItemStack(Material.SAND, 4), new ItemStack(Material.SANDSTONE)
    );
    public static final Pair<List<ItemStack>, Map<ItemFilter, Pair<ItemStack, Integer>>> decompressorRecipes = makeRecipes(true,
            new ItemStack(Material.COAL, 9), new ItemStack(Material.COAL_BLOCK), new ItemStack(Material.DIAMOND, 9), new ItemStack(Material.DIAMOND_BLOCK),
            new ItemStack(Material.EMERALD, 9), new ItemStack(Material.EMERALD_BLOCK), new ItemStack(Material.REDSTONE, 9), new ItemStack(Material.REDSTONE_BLOCK),
            new ItemStack(Material.IRON_INGOT, 9), new ItemStack(Material.IRON_BLOCK), new ItemStack(Material.NETHERITE_INGOT, 9), new ItemStack(Material.NETHERITE_BLOCK),
            new ItemStack(Material.GOLD_INGOT, 9), new ItemStack(Material.GOLD_BLOCK), new ItemStack(Material.LAPIS_LAZULI, 9), new ItemStack(Material.LAPIS_BLOCK),
            new ItemStack(Material.QUARTZ, 4), new ItemStack(Material.QUARTZ_BLOCK), new ItemStack(Material.SAND, 4), new ItemStack(Material.SANDSTONE)
    );

    private static final int inputSlot = MenuPreset.slot1;
    private static final int outputSlot = MenuPreset.slot3;
    
    private final List<ItemStack> displayRecipes;
    private final Map<ItemFilter, Pair<ItemStack, Integer>> recipes;
    private final int speed;

    public ProcessingMachine(SlimefunItemStack item, Pair<List<ItemStack>, Map<ItemFilter, Pair<ItemStack, Integer>>> recipes, int consumption, int speed, ItemStack[] recipe) {
        super(item, recipe, MenuPreset.slot2, consumption);
        this.speed = speed;
        this.recipes = recipes.getSecondValue();
        this.displayRecipes = recipes.getFirstValue();
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onBreak(Player p, Block b, Location l, BlockMenu menu, PowerGrid grid) {
        menu.dropItems(l, inputSlot, outputSlot);
    }

    @Override
    public boolean process(@Nonnull BlockMenu menu, @Nonnull Block b) {
            @Nullable ItemStack input = menu.getItemInSlot(inputSlot);
            if (input != null) {
                Pair<ItemStack, Integer> pair = this.recipes.get(new ItemFilter(input, FilterType.IGNORE_AMOUNT));
                if (pair != null) {
                    int amount = Math.min(input.getAmount() / pair.getSecondValue(), this.speed);
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

    private static Pair<List<ItemStack>, Map<ItemFilter, Pair<ItemStack, Integer>>> makeRecipes(boolean inverse, ItemStack... items) {
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
    
    private static Pair<List<ItemStack>, Map<ItemFilter, Pair<ItemStack, Integer>>> makeFurnaceRecipes() {
        Map<ItemFilter, Pair<ItemStack, Integer>> map = new HashMap<>();
        List<ItemStack> list = new ArrayList<>();
        
        SlimefunPlugin.getMinecraftRecipeService().subscribe(snapshot -> {
            for (FurnaceRecipe furnaceRecipe : snapshot.getRecipes(FurnaceRecipe.class)) {
                RecipeChoice choice = furnaceRecipe.getInputChoice();
                if (choice instanceof RecipeChoice.MaterialChoice) {
                    for (Material input : ((RecipeChoice.MaterialChoice) choice).getChoices()) {
                        list.add(new ItemStack(input));
                        list.add(furnaceRecipe.getResult());
                        map.put(new ItemFilter(new ItemStack(input, 1), FilterType.IGNORE_AMOUNT), new Pair<>(furnaceRecipe.getResult(), 1));
                    }
                }
            }
        });
        
        return new Pair<>(list, map);
    }

}
