package io.github.mooy1.simpleutils.implementation.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks.EnhancedCraftingTable;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;

class FakeEnhancedCrafter extends EnhancedCraftingTable {

    private final List<ItemStack[]> inputs = new ArrayList<>();
    private final List<ItemStack> outputs = new ArrayList<>();

    FakeEnhancedCrafter(ItemGroup category, SlimefunItemStack item) {
        super(category, item);

        EnhancedCraftingTable real = SlimefunItems.ENHANCED_CRAFTING_TABLE.getItem(EnhancedCraftingTable.class);
        List<ItemStack[]> recipes = Objects.requireNonNull(real).getRecipes();
        for (int i = 0; i < recipes.size(); i++) {
            if (i % 2 == 0) {
                inputs.add(recipes.get(i));
            } else {
                outputs.add(recipes.get(i)[0]);
            }
        }
    }

    @Nullable
    ItemStack craft(ItemStack[] input) {
        loop: for (int i = 0; i < inputs.size(); i++) {
            ItemStack[] recipe = inputs.get(i);

            for (int j = 0; j < recipe.length; j++) {
                if (!SlimefunUtils.isItemSimilar(input[i], recipe[i], false, false)) {
                    continue loop;
                }
            }

            return outputs.get(i);
        }

        return null;
    }

}
