package io.github.mooy1.slimegrid.implementation.wireless;

import io.github.mooy1.slimegrid.lists.Categories;
import io.github.mooy1.slimegrid.lists.Items;
import io.github.mooy1.slimegrid.utils.WirelessUtils;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.inventory.ItemStack;

public class WirelessOutputNode extends SlimefunItem {
    
    public WirelessOutputNode() {
        super(Categories.MACHINES, Items.WIRELESS_OUTPUT_NODE, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                
        }, new SlimefunItemStack(Items.WIRELESS_OUTPUT_NODE, 3));
        
        addItemHandler(WirelessUtils.NODE_HANDLER);
        
    }
    
}
