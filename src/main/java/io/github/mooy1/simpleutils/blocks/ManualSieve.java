package io.github.mooy1.simpleutils.blocks;

import io.github.mooy1.infinitylib.PluginUtils;
import io.github.mooy1.infinitylib.items.LoreUtils;
import io.github.mooy1.infinitylib.recipes.normal.RandomRecipeMap;
import io.github.mooy1.simpleutils.Materials;
import io.github.thebusybiscuit.slimefun4.core.multiblocks.MultiBlockMachine;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
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
import java.util.List;

public final class ManualSieve extends MultiBlockMachine {
    
    public static final SlimefunItemStack ITEM = new SlimefunItemStack(
            "MANUAL_SIEVE",
            Material.COMPOSTER,
            "&6Manual Sieve",
            "&7Sifts gravel, sand, and crushed materials",
            "&7into ore chunks and minerals"
    );
    public static final RecipeType TYPE = new RecipeType(PluginUtils.getKey("manual_sieve"), ITEM);
    private static final RandomRecipeMap RECIPES = new RandomRecipeMap();
    public static final List<ItemStack> DISPLAY_RECIPES = new ArrayList<>();
    
    static {
        addRecipe(new ItemStack(Material.GRAVEL),  new ItemStack[] {
                new ItemStack(Material.AIR), new ItemStack(Material.FLINT),
                Materials.CRUSHED_IRON, Materials.CRUSHED_COPPER, Materials.CRUSHED_ZINC,
                Materials.CRUSHED_ALUMINUM, Materials.CRUSHED_LEAD, Materials.CRUSHED_TIN
        }, new float[] {
                60, 14,
                5, 5, 4,
                4, 4, 4
        });
        addRecipe(new ItemStack(Material.SAND),  new ItemStack[] {
                new ItemStack(Material.AIR), new ItemStack(Material.QUARTZ), Materials.CRUSHED_MAGNESIUM,
                Materials.CRUSHED_GOLD, Materials.CRUSHED_SILVER, Materials.CRUSHED_COPPER,
                new ItemStack(Material.IRON_NUGGET)
        }, new float[] {
                60, 5, 8,
                5, 8, 5,
                9
        });
        addRecipe(Materials.NETHER_GRAVEL,  new ItemStack[] {
                new ItemStack(Material.AIR), new ItemStack(Material.GOLD_NUGGET), Materials.CRUSHED_GOLD,
                SlimefunItems.SULFATE
        }, new float[] {
                70, 10, 10, 10
        });
        addRecipe(Materials.END_GRAVEL,  new ItemStack[] {
                new ItemStack(Material.AIR), Materials.CRUSHED_COBALT, Materials.CRUSHED_PLATINUM
        }, new float[] {
                94, 5, 1
        });
        addRecipe(Materials.DUST,  new ItemStack[] {
                new ItemStack(Material.AIR), Materials.CRUSHED_COPPER, Materials.CRUSHED_MAGNESIUM,
                Materials.CRUSHED_ALUMINUM, Materials.CRUSHED_TIN,
        }, new float[] {
                80, 5, 5,
                5, 5
        });
        addRecipe(Materials.CRUSHED_ANDESITE,  new ItemStack[] {
                new ItemStack(Material.AIR), Materials.CRUSHED_NICKEL,
                Materials.CRUSHED_IRON, Materials.CRUSHED_COPPER, Materials.CRUSHED_ZINC,
                Materials.CRUSHED_ALUMINUM, Materials.CRUSHED_LEAD, Materials.CRUSHED_TIN

        }, new float[] {
                64, 5,
                5, 5, 4,
                4, 4, 4

        });
        addRecipe(Materials.CRUSHED_DIORITE, new ItemStack[] {
                new ItemStack(Material.AIR),
                Materials.CRUSHED_IRON, Materials.CRUSHED_COPPER, Materials.CRUSHED_ZINC,
                Materials.CRUSHED_ALUMINUM, Materials.CRUSHED_LEAD, Materials.CRUSHED_TIN

        }, new float[] {
                74,
                5, 5, 4,
                4, 4, 4

        });
        addRecipe(Materials.CRUSHED_ANDESITE,  new ItemStack[] {
                new ItemStack(Material.AIR),
                Materials.CRUSHED_IRON, Materials.CRUSHED_COPPER, Materials.CRUSHED_ZINC,
                Materials.CRUSHED_ALUMINUM, Materials.CRUSHED_LEAD, Materials.CRUSHED_TIN

        }, new float[] {
                74,
                5, 5, 4,
                4, 4, 4

        });
        addRecipe(new ItemStack(Material.DIRT),  new ItemStack[] {
                new ItemStack(Material.AIR),
        }, new float[] {
                100
        });
        addRecipe(new ItemStack(Material.SOUL_SAND),  new ItemStack[] {
                new ItemStack(Material.AIR),
        }, new float[] {
                100
        });
    }

    public ManualSieve(Category category) {
        super(category, ITEM, new ItemStack[] {
            null, null, null,
            null, new ItemStack(Material.OAK_TRAPDOOR), null,
            null, new ItemStack(Material.COMPOSTER), null
        }, BlockFace.SELF);
    }

    // use AIR for no output
    private static void addRecipe(ItemStack item, ItemStack[] stacks, float[] chances) {
        RECIPES.put(item, stacks, chances);
        for (int i = 0 ; i < stacks.length ; i ++) {
            if (stacks[i].getType() != Material.AIR) {
                DISPLAY_RECIPES.add(item);
                ItemStack chance = stacks[i].clone();
                LoreUtils.addLore(chance, "", ChatColor.GOLD + "Chance: " + ChatColor.YELLOW + chances[i] + "%");
                DISPLAY_RECIPES.add(chance);
            }
        }
    }

    /**
     * returns null if invalid input, or air depending on chances
     */
    @Nullable
    public static ItemStack getOutput(@Nullable ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return null;
        }
        return RECIPES.get(item);
    }

    @Nonnull
    @Override
    public List<ItemStack> getDisplayRecipes() {
        return DISPLAY_RECIPES;
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
                    PluginUtils.runSync(() -> {
                        outputChest.addItem(output);
                        p.playSound(b.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1);
                    }, 40);
                } else {
                    PluginUtils.runSync(() -> {
                        b.getWorld().dropItemNaturally(b.getLocation().add(0, .5, 0), output);
                        p.playSound(b.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1);
                    }, 40);
                }
            }
        }
    }

}
