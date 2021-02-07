package io.github.mooy1.gridexpansion.implementation.powergrid;

import io.github.mooy1.gridexpansion.setup.Categories;
import io.github.mooy1.infinitylib.player.LeaveListener;
import io.github.mooy1.infinitylib.presets.LorePreset;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import org.apache.commons.lang.mutable.MutableInt;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class PowerGridViewer extends SimpleSlimefunItem<BlockUseHandler> {
    
    private static final int NEXT = 52;
    private static final int PREV = 46;
    private static final int REFRESH = 49;
    private static final int STATUS = 4;
    
    private final Map<UUID, Long> cooldowns = new HashMap<>();
    
    public static final SlimefunItemStack ITEM = new SlimefunItemStack(
            "GRID_VIEWER",
            Material.CHISELED_QUARTZ_BLOCK,
            "&6Grid Viewer",
            "&7Shows stats and components of your power grid"
    );

    public PowerGridViewer() {
        super(Categories.MAIN, ITEM, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                
        });
        LeaveListener.add(this.cooldowns);
    }
    
    private void open(@Nonnull Player p) {
        ChestMenu menu = new ChestMenu("Grid Overview");

        menu.setEmptySlotsClickable(false);
        menu.setPlayerInventoryClickable(false);

        for (int i = 0 ; i < 9 ; i++) {
            menu.addItem(i, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }
        for (int i = 9 ; i < 45 ; i++) {
            menu.addMenuClickHandler(i, ChestMenuUtils.getEmptyClickHandler());
        }
        for (int i = 45 ; i < 53 ; i++) {
            if (i == NEXT || i == PREV || i == REFRESH) i++;
            menu.addItem(i, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }
        
        PowerGrid grid = PowerGrid.get(p.getUniqueId().toString());
        
        MutableInt pages = new MutableInt(0);
        
        MutableInt page = new MutableInt(0);
        
        menu.addItem(REFRESH, new CustomItem(Material.LIME_STAINED_GLASS_PANE, "&aRefresh", "", "&7 > Click to refresh"), (player, i, itemStack, clickAction) -> {
            if (checkCd(player)) {
                refresh(player, menu, grid, page, pages);
            }
            return false;
        });
        
        menu.addMenuClickHandler(PREV, (player, i, itemStack, clickAction) -> {
            if (page.intValue() > 0 && checkCd(player)) {
                page.decrement();
                refresh(player, menu, grid, page, pages);
            }
            return false;
        });

        menu.addMenuClickHandler(NEXT, (player, i, itemStack, clickAction) -> {
            if (page.intValue() < pages.intValue() - 1 && checkCd(player)) {
                page.increment();
                refresh(player, menu, grid, page, pages);
            }
            return false;
        });

        refresh(p, menu, grid, page, pages);
        
        menu.open(p);
        
    }
    
    private boolean checkCd(Player p) {
        if (System.currentTimeMillis() - this.cooldowns.getOrDefault(p.getUniqueId(), 0L) < 1000) {
            return false;
        }
        this.cooldowns.put(p.getUniqueId(), System.currentTimeMillis());
        return true;
    }
    
    private static void refresh(@Nonnull Player p, @Nonnull ChestMenu menu, @Nonnull PowerGrid grid, MutableInt page, MutableInt pages) {
        List<GridComponent> components = grid.getComponents();
        
        pages.setValue(1 + components.size() / 36);
        
        if (page.intValue() >= pages.intValue()) {
            page.setValue(0);
        }
        
        menu.replaceExistingItem(STATUS, new CustomItem(
                grid.maxed ? Material.RED_STAINED_GLASS_PANE : grid.usage == grid.max ? Material.YELLOW_STAINED_GLASS_PANE : Material.LIME_STAINED_GLASS_PANE,
                "&6Grid: &e" + grid.usage + " / " + grid.max,
                "&7Usage: " + (grid.max == 0 ? '0' : LorePreset.format(100 * (double) grid.usage / grid.max)) + "%",
                "",
                grid.maxed ? "&cGrid Overloaded!" : ""
        ));
        
        menu.replaceExistingItem(PREV, ChestMenuUtils.getPreviousButton(p, page.intValue() + 1, pages.intValue()));
        
        menu.replaceExistingItem(NEXT, ChestMenuUtils.getNextButton(p, page.intValue() + 1, pages.intValue()));
        
        int slot = 9;
        
        for (int i = page.intValue() * 36 ; i < components.size() ; i++) {
            
            menu.replaceExistingItem(slot, components.get(i).getViewerItem());
            
            slot++;
            
            if (slot == 45) {
                break;
            }
        }
        
        while (slot < 45) {
            menu.replaceExistingItem(slot, null);
            slot++;
        }
    }

    @Nonnull
    @Override
    public BlockUseHandler getItemHandler() {
        return e -> open(e.getPlayer());
    }

}
