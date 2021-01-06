package io.github.mooy1.gridfoundation.implementation.grid;

import io.github.mooy1.gridfoundation.setup.Categories;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.apache.commons.lang.mutable.MutableInt;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

public final class GridViewer extends SimpleSlimefunItem<BlockUseHandler> {
    
    private static final int NEXT = 52;
    private static final int PREV = 46;
    
    public static final SlimefunItemStack ITEM = new SlimefunItemStack(
            "GRID_VIEWER",
            Material.CHISELED_QUARTZ_BLOCK,
            "&6Grid Viewer",
            "&7Shows stats and components of your power grid"
    );

    public GridViewer() {
        super(Categories.MAIN, ITEM, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                
        });
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
            if (i == NEXT || i == PREV) i++;
            menu.addItem(i, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }
        
        Grid grid = GridManager.get(p.getUniqueId());
        
        MutableInt pages = new MutableInt(0);
        
        MutableInt page = new MutableInt(0);

        refresh(p, menu, grid, page, pages);

        menu.addMenuClickHandler(PREV, (player, i, itemStack, clickAction) -> {
            if (page.intValue() > 0) {
                page.decrement();
                refresh(p, menu, grid, page, pages);
            }
            return false;
        });

        menu.addMenuClickHandler(NEXT, (player, i, itemStack, clickAction) -> {
            if (page.intValue() < pages.intValue() - 1) {
                page.increment();
                refresh(p, menu, grid, page, pages);
            }
            return false;
        });
        
        menu.open(p);
        
    }
    
    private void refresh(@Nonnull Player p, @Nonnull ChestMenu menu, @Nonnull Grid grid, MutableInt page, MutableInt pages) {
        List<Component> components = grid.getComponents();
        
        pages.setValue(1 + components.size() / 36);
        
        if (page.intValue() >= pages.intValue()) {
            page.setValue(0);
        }
        
        menu.replaceExistingItem(PREV, ChestMenuUtils.getPreviousButton(p, page.intValue() + 1, pages.intValue()));
        
        menu.replaceExistingItem(NEXT, ChestMenuUtils.getNextButton(p, page.intValue() + 1, pages.intValue()));
        
        int slot = 9;
        
        for (Component component : components) {
            
            menu.replaceExistingItem(slot, component.getViewerItem());
            
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
