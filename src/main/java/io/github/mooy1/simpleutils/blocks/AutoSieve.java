package io.github.mooy1.simpleutils.blocks;

import io.github.mooy1.infinitylib.PluginUtils;
import io.github.mooy1.infinitylib.abstracts.AbstractMachine;
import io.github.mooy1.infinitylib.presets.LorePreset;
import io.github.mooy1.infinitylib.presets.MenuPreset;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

public final class AutoSieve extends AbstractMachine implements RecipeDisplayItem {
    
    private static final int INPUT = MenuPreset.slot1;
    private static final int[] OUTPUT = {14, 15, 16};
    private static final int[] INPUT_BORDER = MenuPreset.slotChunk1;
    private static final int[] OUTPUT_BORDER = {4, 5, 6, 7, 8, 13, 17, 22, 23, 24, 25, 26};
    private static final int[] BACKGROUND = {3, 21};
    private static final ItemStack SIEVING = new CustomItem(Material.LIME_STAINED_GLASS_PANE, "&aSieving...");
    
    private final int ticks;
    
    public AutoSieve(Category category, String tier, int ticks, int energy, ItemStack[] recipe) {
        super(category, new SlimefunItemStack(
                "AUTO_SIEVE_" + tier,
                Material.BROWN_CONCRETE,
                "&6Auto Sieve &e" + tier,
                "&7Automatically sieves gravel, sand, and crushed materials",
                "",
                LorePreset.speed(9 / ticks),
                LorePreset.energyPerSecond(energy)
        ), RecipeType.ENHANCED_CRAFTING_TABLE, recipe, 12, energy);
        this.ticks = ticks;
    }

    @Override
    protected boolean process(@Nonnull BlockMenu blockMenu, @Nonnull Block block, @Nonnull Config config) {
        if (PluginUtils.getCurrentTick() % this.ticks != 0) {
            return true;
        }
        ItemStack input = blockMenu.getItemInSlot(INPUT);
        ItemStack output = ManualSieve.getOutput(input);
        if (output == null) {
            return false;
        }
        input.setAmount(input.getAmount() - 1);
        if (output.getType() != Material.AIR) {
            if (blockMenu.fits(output, OUTPUT)) {
                blockMenu.pushItem(output.clone(), OUTPUT);
            } else {
                return false;
            }
        }
        if (blockMenu.hasViewer()) {
            blockMenu.replaceExistingItem(this.statusSlot, SIEVING);
        }
        return true;
    }

    @Override
    protected void setupMenu(@Nonnull BlockMenuPreset blockMenuPreset) {
        super.setupMenu(blockMenuPreset);
        blockMenuPreset.drawBackground(BACKGROUND);
        blockMenuPreset.drawBackground(MenuPreset.borderItemInput, INPUT_BORDER);
        blockMenuPreset.drawBackground(MenuPreset.borderItemOutput, OUTPUT_BORDER);
    }

    @Nonnull
    @Override
    protected int[] getTransportSlots(@Nonnull DirtyChestMenu dirtyChestMenu, @Nonnull ItemTransportFlow itemTransportFlow, ItemStack itemStack) {
        if (itemTransportFlow == ItemTransportFlow.WITHDRAW) {
            return OUTPUT;
        }
        if (itemTransportFlow == ItemTransportFlow.INSERT) {
            return new int[] {INPUT};
        }
        return new int[0];
    }

    @Override
    public int getCapacity() {
        return this.energy * 2;
    }

    @Nonnull
    @Override
    public List<ItemStack> getDisplayRecipes() {
        return ManualSieve.DISPLAY_RECIPES;
    }

}
