package io.github.mooy1.simpleutils.implementation.blocks;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.logging.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import io.github.mooy1.infinitylib.common.Events;
import io.github.mooy1.infinitylib.common.Scheduler;
import io.github.mooy1.infinitylib.machines.MachineLayout;
import io.github.mooy1.infinitylib.machines.MenuBlock;
import io.github.mooy1.simpleutils.SimpleUtils;
import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.items.backpacks.SlimefunBackpack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.ItemStackSnapshot;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;

@ParametersAreNonnullByDefault
public final class Workbench extends MenuBlock implements Listener {

    private static final int[] INPUT_SLOTS = MachineLayout.CRAFTING_DEFAULT.inputSlots();
    private static final int OUTPUT_SLOT = 24;
    private static final ItemStack NO_OUTPUT = new CustomItemStack(Material.BARRIER, " ");

    private final NamespacedKey displayKey = SimpleUtils.createKey("display");
    private final BiFunction<ItemStack[], Player, ItemStack> craftItem;
    private final Map<UUID, BlockMenu> openMenus = new HashMap<>();
    private final FakeEnhancedCrafter fakeEnhancedCrafter;

    public Workbench(ItemGroup category, SlimefunItemStack itemStack, RecipeType recipeType, ItemStack[] r) {
        super(category, itemStack, recipeType, r);

        Events.registerListener(this);

        fakeEnhancedCrafter = new FakeEnhancedCrafter(category, itemStack);

        if (Slimefun.getMinecraftVersion().isBefore(MinecraftVersion.MINECRAFT_1_17)) {
            craftItem = Workbench::craftItemOld;
            Scheduler.run(() -> SimpleUtils.log(Level.WARNING,
                    "The SimpleWorkbench may not be able to craft vanilla recipes in mc 1.16"));
        } else {
            craftItem = Workbench::craftItem;
        }
    }

    @Override
    protected void setup(BlockMenuPreset blockMenuPreset) {
        blockMenuPreset.drawBackground(INPUT_BORDER, MachineLayout.CRAFTING_DEFAULT.inputBorder());
        blockMenuPreset.drawBackground(OUTPUT_BORDER, new int[] {
                14, 15, 16, 23, 25, 32, 33, 34
        });
        blockMenuPreset.drawBackground(new int[] {
                5, 6, 7, 8, 17, 26, 35, 41, 42, 43, 44
        });
    }

    @Override
    protected void onNewInstance(BlockMenu menu, Block b) {
        menu.addMenuOpeningHandler(p -> Workbench.this.openMenus.put(p.getUniqueId(), menu));
        menu.addMenuCloseHandler(p -> Workbench.this.openMenus.remove(p.getUniqueId()));
        menu.addPlayerInventoryClickHandler((p, slot, item, action) -> {
            if (action.isShiftClicked()) {
                refreshOutput(menu, p);
            }
            return true;
        });
        for (int i : INPUT_SLOTS) {
            menu.addMenuClickHandler(i, (p, slot, item, action) -> {
                refreshOutput(menu, p);
                return true;
            });
        }
        menu.addMenuClickHandler(OUTPUT_SLOT, (p, slot, item, action) -> {
            craft(p, menu, action.isShiftClicked());
            return false;
        });
    }

    @Override
    protected int[] getInputSlots(DirtyChestMenu menu, ItemStack in) {
        return new int[0];
    }

    @Override
    protected int[] getInputSlots() {
        return INPUT_SLOTS;
    }

    @Override
    protected int[] getOutputSlots() {
        return new int[0];
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

        ItemStackSnapshot[] snap = ItemStackSnapshot.wrapArray(input);
        ItemStack output = fakeEnhancedCrafter.craft(snap);

        if (output == null) {
            output = craftItem.apply(input, p);;
            if (output == null) {
                return;
            }
        } else {
            SlimefunItem item = SlimefunItem.getByItem(output);

            if (item != null) {
                if (!item.canUse(p, true)) {
                    return;
                }
                if (item instanceof SlimefunBackpack) {
                    p.sendMessage(ChatColor.RED + "Use the enhanced crafting table to upgrade backpacks!");
                    return;
                }
            }
        }

        // find smallest amount greater than 0
        int lowestAmount = 65;
        for (int i : amounts) {
            if (i > 0 && i < lowestAmount) {
                lowestAmount = i;
            }
        }

        if (lowestAmount == 65) {
            // this would only happen if there was an empty registered recipe
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
            }
            else {
                arr = new ItemStack[fullStacks + 1];
                arr[fullStacks] = new CustomItemStack(output, partialStack);
            }

            // fill with full stacks
            while (fullStacks-- != 0) {
                arr[fullStacks] = new CustomItemStack(output, output.getMaxStackSize());
            }

            // output and drop remaining
            Map<Integer, ItemStack> remaining = p.getInventory().addItem(arr);
            for (ItemStack stack : remaining.values()) {
                p.getWorld().dropItemNaturally(p.getLocation(), stack);
            }

            // refresh
            refreshOutput(menu, p);

        }
        else {

            // output and drop remaining
            Map<Integer, ItemStack> remaining = p.getInventory().addItem(output.clone());
            for (ItemStack stack : remaining.values()) {
                p.getWorld().dropItemNaturally(p.getLocation(), stack);
            }

            // refresh if a slot will run out
            if (lowestAmount == 1) {
                refreshOutput(menu, p);
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

    private void refreshOutput(@Nonnull BlockMenu menu, Player p) {
        Scheduler.run(() -> {
            ItemStack[] input = new ItemStack[9];
            for (int i = 0 ; i < INPUT_SLOTS.length ; i++) {
                input[i] = menu.getItemInSlot(INPUT_SLOTS[i]);
            }

            ItemStackSnapshot[] snap = ItemStackSnapshot.wrapArray(input);
            ItemStack output = fakeEnhancedCrafter.craft(snap);

            if (output == null) {
                output = craftItem.apply(input, p);
            }

            if (output == null) {
                menu.replaceExistingItem(OUTPUT_SLOT, NO_OUTPUT);
            }
            else {
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
            refreshOutput(menu, (Player) e.getWhoClicked());
        }
    }

    @Nullable
    private static ItemStack craftItem(ItemStack[] input, Player p) {
        ItemStack output = Bukkit.craftItem(input, p.getWorld(), p);
        return output.getType().isAir() ? null : output;
    }

    @Nullable
    private static ItemStack craftItemOld(ItemStack[] input, Player p) {
        return null; // TODO find new way
    }

}
