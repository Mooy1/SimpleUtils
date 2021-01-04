package io.github.mooy1.gridfoundation.implementation.wireless;

import io.github.mooy1.gridfoundation.setup.Categories;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class WirelessOutputNode extends SlimefunItem {
    
    public static final SlimefunItemStack ITEM = new SlimefunItemStack(
            "WIRELESS_OUTPUT_NODE",
            Material.END_ROD,
            "&9Wireless output node",
            "&7Transfers items from wireless input nodes to the connected inventory"
    );
    
    public WirelessOutputNode() {
        super(Categories.MAIN, ITEM, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                
        });
    }

    @Override
    public void preRegister() {
        super.preRegister();
        addItemHandler(WirelessUtils.NODE_HANDLER);
    }

}
