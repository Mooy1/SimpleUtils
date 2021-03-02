package io.github.mooy1.simpleutils;

import io.github.mooy1.infinitylib.PluginUtils;
import lombok.experimental.UtilityClass;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import org.bukkit.Material;

import java.util.Locale;

@UtilityClass
public final class Items {

    public static final Category CATEGORY = new Category(PluginUtils.getKey("main"), new CustomItem(Material.COMPOSTER, "&6Simple Utils"), 0);
    
    // dusts
    public static final SlimefunItemStack NICKEL_DUST = dust(Material.SUGAR, "&7Nickel");
    public static final SlimefunItemStack COBALT_DUST = dust(Material.SUGAR, "&9Cobalt");

    private static SlimefunItemStack dust(Material material, String  name) {
        return new SlimefunItemStack(
                name.substring(2).toUpperCase(Locale.ROOT) + "_DUST",
                material,
                name + " Dust"
        );
    }
    
    // misc
    public static final SlimefunItemStack AUTOMATON_CORE = new SlimefunItemStack(
      "AUTOMATON_CORE",
      Material.POLISHED_GRANITE,
      "&6Automaton Core",
      "&7Core component of simple automated machines"      
    );
    
    public static final SlimefunItemStack HAMMER_ROD = new SlimefunItemStack(
            "HAMMER_ROD",
            Material.STICK,
            "&6Hammer Rod",
            "&7Core component of mining hammers"
    );
    
}
