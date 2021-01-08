package io.github.mooy1.gridfoundation.implementation.upgrades;

import lombok.Getter;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Material;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Getter
public enum UpgradeType {

    BASIC(Material.WHITE_STAINED_GLASS_PANE, "&fBasic"),
    ADVANCED(Material.YELLOW_STAINED_GLASS_PANE, "&eAdvanced"),
    HARDENED(Material.ORANGE_STAINED_GLASS_PANE, "&6Hardened"),
    ELITE(Material.RED_STAINED_GLASS_PANE, "&cElite"),
    REINFORCED(Material.BLACK_STAINED_GLASS_PANE, "&7Reinforced"),
    INFUSED(Material.BLUE_STAINED_GLASS_PANE, "&9Infused"),
    ULTIMATE(Material.PURPLE_STAINED_GLASS_PANE, "&dUltimate");

    private final Material material;
    private final String name;
    private final SlimefunItemStack item;

    private static final Map<String, UpgradeType> values = new HashMap<>();

    UpgradeType(Material material, String name) {
        this.material = material;
        this.name = name;
        this.item = new SlimefunItemStack(
                name.substring(2).toUpperCase(Locale.ROOT) + "_UPGRADE_KIT",
                material,
                name + " Upgrade Kit",
                "&7Use on a grid machine or generator",
                "&7to upgrade it's speed or generation",
                "&7Each tier must be used before the next"
        );
    }
    
    public int tier() {
        return this.ordinal();
    }

    static {
        for (UpgradeType type : values()) {
            values.put(type.name(), type);
        }
    }

    @Nullable
    static UpgradeType get(@Nullable String type) {
        if (type == null) {
            return BASIC;
        }
        return values.get(type);
    }

}
