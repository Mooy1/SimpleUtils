package io.github.mooy1.simpleutils;

import io.github.mooy1.infinitylib.PluginUtils;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import org.bukkit.Material;

import javax.annotation.Nonnull;

public final class Setup {

    public static void setup(@Nonnull SimpleUtils plugin) {
        
        Category category = new Category(PluginUtils.getKey("main"), new CustomItem(Material.COMPOSTER), 0);

        
        
    }

}
