package io.github.mooy1.slimeutils.lists;

import io.github.mooy1.slimeutils.SlimeUtils;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

public final class Categories {
    
    private static final SlimeUtils plugin = SlimeUtils.getInstance();

    public static final Category MAIN = new Category(new NamespacedKey(plugin, "main"), new CustomItem(Material.EMERALD_BLOCK, "main"), 3);
    
}
