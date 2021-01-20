package io.github.mooy1.gridfoundation.utils;

import io.github.mooy1.infinitylib.filter.MultiFilter;
import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public final class CombinedCraftingService {

    private static final Map<MultiFilter, ItemStack> recipes = new HashMap<>();
    
    public static void setup() {
        
    }
    
}
