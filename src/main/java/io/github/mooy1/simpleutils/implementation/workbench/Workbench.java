package io.github.mooy1.simpleutils.implementation.workbench;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nonnull;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import io.github.mooy1.infinitylib.slimefun.abstracts.AbstractContainer;
import io.github.mooy1.infinitylib.slimefun.presets.MenuPreset;
import io.github.mooy1.simpleutils.SimpleUtils;
import io.github.thebusybiscuit.slimefun4.core.multiblocks.MultiBlockMachine;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.tags.SlimefunTag;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;

public final class Workbench extends AbstractContainer implements Listener {

    private static final int[] INPUT_SLOTS = MenuPreset.craftingInput;
    private static final int OUTPUT_SLOT = 24;
    private static final ItemStack NO_OUTPUT = new CustomItem(Material.BARRIER, " ");
    private static final Map<ShapedInput, ItemStack> RECIPES = new HashMap<>();

    static {
        SlimefunPlugin.getMinecraftRecipeService().subscribe(recipeSnapshot -> {
            for (ShapedRecipe recipe : recipeSnapshot.getRecipes(ShapedRecipe.class)) {

                Map<Character, ItemStack> choices = recipe.getIngredientMap();
                ItemStack[] arr = new ItemStack[9];
                String[] shape = recipe.getShape();

                List<Integer> plankSlots = null;
                boolean couldHaveAltPlankRecipes = true;

                for (int r = 0 ; r < shape.length ; r++) {
                    for (int c = 0 ; c < shape[r].length() ; c++) {
                        char ch = shape[r].charAt(c);
                        ItemStack stack = choices.get(ch);
                        if (stack != null) {
                            int slot = r * 3 + c;
                            arr[slot] = stack;

                            // add plank slots
                            if (couldHaveAltPlankRecipes && stack.getType() == Material.OAK_PLANKS) {
                                if (plankSlots != null) {
                                    plankSlots.add(slot);
                                } else if (recipe.getChoiceMap().get(ch).test(new ItemStack(Material.SPRUCE_PLANKS))) {
                                    plankSlots = new ArrayList<>();
                                    plankSlots.add(slot);
                                } else {
                                    couldHaveAltPlankRecipes = false;
                                }
                            }
                        }
                    }
                }

                // add alternate plank recipes
                if (couldHaveAltPlankRecipes && plankSlots != null) {
                    for (Material material : SlimefunTag.PLANKS.getValues()) {
                        if (material != Material.OAK_PLANKS) {
                            ItemStack plank = new ItemStack(material);
                            ItemStack[] newLogRecipe = new ItemStack[9];
                            for (int i = 0 ; i < 9 ; i++) {
                                if (plankSlots.contains(i)) {
                                    newLogRecipe[i] = plank;
                                } else {
                                    newLogRecipe[i] = arr[i];
                                }
                            }
                            RECIPES.put(new ShapedInput(newLogRecipe), recipe.getResult());
                        }
                    }
                }

                RECIPES.put(new ShapedInput(arr), recipe.getResult());
            }
            for (ShapelessRecipe recipe : recipeSnapshot.getRecipes(ShapelessRecipe.class)) {
                List<ItemStack> ingredientList = recipe.getIngredientList();
                ItemStack[] arr = new ItemStack[9];
                for (int i = 0 ; i < Math.min(9, ingredientList.size()) ; i++) {
                    arr[i] = ingredientList.get(i);
                }
                RECIPES.put(new ShapedInput(arr), recipe.getResult());
            }
        });
        SimpleUtils.inst().runSync(() -> {
            for (RecipeType type : Arrays.asList(RecipeType.ENHANCED_CRAFTING_TABLE, RecipeType.ARMOR_FORGE, RecipeType.MAGIC_WORKBENCH)) {
                List<ItemStack[]> list = ((MultiBlockMachine) type.getMachine()).getRecipes();
                for (int i = 0 ; i < list.size() ; i += 2) {
                    ItemStack[] recipe = list.get(i);
                    if (recipe.length != 9) {
                        continue;
                    }
                    for (int j = 0 ; j < 9 ; j++) {
                        // make sure recipe isn't empty
                        if (recipe[j] != null) {
                            ItemStack out = list.get(i + 1)[0];
                            SlimefunItem item = SlimefunItem.getByItem(out);
                            if (item != null && item.hasResearch()) {
                                RECIPES.put(new ShapedInput(recipe), new ResearchableItemStack(item.getResearch(), out));
                            } else {
                                RECIPES.put(new ShapedInput(recipe), out);
                            }
                            break;
                        }
                    }
                }
            }
        }, 100);
    }

    private final Map<UUID, BlockMenu> openMenus = new HashMap<>();

    public Workbench(Category category, SlimefunItemStack itemStack, RecipeType recipeType, ItemStack[] r) {
        super(category, itemStack, recipeType, r);
        SimpleUtils.inst().registerListener(this);
    }

    @Override
    protected void onBreak(@Nonnull BlockBreakEvent e, @Nonnull BlockMenu menu, @Nonnull Location l) {
        menu.dropItems(l, INPUT_SLOTS);
    }

    private static void craft(Player p, BlockMenu menu, boolean max) {

        ItemStack[] input = new ItemStack[9];
        int[] amounts = new int[9];

        for (int i = 0 ; i < INPUT_SLOTS.length ; i++) {
            ItemStack in = menu.getItemInSlot(INPUT_SLOTS[i]);
            if (in != null) {
                input[i] = in;
                amounts[i] = in.getAmount();
            }
        }

        ItemStack output = RECIPES.get(new ShapedInput(input));

        if (output == null || (output instanceof ResearchableItemStack && ((ResearchableItemStack) output).cantCraft(p))) {
            return;
        }

        // find smallest amount greater than 0
        int lowestAmount = 65;
        for (int i : amounts) {
            if (i > 0 && i < lowestAmount) {
                lowestAmount = i;
            }
        }

        if (lowestAmount == 65) {
            // this would only happen if there was a empty registered recipe
            return;
        }

        if (max) {

            // calc amounts
            int total = output.getAmount() * lowestAmount;
            int fullStacks = total / output.getMaxStackSize();
            int partialStack = total % output.getMaxStackSize();

            // create array of items
            ItemStack[] arr;
            if (partialStack == 0) {
                arr = new ItemStack[fullStacks];
            } else {
                arr = new ItemStack[fullStacks + 1];
                arr[fullStacks] = new CustomItem(output, partialStack);
            }

            // fill with full stacks
            while (fullStacks-- != 0) {
                arr[fullStacks] = new CustomItem(output, output.getMaxStackSize());
            }

            // output and drop remaining
            Map<Integer, ItemStack> remaining = p.getInventory().addItem(arr);
            for (ItemStack stack : remaining.values()) {
                p.getWorld().dropItemNaturally(p.getLocation(), stack);
            }

            // refresh
            refreshOutput(menu);

        } else {

            // output and drop remaining
            Map<Integer, ItemStack> remaining = p.getInventory().addItem(output.clone());
            for (ItemStack stack : remaining.values()) {
                p.getWorld().dropItemNaturally(p.getLocation(), stack);
            }

            // refresh if a slot will run out
            if (lowestAmount == 1) {
                refreshOutput(menu);
            }

            lowestAmount = 1;
        }

        // consume
        for (int i = 0 ; i < 9 ; i++) {
            if (amounts[i] != 0) {
                menu.consumeItem(INPUT_SLOTS[i], lowestAmount, true);
            }
        }
    }

    private static void refreshOutput(@Nonnull BlockMenu menu) {
        SimpleUtils.inst().runSync(() -> {
            ItemStack[] input = new ItemStack[9];
            for (int i = 0 ; i < INPUT_SLOTS.length ; i++) {
                input[i] = menu.getItemInSlot(INPUT_SLOTS[i]);
            }
            ItemStack output = RECIPES.get(new ShapedInput(input));
            if (output == null) {
                menu.replaceExistingItem(OUTPUT_SLOT, NO_OUTPUT);
            } else {
                menu.replaceExistingItem(OUTPUT_SLOT, output);
            }
        });
    }

    @Override
    protected void setupMenu(@Nonnull BlockMenuPreset blockMenuPreset) {
        blockMenuPreset.drawBackground(MenuPreset.borderItemInput, MenuPreset.craftingInputBorder);
        blockMenuPreset.drawBackground(MenuPreset.borderItemOutput, new int[] {14, 15, 16, 23, 25, 32, 33, 34});
        blockMenuPreset.drawBackground(new int[] {5, 6, 7, 8, 17, 26, 35, 41, 42, 43, 44});
    }

    @Override
    protected void onNewInstance(@Nonnull BlockMenu menu, @Nonnull Block b) {
        menu.addMenuOpeningHandler(p -> this.openMenus.put(p.getUniqueId(), menu));
        menu.addMenuCloseHandler(p -> this.openMenus.remove(p.getUniqueId()));
        menu.addPlayerInventoryClickHandler((p, slot, item, action) -> {
            if (action.isShiftClicked()) {
                refreshOutput(menu);
            }
            return true;
        });
        for (int i : INPUT_SLOTS) {
            menu.addMenuClickHandler(i, (p, slot, item, action) -> {
                refreshOutput(menu);
                return true;
            });
        }
        menu.addMenuClickHandler(OUTPUT_SLOT, (p, slot, item, action) -> {
            craft(p, menu, action.isShiftClicked());
            return false;
        });
        refreshOutput(menu);
    }

    @EventHandler
    public void onDrag(InventoryDragEvent e) {
        BlockMenu menu = this.openMenus.get(e.getWhoClicked().getUniqueId());
        if (menu != null) {
            refreshOutput(menu);
        }
    }

    @Nonnull
    @Override
    protected int[] getTransportSlots(@Nonnull DirtyChestMenu dirtyChestMenu, @Nonnull ItemTransportFlow itemTransportFlow, ItemStack itemStack) {
        return new int[0];
    }

}
