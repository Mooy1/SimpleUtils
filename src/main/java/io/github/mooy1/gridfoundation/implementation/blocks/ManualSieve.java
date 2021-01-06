package io.github.mooy1.gridfoundation.implementation.blocks;

import io.github.mooy1.gridfoundation.implementation.materials.CrushedBlock;
import io.github.mooy1.gridfoundation.setup.Categories;
import io.github.mooy1.infinitylib.PluginUtils;
import io.github.mooy1.infinitylib.filter.FilterType;
import io.github.mooy1.infinitylib.filter.ItemFilter;
import io.github.mooy1.infinitylib.items.LoreUtils;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.cscorelib2.collections.RandomizedSet;
import me.mrCookieSlime.Slimefun.cscorelib2.inventory.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ManualSieve extends SimpleSlimefunItem<BlockUseHandler> implements RecipeDisplayItem {

    public static final SlimefunItemStack ITEM = new SlimefunItemStack(
            "MANUAL_SIEVE",
            Material.CAULDRON,
            "&6Sieve",
            "&7Sifts gravel, sand, and crushed materials into dusts"
    );
    public static final RecipeType TYPE = new RecipeType(PluginUtils.getKey("manual_sieve"), ITEM);

    private static final Map<ItemFilter, RandomizedSet<ItemStack>> recipes = new HashMap<>();
    public static final List<ItemStack> displayRecipes = new ArrayList<>();
    
    private static void addRecipe(ItemStack item, float empty, ItemStack[] stacks, float[] chances) {
        recipes.put(new ItemFilter(item, FilterType.IGNORE_AMOUNT), new RandomizedSet<ItemStack>() {{
            add(new ItemStack(Material.STONE), empty);
            for (int i = 0 ; i < stacks.length ; i ++) {
                add(stacks[i], chances[i]);
            }
        }});
        for (int i = 0 ; i < stacks.length ; i ++) {
            displayRecipes.add(item);
            ItemStack chance = stacks[i].clone();
            LoreUtils.addLore(chance, "", ChatColor.GOLD + "Chance: " + ChatColor.YELLOW + chances[i] + "%");
            displayRecipes.add(chance);
        }
    }
    
    /**
     * returns null if invalid input, or empty optional depending on chance
     */
    @Nullable
    public static Optional<ItemStack> getOutput(@Nullable ItemStack item) {
        if (item == null) {
            return null;
        }
        RandomizedSet<ItemStack> set = recipes.get(new ItemFilter(item, FilterType.IGNORE_AMOUNT));
        if (set == null) {
            return null;
        }
        ItemStack output = set.getRandom();
        if (output.getType() == Material.STONE) {
            return Optional.empty();
        }
        return Optional.of(output.clone());
    }
    
    /**
     * returns null if invalid input, or empty optional depending on chance
     */
    @Nullable
    public static List<Optional<ItemStack>> getOutputs(@Nullable ItemStack item, int outputs) {
        if (item == null) {
            return null;
        }
        RandomizedSet<ItemStack> set = recipes.get(new ItemFilter(item, FilterType.IGNORE_AMOUNT));
        if (set == null) {
            return null;
        }
        List<Optional<ItemStack>> list = new  ArrayList<>(outputs);
        for (int i = 0 ; i < outputs ; i++) {
            ItemStack output = set.getRandom();
            if (output.getType() == Material.STONE) {
                list.add(Optional.empty());
            } else {
                list.add(Optional.of(output.clone()));
            }
        }
        return list;
    }

    public ManualSieve() {
        super(Categories.MAIN, ITEM, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {

        });

        addRecipe(new ItemStack(Material.GRAVEL), 5, new ItemStack[] {
                SlimefunItems.IRON_DUST
        }, new float[] {
                5
        });
        addRecipe(new ItemStack(Material.SAND), 50, new ItemStack[] {

        }, new float[] {

        });
        addRecipe(new ItemStack(Material.DIRT), 50, new ItemStack[] {

        }, new float[] {

        });
        addRecipe(new ItemStack(Material.SOUL_SAND), 50, new ItemStack[] {

        }, new float[] {

        });
        addRecipe(CrushedBlock.NETHER, 50, new ItemStack[] {

        }, new float[] {

        });
        addRecipe(CrushedBlock.END, 50, new ItemStack[] {

        }, new float[] {

        });
        addRecipe(CrushedBlock.DUST, 50, new ItemStack[] {

        }, new float[] {

        });
        addRecipe(CrushedBlock.ANDESITE, 50, new ItemStack[] {

        }, new float[] {

        });
        addRecipe(CrushedBlock.DIORITE, 50, new ItemStack[] {

        }, new float[] {

        });
        addRecipe(CrushedBlock.GRANITE, 50, new ItemStack[] {

        }, new float[] {

        });
    }

    @Nonnull
    @Override
    public BlockUseHandler getItemHandler() {
        return e -> {
            if (e.getClickedBlock().isPresent()) {
                e.setUseBlock(Event.Result.DENY);
                
                Optional<ItemStack> item = getOutput(e.getItem());

                if (item != null) {

                    ItemUtils.consumeItem(e.getItem(), 1,false);
                    
                    item.ifPresent(stack -> {
                        e.getPlayer().playSound(e.getClickedBlock().get().getLocation(), Sound.BLOCK_SAND_BREAK, 1, 1);
                        e.getClickedBlock().get().getWorld().dropItemNaturally(e.getClickedBlock().get().getLocation().add(0, .8, 0), stack);
                    });
                }
            }
        };
    }

    @Nonnull
    @Override
    public List<ItemStack> getDisplayRecipes() {
        return displayRecipes;
    }

}
