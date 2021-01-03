package io.github.mooy1.gridfoundation.implementation.upgrades;

import io.github.mooy1.infinitylib.PluginUtils;
import io.github.mooy1.infinitylib.player.EventUtils;
import io.github.mooy1.infinitylib.player.MessageUtils;
import io.github.mooy1.gridfoundation.GridFoundation;
import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class UpgradeManager implements Listener {

    private static final Map<Location, UpgradeType> cache = new HashMap<>();
    private static final String string = "upgrade";
    private static final NamespacedKey key = PluginUtils.getKey(string);
    
    @Nonnull
    static UpgradeType getType(@Nonnull Location l) {
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

    private static void setType(@Nonnull Location l, @Nonnull UpgradeType type, @Nonnull UpgradeableBlock block) {
        BlockStorage.addBlockInfo(l, string, type.name());
        cache.put(l, type);

        if (block.getMaxLevel() != UpgradeType.NONE) {
            BlockMenu menu = BlockStorage.getInventory(l);

            if (menu != null) {
                menu.replaceExistingItem(block.getUpgradeSlot(), new CustomItem(
                        type.material,
                        "&6Tier: &e" + type.name,
                        "&6Max: &e" + block.getMaxLevel().name
                ));
            }
        }
    }

    public static void onPlace(@Nonnull Location l, @Nonnull ItemStack item, @Nonnull UpgradeableBlock block) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            UpgradeType type = UpgradeType.get(meta.getPersistentDataContainer().get(key, PersistentDataType.STRING));
            if (type == null) {
                type = UpgradeType.NONE;
            }
            setType(l, type, block);
        }
    }

    public static void onBreak(@Nonnull Location l, @Nonnull ItemStack item) {
        UpgradeType type = getType(l);
        if (type != UpgradeType.NONE) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, type.name());
                List<String> lore = meta.getLore();
                if (lore != null) {
                    lore.add("");
                    lore.add("&6Upgraded to: " + type.name);
                }
                item.setItemMeta(meta);
            }
        }
    }

    public UpgradeManager(@Nonnull GridFoundation plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onUpgrade(PlayerRightClickEvent e) {
        
        if (!EventUtils.checkRightClickEvent(e)) {
            return;
        }

        if (!e.getClickedBlock().isPresent()
                || !e.getSlimefunBlock().isPresent() || !(e.getSlimefunBlock().get() instanceof UpgradeableBlock)
                || !e.getSlimefunItem().isPresent() || !(e.getSlimefunItem().get() instanceof UpgradeKit)
        ) {
            return;
        }

        UpgradeableBlock block = (UpgradeableBlock) e.getSlimefunBlock().get();

        if (block.getMaxLevel() == UpgradeType.NONE) {

            MessageUtils.message(e.getPlayer(), "&eThis Block cannot be upgraded!");
            return;

        }

        Location target = e.getClickedBlock().get().getLocation();

        UpgradeType old = getType(target);

        UpgradeType type = ((UpgradeKit) e.getSlimefunItem().get()).type;

        if (old.ordinal() >= block.getMaxLevel().ordinal()) {

            MessageUtils.message(e.getPlayer(), "&eThis Block cannot be upgraded any further!");

        } else if (type.ordinal() - old.ordinal() == 1) {

            setType(target, type, block);

            MessageUtils.message(e.getPlayer(), "&aUpgraded to " + type.name);

            e.getPlayer().playSound(target, Sound.BLOCK_ANVIL_USE, 1, 1);

        } else if (old.ordinal() + 1 < UpgradeType.values().length) {

            MessageUtils.message(e.getPlayer(), "&eThis Block requires a " + UpgradeType.values()[old.ordinal() + 1].item.getDisplayName() + "&e to be upgraded further!");

        }
    }

}
