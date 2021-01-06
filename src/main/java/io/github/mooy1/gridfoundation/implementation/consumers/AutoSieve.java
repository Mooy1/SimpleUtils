package io.github.mooy1.gridfoundation.implementation.consumers;

import io.github.mooy1.gridfoundation.implementation.blocks.ManualSieve;
import io.github.mooy1.gridfoundation.implementation.upgrades.UpgradeType;
import io.github.mooy1.infinitylib.presets.MenuPreset;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

public class AutoSieve extends AbstractGridConsumer {

    public static final SlimefunItemStack ITEM = make(4, "Auto Sieve", "&7Sifts gravel, sand, and crushed materials into dusts", Material.BROWN_CONCRETE);
    
    private static final int inputSlot = MenuPreset.slot2;
    private static final int[] outputSlots = {37, 38, 39, 40, 41, 42, 43};
    
    public AutoSieve() {
        super(ITEM, new ItemStack[] {
                
        }, 15, 4);
    }

    @Override
    public void process(@Nonnull BlockMenu menu, @Nonnull Block b, @Nonnull UpgradeType type) {
        ItemStack input = menu.getItemInSlot(inputSlot);
        if (input == null) {
            return;
        }
        int amount = Math.min(type.getLevel(), input.getAmount());
        
        List<Optional<ItemStack>> outputs = ManualSieve.getOutputs(input, Math.max(1, amount / 8));
        if (outputs == null) {
            return;
        }
        for (int i = 0 ; i < amount ; i++) {
            // TODO BETTER WAY OF OUTPUTTING RANDOMLY
        }
        for (Optional<ItemStack> output : outputs) {
            if (output.isPresent()) {
                ItemStack item = output.get();
                item.setAmount(size * item.getAmount());
                menu.pushItem(item, outputSlots);
            }
        }
        menu.consumeItem(inputSlot, amount);
    }

    @Nonnull
    @Override
    public UpgradeType getMaxLevel() {
        return UpgradeType.ULTIMATE;
    }

    @Override
    public int getUpgradeSlot() {
        return 11;
    }

    private static final int[] background = {
            0, 1, 2, 6, 7, 8,
            9, 10, 11, 15, 16, 17,
            18, 19, 20, 24, 25, 26
    };
    private static final int[] outputBorder = {
            27, 28, 29, 30, 31, 32, 33, 34, 35,
            36, 44,
            45, 46, 47, 48, 49, 50, 51, 52, 53
    };
    
    @Override
    public void setupInv(@Nonnull BlockMenuPreset blockMenuPreset) {
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
