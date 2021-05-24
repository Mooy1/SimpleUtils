package io.github.mooy1.simpleutils.implementation.blocks.workbench;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import io.github.mooy1.infinitylib.presets.MenuPreset;
import io.github.mooy1.infinitylib.slimefun.AbstractContainer;
import io.github.mooy1.simpleutils.SimpleUtils;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;

public final class Workbench extends SlimefunItem implements Listener {

    private static final int[] INPUT_SLOTS = MenuPreset.CRAFTING_INPUT;
    private static final int OUTPUT_SLOT = 24;
    private static final ItemStack NO_OUTPUT = new CustomItem(Material.BARRIER, " ");

    private final NamespacedKey displayKey = SimpleUtils.inst().getKey("display");
    private final Map<UUID, BlockMenu> openMenus = new HashMap<>();
    private final RecipeRegistry recipes = new RecipeRegistry();

    public Workbench(Category category, SlimefunItemStack itemStack, RecipeType recipeType, ItemStack[] r) {
        super(category, itemStack, recipeType, r);

        SimpleUtils.inst().registerListener(this);

        new BlockMenuPreset(getId(), getItemName()) {

            @Override
            public void init() {
                drawBackground(MenuPreset.INPUT_ITEM, MenuPreset.CRAFTING_INPUT_BORDER);
                drawBackground(MenuPreset.OUTPUT_ITEM, new int[] {14, 15, 16, 23, 25, 32, 33, 34});
                drawBackground(new int[] {5, 6, 7, 8, 17, 26, 35, 41, 42, 43, 44});
            }

            @Override
            public void newInstance(@Nonnull BlockMenu menu, @Nonnull Block b) {
                menu.addMenuOpeningHandler(p -> Workbench.this.openMenus.put(p.getUniqueId(), menu));
                menu.addMenuCloseHandler(p -> Workbench.this.openMenus.remove(p.getUniqueId()));
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

            @Override
            public boolean canOpen(@Nonnull Block b, @Nonnull Player p) {
                return AbstractContainer.canOpen(b, p);
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
                return new int[0];
            }
        };

        addItemHandler(new BlockBreakHandler(false, false) {
            @Override
            public void onPlayerBreak(@Nonnull BlockBreakEvent e, @Nonnull ItemStack item, @Nonnull List<ItemStack> drops) {
                BlockStorage.getInventory(e.getBlock()).dropItems(e.getBlock().getLocation(), INPUT_SLOTS);
            }
        });

    }
    private void craft(Player p, BlockMenu menu, boolean max) {

        ItemStack[] input = new ItemStack[9];
        int[] amounts = new int[9];

        for (int i = 0 ; i < INPUT_SLOTS.length ; i++) {
            ItemStack in = menu.getItemInSlot(INPUT_SLOTS[i]);
            if (in != null) {
                input[i] = in;
                amounts[i] = in.getAmount();
            }
        }

        ItemStack output = this.recipes.get(new ShapedInput(input));

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

    private void refreshOutput(@Nonnull BlockMenu menu) {
        SimpleUtils.inst().runSync(() -> {
            ItemStack[] input = new ItemStack[9];
            for (int i = 0 ; i < INPUT_SLOTS.length ; i++) {
                input[i] = menu.getItemInSlot(INPUT_SLOTS[i]);
            }
            ItemStack output = this.recipes.get(new ShapedInput(input));
            if (output == null) {
                menu.replaceExistingItem(OUTPUT_SLOT, NO_OUTPUT);
            } else {
                output = output.clone();
                ItemMeta meta = output.getItemMeta();
                meta.getPersistentDataContainer().set(this.displayKey, PersistentDataType.BYTE, (byte) 0);
                output.setItemMeta(meta);
                menu.replaceExistingItem(OUTPUT_SLOT, output);
            }
        });
    }

    @EventHandler
    public void onDrag(@Nonnull InventoryDragEvent e) {
        BlockMenu menu = this.openMenus.get(e.getWhoClicked().getUniqueId());
        if (menu != null) {
            refreshOutput(menu);
        }
    }

}
