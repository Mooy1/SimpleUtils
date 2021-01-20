package io.github.mooy1.gridfoundation.implementation.upgrades;

import io.github.mooy1.infinitylib.PluginUtils;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.cscorelib2.chat.ChatColors;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class UpgradeManager {

    /**
     * Cache of locations and their upgrade
     */
    private static final Map<Location, UpgradeType> CACHE = new HashMap<>();

    /**
     * Block data key
     */
    private static final String INFO = "upgrade";

    /**
     * Key for persistent data
     */
    private static final NamespacedKey KEY = PluginUtils.getKey(INFO);

    /**
     * Default string of lore to add to new items and look for when replacing with upgraded tier
     */
    private static final String DEFAULT = ChatColors.color("&6Tier: " + UpgradeType.BASIC.getName());

    @Nonnull
    static UpgradeType getUpgrade(@Nonnull Location l) {
        UpgradeType cached = CACHE.get(l);
        if (cached != null) {
            return cached;
        }
        UpgradeType type = UpgradeType.get(BlockStorage.getLocationInfo(l, INFO));
        CACHE.put(l, type);
        return type;
    }

    static void setUpgrade(@Nonnull Location l, @Nonnull UpgradeType type, @Nonnull UpgradeableBlock block) {
        BlockStorage.addBlockInfo(l, INFO, type.name());
        CACHE.put(l, type);
        BlockMenu menu = BlockStorage.getInventory(l);
        if (menu != null) {
            updateMenu(menu, type, block);
        }
    }
    
    static void updateMenu(@Nonnull BlockMenu menu, @Nonnull UpgradeType type, @Nonnull UpgradeableBlock block) {
        int slot = block.getUpgradeSlot();
        if (slot == -1) {
            return;
        }
        UpgradeType max = block.getMaxLevel();
        List<String> stats = new ArrayList<>(3);
        block.getStats(stats, type.tier());
        if (type == max) {
            menu.replaceExistingItem(slot, new CustomItem(type.getMaterial(), "&6Upgrade: " + type.getName() + " &6(Max)", stats));
        } else {
            stats.add("&6Max: &e" + max.getName());
            menu.replaceExistingItem(slot, new CustomItem(type.getMaterial(), "&6Upgrade: &e" + type.getName(), stats));
        }
        
    }

    static void onPlace(@Nonnull Location l, @Nonnull ItemStack item, @Nonnull UpgradeableBlock block) {
        setUpgrade(l, UpgradeType.get(item.getItemMeta().getPersistentDataContainer().get(KEY, PersistentDataType.STRING)), block);
    }

    static void onBreak(@Nonnull BlockBreakEvent e, @Nonnull ItemStack item) {
        e.setDropItems(false);
        Location l = e.getBlock().getLocation();
        CACHE.remove(l);
        UpgradeType type = getUpgrade(l);
        if (type != UpgradeType.BASIC) {
            ItemMeta meta = item.getItemMeta();
            List<String> lore = meta.getLore();
            if (lore != null) {
                int index = lore.indexOf(DEFAULT);
                if (index > -1) {
                    lore.set(index, ChatColors.color("&6Tier: " + type.getName()));
                }
            }
            meta.getPersistentDataContainer().set(KEY, PersistentDataType.STRING, type.name());
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        e.getBlock().getWorld().dropItemNaturally(l,  item);
    }
    
    static void addDefaultMeta(@Nonnull SlimefunItemStack item) {
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(KEY, PersistentDataType.STRING, UpgradeType.BASIC.name());
        List<String> lore = meta.getLore();
        if (lore != null) {
            lore.add("");
            lore.add(DEFAULT);
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

}
