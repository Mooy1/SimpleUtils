package io.github.mooy1.gridfoundation.utils;

import io.github.mooy1.infinitylib.PluginUtils;
import lombok.experimental.UtilityClass;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

@UtilityClass
public final class MachineRecipeService {
    
    public static void accept(BiConsumer<ItemStack, ItemStack> consumer, Runnable after, SlimefunItemStack... items) {
        for (SlimefunItemStack item : items) {
            if (!(item.getItem() instanceof AContainer)) {
                return;
            }
        }

        PluginUtils.runSync(() -> {
            
            for (SlimefunItemStack item : items) {

                List<ItemStack> list = ((AContainer) Objects.requireNonNull(item.getItem())).getDisplayRecipes();

                for (int i = 0 ; i < list.size() ; i += 2) {
                    consumer.accept(list.get(i), list.get(i + 1));
                }
                
            }
            
        }, 20);

        PluginUtils.runSync(after, 30);
    }
    
}
