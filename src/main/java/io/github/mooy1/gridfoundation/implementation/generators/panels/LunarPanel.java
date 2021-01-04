package io.github.mooy1.gridfoundation.implementation.generators.panels;

import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.inventory.ItemStack;

public final class LunarPanel extends AbstractPanel {

    public static final SlimefunItemStack ITEM = make(1, "Lunar", "darkness");
    
    public LunarPanel() {
        super(false, 1, ITEM, new ItemStack[] {
                
        });
    }

}
