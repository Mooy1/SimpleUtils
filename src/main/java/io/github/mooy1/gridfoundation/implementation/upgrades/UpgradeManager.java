package io.github.mooy1.gridfoundation.implementation.upgrades;

import io.github.mooy1.infinitylib.PluginUtils;
import io.github.mooy1.infinitylib.player.MessageUtils;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.cscorelib2.chat.ChatColors;
import me.mrCookieSlime.Slimefun.cscorelib2.inventory.ItemUtils;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class UpgradeManager {

    private static final Map<Location, UpgradeType> cache = new HashMap<>();
    private static final String string = "upgrade";
    private static final NamespacedKey key = PluginUtils.getKey(string);
    
    @Nonnull
    static UpgradeType getUpgrade(@Nonnull Location l) {
        UpgradeType cached = cache.get(l);
        if (cached != null) {
            return cached;
        }
        UpgradeType type = UpgradeType.get(BlockStorage.getLocationInfo(l, string));
        if (type == null) {
            BlockStorage.addBlockInfo(l, string, null);
            type = UpgradeType.BASIC;
        }
        cache.put(l, type);
        return type;
    }

    private static void setUpgrade(@Nonnull Location l, @Nonnull UpgradeType type, @Nonnull UpgradeableBlock block) {
        BlockStorage.addBlockInfo(l, string, type.name());
        cache.put(l, type);

        BlockMenu menu = BlockStorage.getInventory(l);

        if (menu != null) {
            updateMenu(menu, type, block);
        }
    }
    
    static void updateMenu(@Nonnull BlockMenu menu, @Nonnull UpgradeType type, @Nonnull UpgradeableBlock block) {
        if (block.getUpgradeSlot() == -1) {
            return;
        }
        List<String> stats = new ArrayList<>(3);
        block.getStats(stats, type.tier());
        if (type == block.getMaxLevel()) {
            menu.replaceExistingItem(block.getUpgradeSlot(), new CustomItem(type.getMaterial(), "&6Upgrade: " + type.getName() + " &6(Max)", stats));
        } else {
            stats.add("&6Max: &e" + block.getMaxLevel().getName());
            menu.replaceExistingItem(block.getUpgradeSlot(), new CustomItem(type.getMaterial(), "&6Upgrade: &e" + type.getName(), stats));
        }
        
    }

    static void onPlace(@Nonnull Location l, @Nonnull ItemStack item, @Nonnull UpgradeableBlock block) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            UpgradeType type = UpgradeType.get(meta.getPersistentDataContainer().get(key, PersistentDataType.STRING));
            if (type == null) {
                type = UpgradeType.BASIC;
            }
            setUpgrade(l, type, block);
        }
    }

    static void onBreak(@Nonnull BlockBreakEvent e, @Nonnull Location l, @Nonnull ItemStack item) {
        e.setDropItems(false);
        setMeta(item, getUpgrade(l));
        World w = l.getWorld();
        if (w != null) {
            w.dropItemNaturally(l,  item);
        }
    }
    
    private static final String DEFAULT = ChatColors.color("&6Tier: " + UpgradeType.BASIC.getName());
    
    static void setMeta(@Nonnull ItemStack item, @Nonnull UpgradeType type) {
        if (type != UpgradeType.BASIC) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                List<String> lore = meta.getLore();
                if (lore != null) {
                    int index = lore.indexOf(DEFAULT);
                    if (index > -1) {
                        lore.set(index, ChatColors.color("&6Tier: " + type.getName()));
                    }
                }
                meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, type.name());
                meta.setLore(lore);
                item.setItemMeta(meta);
            }
        }
    }
    
    public static void addMeta(@Nonnull SlimefunItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, UpgradeType.BASIC.name());
            List<String> lore = meta.getLore();
            if (lore != null) {
                lore.add("");
                lore.add(DEFAULT);
            }
            meta.setLore(lore);
        }
        item.setItemMeta(meta);
    }

    @ParametersAreNonnullByDefault
    static void onUpgrade(Player p, ItemStack item, Location target, UpgradeableBlock block, UpgradeKit kit) {
        
        if (block.getMaxLevel() == UpgradeType.BASIC) {

            MessageUtils.message(p, "&eThis Block cannot be upgraded!");
            return;

        }
        
        UpgradeType old = getUpgrade(target);
        
        if (old.tier() >= block.getMaxLevel().ordinal()) {

            MessageUtils.message(p, "&eThis Block cannot be upgraded any further!");

        } else if (kit.isConversion() || kit.getType().ordinal() - old.tier() == 1) {

            setUpgrade(target, kit.getType(), block);

            if (p.getGameMode() != GameMode.CREATIVE) {
                ItemUtils.consumeItem(item, false);
            }
            
            if (kit.isConversion()) {
                MessageUtils.message(p, "&aConverted to " + kit.getType().getName());
            } else {
                MessageUtils.message(p, "&aUpgraded to " + kit.getType().getName());
            }
            
            p.playSound(target, Sound.BLOCK_ANVIL_USE, 1, 1);

        } else if (old.tier() + 1 < UpgradeType.values().length) {

            MessageUtils.message(p, "&eThis Block requires an " + UpgradeType.values()[old.tier() + 1].getItem().getDisplayName() + "&e to be upgraded further!");

        }
    }

}
