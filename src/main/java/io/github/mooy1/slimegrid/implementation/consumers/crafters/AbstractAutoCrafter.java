package io.github.mooy1.slimegrid.implementation.consumers.crafters;

import io.github.mooy1.infinitylib.filter.FilterType;
import io.github.mooy1.infinitylib.filter.ItemFilter;
import io.github.mooy1.infinitylib.filter.MultiFilter;
import io.github.mooy1.infinitylib.items.StackUtils;
import io.github.mooy1.infinitylib.player.MessageUtils;
import io.github.mooy1.infinitylib.presets.MenuPreset;
import io.github.mooy1.slimegrid.implementation.consumers.AbstractGridConsumer;
import io.github.mooy1.slimegrid.implementation.grid.PowerGrid;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import me.mrCookieSlime.Slimefun.cscorelib2.collections.Pair;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractAutoCrafter extends AbstractGridConsumer {
    
    private static final int[] inputSlots = {
            10, 11, 12,
            19, 20, 21,
            28, 29, 30
    };
    private static final int[] inputBorder = {
            0, 1, 2, 3, 4,
            9, 13,
            18, 22,
            27, 31,
            36, 37, 38, 39, 40
    };
    private static final int[] background = {
            5, 6, 7, 14, 8,
            41, 42, 43, 44, 32
    };
    private static final int keySlot = 23;
    private static final int[] outputSlots = {25};
    private static final int[] outputBorder = {24, 26, 15, 16, 17, 33, 34, 35};
    protected final Map<Location, Pair<MultiFilter, ItemStack>> cache = new HashMap<>();

    public AbstractAutoCrafter(int gp, SlimefunItemStack item, ItemStack[] recipe) {
        super(item, recipe, 7, gp);
    }

    @Override
    public void onBreak(Player p, Block b, Location l, BlockMenu menu, PowerGrid grid) {
        menu.dropItems(l, inputSlots);
        menu.dropItems(l, outputSlots);
        this.cache.remove(b.getLocation());
    }

    @Override
    public void onNewInstance(@Nonnull BlockMenu menu, @Nonnull Block b) {
        super.onNewInstance(menu, b);

        menu.addMenuClickHandler(keySlot, new ChestMenu.AdvancedMenuClickHandler() {
            @Override
            public boolean onClick(InventoryClickEvent inventoryClickEvent, Player player, int i, ItemStack itemStack, ClickAction clickAction) {
                if (inventoryClickEvent.getAction() != InventoryAction.HOTBAR_SWAP) {
                    ItemStack item = player.getItemOnCursor();
                    if (updateCache(b, item)) {
                        menu.replaceExistingItem(keySlot, StackUtils.getUnique(new CustomItem(item, 1)));
                    } else {
                        menu.replaceExistingItem(keySlot, MenuPreset.emptyKey);
                    }
                }
                return false;
            }

            @Override
            public boolean onClick(Player player, int i, ItemStack itemStack, ClickAction clickAction) {
                return false;
            }
        });

        // add to cache
        ItemStack current = menu.getItemInSlot(keySlot);
        if (current == null || !updateCache(b, current)) {
            menu.replaceExistingItem(keySlot, MenuPreset.emptyKey, false);
        }
    }
    
    public abstract boolean updateCache(@Nonnull Block b, @Nonnull ItemStack item);

    @Override
    public void setupInv(@Nonnull BlockMenuPreset blockMenuPreset) {
        for (int slot : inputBorder) {
            blockMenuPreset.addItem(slot, MenuPreset.borderItemInput, ChestMenuUtils.getEmptyClickHandler());
        }
        for (int slot : outputBorder) {
            blockMenuPreset.addItem(slot, MenuPreset.borderItemOutput, ChestMenuUtils.getEmptyClickHandler());
        }
        for (int slot : background) {
            blockMenuPreset.addItem(slot, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }
    }

    @Nonnull
    @Override
    public int[] getTransportSlots(@Nonnull DirtyChestMenu menu, @Nonnull ItemTransportFlow flow, @Nonnull ItemStack item) {
        if (flow == ItemTransportFlow.WITHDRAW) {
            return outputSlots;
        }
        @Nullable Pair<MultiFilter, ItemStack> pair = this.cache.get(((BlockMenu) menu).getLocation());
        if (pair != null) {
            int i = pair.getFirstValue().indexOf(new ItemFilter(item), FilterType.IGNORE_AMOUNT);
            if (i > -1) return new int[] {inputSlots[i]};
        }
        return new int[0];
    }

    @Override
    public boolean process(@Nonnull BlockMenu menu, @Nonnull Block b) {
        
        Pair<MultiFilter, ItemStack> pair = this.cache.get(b.getLocation());
        
        if (pair == null) return false;

        MultiFilter input = MultiFilter.fromMenu(menu, inputSlots);

        MessageUtils.broadcast(input.hashCode() + " ");
        
        if (input.hashCode() == 9 || !pair.getFirstValue().matches(input, FilterType.MIN_AMOUNT)) return false;
        
        if (!menu.fits(pair.getSecondValue(), outputSlots)) return false;

        menu.pushItem(pair.getSecondValue().clone(), outputSlots);

        for (int i = 0 ; i < pair.getFirstValue().size() ; i++) {
            int amount = pair.getFirstValue().getAmounts()[i];
            if (amount > 0) {
                menu.consumeItem(inputSlots[i], amount);
            }
        }

        return true;
        
    }
    
}
