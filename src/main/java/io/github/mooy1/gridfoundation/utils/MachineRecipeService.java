package io.github.mooy1.gridfoundation.utils;

import io.github.mooy1.infinitylib.PluginUtils;
import io.github.mooy1.infinitylib.items.StackUtils;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Material;
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
    

    public static void acceptSkipMaterials(SlimefunItemStack item, BiConsumer<ItemStack, ItemStack> consumer, Material... skips) {
        if (item.getItem() instanceof AContainer) {

            AContainer machine = (AContainer) item.getItem();

            PluginUtils.runSync(() -> {
                List<ItemStack> list = machine.getDisplayRecipes();
                
                loop: for (int i = 0 ; i < list.size() ; i+=2) {
                    ItemStack input = list.get(i);
                    
                    for (Material material : skips) {
                        if (input.getType() == material) continue loop;
                    }
                    
                    consumer.accept(input, list.get(i + 1));
                }

            }, 20);
        }
    }

    public static void acceptSkipIDS(SlimefunItemStack item, BiConsumer<ItemStack, ItemStack> consumer, String... skips) {
        if (item.getItem() instanceof AContainer) {

            AContainer machine = (AContainer) item.getItem();

            PluginUtils.runSync(() -> {
                List<ItemStack> list = machine.getDisplayRecipes();

                loop: for (int i = 0 ; i < list.size() ; i+=2) {
                    ItemStack input = list.get(i);

                    String id = StackUtils.getItemID(input, false);
                    
                    if (id != null) {
                        for (String string : skips) {
                            if (id.equals(string)) continue loop;
                        }
                    }
                    
                    consumer.accept(input, list.get(i + 1));
                }

            }, 20);
        }
    }
    
}
