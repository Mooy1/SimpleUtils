package io.github.mooy1.gridfoundation.implementation.consumers.machines;

import io.github.mooy1.gridfoundation.implementation.blocks.ManualSieve;
import io.github.mooy1.gridfoundation.implementation.grid.Grid;
import io.github.mooy1.infinitylib.presets.MenuPreset;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public final class AutoSieve extends AbstractProcessor {

    public static final SlimefunItemStack ITEM = make(4, "Auto Sieve", "&7Sifts gravel, sand, and crushed materials into dusts", Material.BROWN_CONCRETE);
    
    private static final int inputSlot = MenuPreset.slot2;
    
    public AutoSieve() {
        super(ITEM, ManualSieve.displayRecipes, Material.COMPOSTER, 4, 3, 15, 31, new int[] {37, 38, 39, 40, 41, 42, 43}, new ItemStack[] {
                
        });
    }

    @Override
    public void onBreak(@Nonnull BlockBreakEvent e, @Nonnull Location l, @Nonnull BlockMenu menu, @Nonnull Grid grid) {
        super.onBreak(e, l, menu, grid);
        menu.dropItems(l, inputSlot);
        menu.dropItems(l, this.outputSlots);
    }
    
    @Override
    protected ItemStack getOutput(BlockMenu menu, int max) {
        ItemStack output = ManualSieve.getOutput(menu.getItemInSlot(inputSlot));
        if (output != null) {
            int amount = Math.min(output.getAmount(), max);
            menu.consumeItem(inputSlot, amount);
            output = output.clone();
            output.setAmount(output.getAmount() * amount);
        }
        return output;
    }

    @Override
    public int[] getTransportSlots(@Nonnull ItemTransportFlow flow) {
        if (flow == ItemTransportFlow.INSERT) {
            return new int[] {inputSlot};
        }
        if (flow == ItemTransportFlow.WITHDRAW) {
            return this.outputSlots;
        }
        return new int[0];
    }
    
    @Override
    public int getUpgradeSlot() {
        return 11;
    }

    private static final int[] background = {
            0, 1, 2, 6, 7, 8,
            9, 10, 11, 15, 16, 17,
            18, 19, 20, 24, 25, 26,
            27, 28, 29, 30, 32, 33, 34, 35
    };
    private static final int[] outputBorder = {
            36, 37, 38, 39, 40, 41, 42, 43, 44,
            45, 53
    };
    
    @Override
    public void setupInv(@Nonnull BlockMenuPreset blockMenuPreset) {
        super.setupInv(blockMenuPreset);
        for (int i : background) {
            blockMenuPreset.addItem(i, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }
        for (int i : outputBorder) {
            blockMenuPreset.addItem(i, MenuPreset.borderItemOutput, ChestMenuUtils.getEmptyClickHandler());
        }
        for (int i : MenuPreset.slotChunk2) {
            blockMenuPreset.addItem(i, MenuPreset.borderItemInput, ChestMenuUtils.getEmptyClickHandler());
        }
    }

    

}
