package io.github.mooy1.simpleutils.blocks;

import io.github.mooy1.infinitylib.PluginUtils;
import io.github.mooy1.infinitylib.filter.FilterType;
import io.github.mooy1.infinitylib.filter.ItemFilter;
import io.github.mooy1.infinitylib.items.LoreUtils;
import io.github.thebusybiscuit.slimefun4.core.multiblocks.MultiBlockMachine;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.cscorelib2.collections.RandomizedSet;
import me.mrCookieSlime.Slimefun.cscorelib2.inventory.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ManualSieve extends MultiBlockMachine {

    public static final SlimefunItemStack ITEM = new SlimefunItemStack(
            "MANUAL_SIEVE",
            Material.COMPOSTER,
            "&6Sieve",
            "&7Sifts gravel, sand, and crushed materials into ore chunks and minerals"
    );
    public static final RecipeType TYPE = new RecipeType(PluginUtils.getKey("manual_sieve"), ITEM);

    private static final Map<ItemFilter, RandomizedSet<ItemStack>> recipes = new HashMap<>();
    public static final List<ItemStack> displayRecipes = new ArrayList<>();
    
    static {
        addRecipe(new ItemStack(Material.GRAVEL),  new ItemStack[] {
                new ItemStack(Material.AIR), new ItemStack(Material.FLINT),
                CrushedOre.IRON, CrushedOre.COPPER, CrushedOre.ZINC, CrushedOre.ALUMINUM, CrushedOre.LEAD, CrushedOre.TIN
        }, new float[] {
                50, 5,
                10, 15, 5, 5, 5, 5
        });
        addRecipe(new ItemStack(Material.SAND),  new ItemStack[] {
                new ItemStack(Material.AIR), new ItemStack(Material.QUARTZ), CrushedOre.MAGNESIUM,
                CrushedOre.GOLD, CrushedOre.SILVER, CrushedOre.COPPER
        }, new float[] {
                50, 10, 5,
                20, 5, 10
        });
        addRecipe(CrushedBlock.NETHER,  new ItemStack[] {
                new ItemStack(Material.AIR), new ItemStack(Material.GOLD_NUGGET), CrushedOre.GOLD

        }, new float[] {
                50, 25, 25
        });
        addRecipe(CrushedBlock.END,  new ItemStack[] {

        }, new float[] {

        });
        addRecipe(CrushedBlock.DUST,  new ItemStack[] {
                new ItemStack(Material.AIR),

        }, new float[] {
                1
        });
        addRecipe(CrushedBlock.ANDESITE,  new ItemStack[] {
                new ItemStack(Material.AIR),

        }, new float[] {
                1

        });
        addRecipe(CrushedBlock.DIORITE, new ItemStack[] {
                new ItemStack(Material.AIR),

        }, new float[] {
                1

        });
        addRecipe(CrushedBlock.GRANITE,  new ItemStack[] {
                new ItemStack(Material.AIR),

        }, new float[] {
                1

        });
        addRecipe(new ItemStack(Material.DIRT),  new ItemStack[] {

        }, new float[] {

        });
        addRecipe(new ItemStack(Material.SOUL_SAND),  new ItemStack[] {

        }, new float[] {

        });
    }
    
    public ManualSieve() {
        super(Categories.MAIN, ITEM, new ItemStack[] {
            null, null, null,
            null, new ItemStack(Material.OAK_TRAPDOOR), null,
            null, new ItemStack(Material.COMPOSTER), null
        }, BlockFace.SELF);
    }

    // use AIR for no output
    private static void addRecipe(ItemStack item, ItemStack[] stacks, float[] chances) {
        recipes.put(new ItemFilter(item, FilterType.IGNORE_AMOUNT), new RandomizedSet<ItemStack>() {{
            for (int i = 0 ; i < stacks.length ; i ++) {
                add(stacks[i], chances[i]);
            }
        }});
        for (int i = 0 ; i < stacks.length ; i ++) {
            if (stacks[i].getType() != Material.AIR) {
                displayRecipes.add(item);
                ItemStack chance = stacks[i].clone();
                LoreUtils.addLore(chance, "", ChatColor.GOLD + "Chance: " + ChatColor.YELLOW + chances[i] + "%");
                displayRecipes.add(chance);
            }
        }
    }

    /**
     * returns null if invalid input, or air depending on chances
     */
    @Nullable
    public static ItemStack getOutput(@Nullable ItemStack item) {
        if (item == null) {
            return null;
        }
        RandomizedSet<ItemStack> set = recipes.get(new ItemFilter(item, FilterType.IGNORE_AMOUNT));
        if (set == null) {
            return null;
        }
        return set.getRandom();
    }

    @Nonnull
    @Override
    public List<ItemStack> getDisplayRecipes() {
        return displayRecipes;
    }

    @Override
    public void onInteract(Player p, Block b) {
        ItemStack input = p.getInventory().getItemInMainHand();
        
        ItemStack item = getOutput(input);

        if (item != null) {

            if (p.getGameMode() != GameMode.CREATIVE) {
                ItemUtils.consumeItem(input, 1,false);
            }

            if (item.getType() != Material.AIR) {
                p.playSound(b.getLocation(), Sound.BLOCK_SAND_BREAK, 1, 1);
                
                ItemStack output = item.clone();
                
                Inventory outputChest = findOutputChest(b.getRelative(BlockFace.DOWN), output);

                if (outputChest != null) {
                    outputChest.addItem(output);
                } else {
                    b.getWorld().dropItemNaturally(b.getLocation().add(0, .5, 0), output);
                }
            }
        }
    }

}
