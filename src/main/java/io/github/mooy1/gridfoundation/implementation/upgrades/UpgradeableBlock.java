package io.github.mooy1.gridfoundation.implementation.upgrades;

import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import me.mrCookieSlime.Slimefun.cscorelib2.protection.ProtectableAction;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

/** // TODO extend infinity lib tickingcontainer
 * Interface for identifying Upgradeable Blocks
 * slimefun item implementing must call register on itself
 * adds a ticker, menu, place handler, and break handler
 */
public interface UpgradeableBlock {

    @Nonnull
    UpgradeType getMaxLevel();

    /**
     * Return -1 for none
     */
    int getUpgradeSlot();

    void getStats(@Nonnull List<String> stats, int tier);

    void tick(@Nonnull BlockMenu menu, @Nonnull Block b, int tier);
    
    void setupMenu(@Nonnull BlockMenuPreset preset);

    @Nonnull
    int[] getTransportSlots(@Nonnull ItemTransportFlow flow);

    @Nonnull
    default int[] getTransportSlots(@Nonnull DirtyChestMenu menu, @Nonnull  ItemTransportFlow flow, ItemStack item) {
        return getTransportSlots(flow);
    }
    
    default boolean canOpen(@Nonnull Block b, @Nonnull Player p) {
        return true;
    }
    
    default void onNewInstance(@Nonnull BlockMenu menu, @Nonnull Block b) {
        
    }
    
    default void onBreak(@Nonnull BlockMenu menu) {
        
    }
    
    default void onPlace() {
        
    }
    
    /**
     * Use this to register your UpgradeableBlock
     */
    default void register(@Nonnull SlimefunItem item) {
        UpgradeManager.addDefaultMeta((SlimefunItemStack) item.getItem());
        item.addItemHandler((BlockBreakHandler) (e, item1, fortune, drops) -> {
            UpgradeManager.onBreak(e, item.getItem());
            BlockMenu menu = BlockStorage.getInventory(e.getBlock());
            if (menu != null) {
                onBreak(menu);
            }
            return true;
        });
        item.addItemHandler(new BlockPlaceHandler(false) {
            @Override
            public void onPlayerPlace(BlockPlaceEvent e) {
                UpgradeManager.onPlace(e.getBlockPlaced().getLocation(), e.getItemInHand(), UpgradeableBlock.this);
                onPlace();
            }
        });
        item.addItemHandler(new BlockTicker() {
            @Override
            public boolean isSynchronized() {
                return true;
            }

            @Override
            public void tick(Block b, SlimefunItem item, Config data) {
                BlockMenu menu = BlockStorage.getInventory(b);
                if (menu != null) {
                    UpgradeableBlock.this.tick(menu, b, UpgradeManager.getUpgrade(b.getLocation()).tier());
                }
            }
        });
        new BlockMenuPreset(item.getId(), item.getItemName()) {
            @Override
            public void init() {
                setupMenu(this);
            }
            @Override
            public boolean canOpen(@Nonnull Block b, @Nonnull Player p) {
                return p.hasPermission("slimefun.inventory.bypass")
                        || (canOpen(b, p) || SlimefunPlugin.getProtectionManager().hasPermission(p, b, ProtectableAction.INTERACT_BLOCK));
            }
            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
                return new int[0];
            }
            @Override
            public void newInstance(@Nonnull BlockMenu menu, @Nonnull Block b) {
                UpgradeableBlock.this.onNewInstance(menu, b);
                UpgradeManager.updateMenu(menu, UpgradeManager.getUpgrade(b.getLocation()), UpgradeableBlock.this);
            }
            @Override
            public int[] getSlotsAccessedByItemTransport(DirtyChestMenu menu, ItemTransportFlow flow, ItemStack item) {
                return UpgradeableBlock.this.getTransportSlots(menu, flow, item);
            }
        };
    }  
    
}
