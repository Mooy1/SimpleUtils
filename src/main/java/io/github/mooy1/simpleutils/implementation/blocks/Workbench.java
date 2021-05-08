package io.github.mooy1.simpleutils.implementation.blocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import io.github.mooy1.infinitylib.items.StackUtils;
import io.github.mooy1.infinitylib.slimefun.abstracts.AbstractContainer;
import io.github.mooy1.infinitylib.slimefun.presets.MenuPreset;
import io.github.mooy1.simpleutils.SimpleUtils;
import io.github.thebusybiscuit.slimefun4.core.multiblocks.MultiBlockMachine;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.tags.SlimefunTag;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
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
                            RECIPES.put(new ShapedInput(newLogRecipe), new ItemStackWithRecipe(recipe));
                        }
                    }
                }

                RECIPES.put(new ShapedInput(arr), new ItemStackWithRecipe(recipe));
            }
            for (ShapelessRecipe recipe : recipeSnapshot.getRecipes(ShapelessRecipe.class)) {
                List<ItemStack> ingredientList = recipe.getIngredientList();
                ItemStack[] arr = new ItemStack[9];
                for (int i = 0 ; i < Math.min(9, ingredientList.size()) ; i++) {
                    arr[i] = ingredientList.get(i);
                }
                RECIPES.put(new ShapedInput(arr), new ItemStackWithRecipe(recipe));
            }
        });
        SimpleUtils.inst().runSync(() -> {
            RecipeType[] types = {RecipeType.ENHANCED_CRAFTING_TABLE, RecipeType.ARMOR_FORGE, RecipeType.MAGIC_WORKBENCH};
            for (RecipeType type : types) {
                List<ItemStack[]> list = ((MultiBlockMachine) type.getMachine()).getRecipes();
                for (int i = 0 ; i < list.size() ; i += 2) {
                    ItemStack[] recipe = list.get(i);
                    if (recipe.length != 9) {
                        continue;
                    }
                    for (int j = 0 ; j < 9 ; j++) {
                        // make sure recipe isn't empty
                        if (recipe[j] != null) {
                            RECIPES.put(new ShapedInput(recipe), list.get(i + 1)[0]);
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

        if (output == null) {
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

        if (output instanceof ItemStackWithRecipe) {
            CraftItemEvent e = new CraftItemEvent(
                    ((ItemStackWithRecipe) output).recipe, p.getOpenInventory(), 
                    InventoryType.SlotType.CONTAINER, OUTPUT_SLOT,
                    max ? ClickType.SHIFT_LEFT : ClickType.LEFT,
                    max ? InventoryAction.MOVE_TO_OTHER_INVENTORY : InventoryAction.PICKUP_ALL
            );
            Bukkit.getPluginManager().callEvent(e);
            if (e.isCancelled()) {
                return;
            }
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

    /**
     * A shaped, 3x3 recipe which detects the shape on its own and compares ids and shape
     */
    private static final class ShapedInput {

        private static final Map<Integer, Shape> SHAPES = new HashMap<>();

        private final String[] ids;
        private final Shape shape;
        private final int hashCode;

        private ShapedInput(@Nonnull ItemStack[] items) {

            // map of ids to digit for shape
            Map<String, Integer> map = new TreeMap<>();

            // array of ids for 3x3 shaped recipes
            String[] ids = new String[9];

            // hash
            int hashCode = 0;

            // the shape int
            int shapeInt = 0;

            // the shape
            Shape shape;

            // the current digit
            AtomicInteger digit = new AtomicInteger(1);

            // find all ids and the shape int
            for (int i = 0 ; i < 9 ; i++) {
                shapeInt *= 10;
                if (items[i] != null) {
                    String id = ids[i] = StackUtils.getIDorType(items[i]);
                    shapeInt += map.computeIfAbsent(id, k -> digit.getAndIncrement());
                }
            }

            // find shape
            shape = SHAPES.getOrDefault(shapeInt, Shape.SHAPED);

            if (shape == Shape.SHAPED) {
                for (String id : ids) {
                    if (id != null) {
                        hashCode += id.hashCode();
                    } else {
                        hashCode += 1;
                    }
                }
            } else {
                ids = new String[map.size()];
                hashCode = 0;
                if (shape == Shape.SHAPELESS) {
                    // sort tree map entries into array
                    int i = 0;
                    for (Map.Entry<String, Integer> entry : map.entrySet()) {
                        ids[i++] = entry.getKey();
                        hashCode += entry.getKey().hashCode();
                    }
                } else {
                    // just use the tree map entries
                    for (Map.Entry<String, Integer> entry : map.entrySet()) {
                        ids[entry.getValue() - 1] = entry.getKey();
                        hashCode += entry.getKey().hashCode();
                    }
                }
            }

            this.shape = shape;
            this.ids = ids;
            this.hashCode = hashCode + shape.hashCode();
        }

        @Override
        public final int hashCode() {
            return this.hashCode;
        }

        @Override
        public final boolean equals(Object obj) {
            if (!(obj instanceof ShapedInput)) {
                return false;
            }
            ShapedInput recipe = (ShapedInput) obj;
            if (recipe.shape != this.shape) {
                return false;
            }
            for (int i = 0 ; i < this.ids.length ; i++) {
                if (!Objects.equals(recipe.ids[i], this.ids[i])) {
                    return false;
                }
            }
            return true;
        }

    }

    private static final class ItemStackWithRecipe extends CustomItem {

        private final Recipe recipe;

        private ItemStackWithRecipe(Recipe recipe) {
            super(recipe.getResult());
            this.recipe = recipe;
        }

    }

    /**
     * Utility enum of 'shapes', for example a helmet could be:
     *
     * 111    000
     * 101 or 111
     * 000    101
     */
    @SuppressWarnings("unused")
    private enum Shape {

        SHAPELESS(120000000, 123000000),

        SINGLE(100000000, 10000000, 1000000, 100000, 10000, 1000, 100, 10, 1),

        TWO_UP(100100000, 10010000, 1001000, 100100, 10010, 1001),
        TWO_SIDE(110000000, 11000000, 110000, 11000, 110, 11),

        THREE_UP(100100100, 10010010, 1001001),
        THREE_SIDE(111000000, 111000, 111),
        THREE_SWORD(100100200, 10010020, 1001002),
        THREE_ARROW(100200300, 10020030, 1002003),
        THREE_SHOVEL(100200200, 10020020, 1002002),
        THREE_SOULBOUND(10020010, 100200100, 1002001),
        THREE_BUCKET(101010000, 101010),

        FOUR_SQUARE(110110000, 11011000, 110110, 11011),
        FOUR_HOE(110200200, 110020020, 11020020, 11002002),
        FOUR_BOOTS(101101000, 101101),

        FIVE_AXE(110120020, 11012002, 110210200, 11021020),
        FIVE_HELMET(111101000, 111101),

        SIX_TABLE(111111000, 111111),
        SIX_DOOR(110110110, 11011011),
        SIX_STAIRS(1011111, 100110111),
        SIX_BOW(120102120, 12102012),
        SIX_FENCE(121121000, 121121),

        SHAPED();

        Shape(int... shapes) {
            for (int i : shapes) {
                ShapedInput.SHAPES.put(i, this);
            }
        }

    }

}
