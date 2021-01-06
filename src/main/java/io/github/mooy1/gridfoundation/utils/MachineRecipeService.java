package io.github.mooy1.gridfoundation.utils;

import io.github.mooy1.infinitylib.PluginUtils;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.BiConsumer;

public final class MachineRecipeService {
    
    public static void accept(SlimefunItemStack item, BiConsumer<ItemStack, ItemStack> consumer) {
        if (item.getItem() instanceof AContainer) {

            AContainer machine = (AContainer) item.getItem();

            PluginUtils.runSync(() -> {
                List<ItemStack> list = machine.getDisplayRecipes();

                for (int i = 0 ; i < list.size() ; i += 2) {
                    consumer.accept(list.get(i), list.get(i + 1));
                }

            }, 20);
        }
    }
    
}
