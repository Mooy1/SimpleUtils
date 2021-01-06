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

    BASIC(1, Material.WHITE_STAINED_GLASS_PANE, "&fBasic"),
    ADVANCED(2, Material.YELLOW_STAINED_GLASS_PANE, "&eAdvanced"),
    HARDENED(4, Material.ORANGE_STAINED_GLASS_PANE, "&6Hardened"),
    ELITE(8, Material.RED_STAINED_GLASS_PANE, "&cElite"),
    REINFORCED(16, Material.BLACK_STAINED_GLASS_PANE, "&7Reinforced"),
    INFUSED(32, Material.BLUE_STAINED_GLASS_PANE, "&9Infused"),
    ULTIMATE(64, Material.PURPLE_STAINED_GLASS_PANE, "&dUltimate");

    private final int level;
    private final Material material;
    private final String name;
    private final SlimefunItemStack item;

    private static final Map<String, UpgradeType> values = new HashMap<>();

    UpgradeType(int level, Material material, String name) {
        this.level = level;
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
