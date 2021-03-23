package io.github.mooy1.simpleutils;

import lombok.experimental.UtilityClass;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

@UtilityClass
public final class Items {

    public static final Category CATEGORY = new Category(new NamespacedKey(SimpleUtils.inst(), "main"), new CustomItem(Material.COMPOSTER, "&6Simple Utils"), 0);

    public static final SlimefunItemStack HAMMER_ROD = new SlimefunItemStack(
            "HAMMER_ROD",
            Material.BLAZE_ROD,
            "&6Hammer Rod",
            "&7Core component of mining hammers"
    );

}
