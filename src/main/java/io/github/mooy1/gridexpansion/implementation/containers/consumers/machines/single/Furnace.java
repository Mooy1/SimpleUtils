package io.github.mooy1.gridexpansion.implementation.containers.consumers.machines.single;

import io.github.mooy1.gridexpansion.utils.RecipeCopierService;
import io.github.mooy1.infinitylib.filter.FilterType;
import io.github.mooy1.infinitylib.filter.ItemFilter;
import io.github.mooy1.infinitylib.items.StackUtils;
import io.github.mooy1.infinitylib.misc.DelayedRecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.cscorelib2.collections.Pair;
import org.bukkit.Material;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;

import java.util.Objects;

public final class Furnace extends AbstractSingleProcessor {

    public static final SlimefunItemStack ITEM = make(4, "Furnace", "Smelts materials using GP", Material.FURNACE);
    public static final DelayedRecipeType TYPE = new DelayedRecipeType(ITEM);
    
    public Furnace() {
        super(ITEM, Material.FLINT_AND_STEEL,4, new ItemStack[] {
                
        });
        
        TYPE.acceptEach((stacks, stack) -> addRecipe(stack, stack, true));

        RecipeCopierService.copyDisplayRecipes((stack, stack2) -> addRecipe(stack, stack2, false), () -> 
                addRecipe(SlimefunItems.GOLD_DUST, new ItemStack(Material.GOLD_INGOT), true), SlimefunItems.ELECTRIC_INGOT_FACTORY
        );

        SlimefunPlugin.getMinecraftRecipeService().subscribe(snapshot -> {
            for (FurnaceRecipe furnaceRecipe : snapshot.getRecipes(FurnaceRecipe.class)) {
                RecipeChoice choice = furnaceRecipe.getInputChoice();
                if (choice instanceof RecipeChoice.MaterialChoice) {
                    for (Material input : ((RecipeChoice.MaterialChoice) choice).getChoices()) {
                        this.displayRecipes.add(new ItemStack(input));
                        this.displayRecipes.add(furnaceRecipe.getResult());
                        this.recipes.put(new ItemFilter(input, FilterType.MIN_AMOUNT), new Pair<>(furnaceRecipe.getResult(), 1));
                    }
                }
            }
        });
    }
    
    public void addRecipe(ItemStack input, ItemStack output, boolean force) {
        if (!force) {
            String id = StackUtils.getID(input);
            if (Objects.equals("GOLD_DUST", id)) {
                return;
            }
        }
        this.displayRecipes.add(0, output);
        this.displayRecipes.add(0, input);
        this.recipes.put(new ItemFilter(input, FilterType.MIN_AMOUNT), new Pair<>(output, input.getAmount()));
    }

    @Override
    protected int getConsumption() {
        return 4;
    }

}
