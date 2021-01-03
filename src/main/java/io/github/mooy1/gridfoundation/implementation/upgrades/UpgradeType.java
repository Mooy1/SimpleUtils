package io.github.mooy1.gridfoundation.implementation.upgrades;

import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public enum UpgradeType {
    
    NONE(1, Material.BARRIER, "&cNone"),
    BASIC(2, Material.WHITE_STAINED_GLASS_PANE, "&fBasic"),
    ADVANCED(4, Material.YELLOW_STAINED_GLASS_PANE, "&eAdvanced"),
    HARDENED(8, Material.ORANGE_STAINED_GLASS_PANE, "&6Hardened"),
    REINFORCED(16, Material.BLACK_STAINED_GLASS_PANE, "&8Reinforced"),
    INFUSED(32, Material.BLUE_STAINED_GLASS_PANE, "&9Infused"),
    ULTIMATE(64, Material.PURPLE_STAINED_GLASS_PANE, "&dUltimate");
    
    public final int level;
    final Material material;
    final String name;
    final SlimefunItemStack item;
    
    UpgradeType(int level, Material material, String name) {
        this.level = level;
        this.material = material;
        this.name = name;
        this.item = new SlimefunItemStack(
                 ChatColor.stripColor(name).toUpperCase(Locale.ROOT) + "_UPGRADE_KIT",
                material,
                name + " Upgrade Kit",
                "&7Use on a grid machine or generator to upgrade it's speed or generation",
                "&7Each tier must be used before the next"
        );
    }

    private static final Map<String, UpgradeType> values = new HashMap<>();

    static {
        for (UpgradeType type : values()) {
            values.put(type.name(), type);
        }
    }

    @Nullable
    static UpgradeType get(@Nullable String type) {
        if (type == null) {
            return NONE;
        }
        return values.get(type);
    }
    
}
