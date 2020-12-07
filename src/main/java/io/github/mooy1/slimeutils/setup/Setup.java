package io.github.mooy1.slimeutils.setup;

import io.github.mooy1.slimeutils.SlimeUtils;
import io.github.mooy1.slimeutils.lists.Categories;
import io.github.mooy1.slimeutils.lists.Items;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

public final class Setup {
    
    public static void setup(SlimeUtils plugin) {
        
        new SlimefunItem(Categories.MAIN, Items.ADDON_INFO, RecipeType.NULL, null).register(plugin);
        
    }

}
