package io.github.mooy1.gridexpansion.utils;

import io.github.mooy1.infinitylib.PluginUtils;
import lombok.experimental.UtilityClass;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

@UtilityClass
public final class RecipeCopierService {
    
    public static void copyDisplayRecipes(BiConsumer<ItemStack, ItemStack> consumer, Runnable after, SlimefunItemStack... items) {
        final List<AContainer> containers = new ArrayList<>(items.length);
        
        for (SlimefunItemStack item : items) {
            if (item.getItem() instanceof AContainer) {
                containers.add((AContainer) item.getItem());
            }
        }
        
        if (containers.size() > 0) {
            PluginUtils.runSync(() -> {
                for (AContainer container : containers) {
                    List<ItemStack> list = container.getDisplayRecipes();
                    for (int i = 0 ; i < list.size() ; i += 2) {
                        consumer.accept(list.get(i), list.get(i + 1));
                    }
                }
                after.run();
            }, 20);
        }
    }
    
}
