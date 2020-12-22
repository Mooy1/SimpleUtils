package io.github.mooy1.slimegrid.lists;

import io.github.mooy1.slimegrid.SlimeGrid;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

public final class Categories {
    
    private static final SlimeGrid plugin = SlimeGrid.getInstance();

    public static final Category MACHINES = new Category(new NamespacedKey(plugin, "machines"), new CustomItem(Material.SLIME_BLOCK, "&6Grid Machines"), 2);
    public static final Category GENERATORS = new Category(new NamespacedKey(plugin, "generators"), new CustomItem(Material.HONEY_BLOCK, "&6Grid Generators"), 2);
    public static final Category COMPONENTS = new Category(new NamespacedKey(plugin, "components"), new CustomItem(Material.HONEYCOMB, "&6Grid Components"), 2);
    
}
