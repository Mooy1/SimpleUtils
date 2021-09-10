package io.github.mooy1.simpleutils.implementation.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

import org.bukkit.inventory.ItemStack;

import io.github.mooy1.infinitylib.common.Scheduler;
import io.github.mooy1.infinitylib.common.StackUtils;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks.EnhancedCraftingTable;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.ItemStackSnapshot;

class FakeEnhancedCrafter extends EnhancedCraftingTable {

    private final List<ItemStackSnapshot[]> inputs = new ArrayList<>();
    private final List<ItemStack> outputs = new ArrayList<>();

    FakeEnhancedCrafter(ItemGroup category, SlimefunItemStack item) {
        super(category, item);

        Scheduler.run(this::init);
    }

    private void init() {
        EnhancedCraftingTable real = SlimefunItems.ENHANCED_CRAFTING_TABLE.getItem(EnhancedCraftingTable.class);
        List<ItemStack[]> recipes = Objects.requireNonNull(real).getRecipes();
        for (int i = 0; i < recipes.size(); i++) {
            if (i % 2 == 0) {
                inputs.add(ItemStackSnapshot.wrapArray(recipes.get(i)));
            } else {
                outputs.add(recipes.get(i)[0]);
            }
        }
    }

    @Nullable
    ItemStack craft(ItemStackSnapshot[] input) {
        loop: for (int i = 0; i < inputs.size(); i++) {
            ItemStackSnapshot[] recipe = inputs.get(i);

            for (int j = 0; j < recipe.length; j++) {
                if (!StackUtils.isSimilar(input[j], recipe[j])) {
                    continue loop;
                }
            }

            return outputs.get(i);
        }

        return null;
    }

}
