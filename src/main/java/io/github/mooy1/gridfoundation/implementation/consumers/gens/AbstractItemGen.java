package io.github.mooy1.gridfoundation.implementation.consumers.gens;

import io.github.mooy1.gridfoundation.implementation.consumers.AbstractGridConsumer;
import io.github.mooy1.gridfoundation.implementation.powergrid.PowerGrid;
import io.github.mooy1.gridfoundation.implementation.upgrades.UpgradeType;
import io.github.mooy1.gridfoundation.utils.GridLorePreset;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.Locale;

public abstract class AbstractItemGen extends AbstractGridConsumer {
    
    private final Material material;
    private static final int[] outputs = {4};
    
    public AbstractItemGen(SlimefunItemStack item, int consumption, Material material, ItemStack[] recipe) {
        super(item, recipe, 5, consumption);
        this.material = material;
    }

    @Override
    public void setupInv(@Nonnull BlockMenuPreset blockMenuPreset) {
        for (int i = 0 ; i < 9 ; i++) {
            if (i == 4) i++;
            blockMenuPreset.addItem(i, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }
    }

    @Override
    public void onBreak(@Nonnull BlockBreakEvent e, @Nonnull Location l, @Nonnull BlockMenu menu, @Nonnull PowerGrid grid) {
        super.onBreak(e, l, menu, grid);
        menu.dropItems(l, outputs);
    }

    @Override
    public void process(@Nonnull BlockMenu menu, @Nonnull Block b, int tier) {
        ItemStack output = new ItemStack(this.material, 1 << tier);
        if (menu.fits(output, outputs)) {
            menu.pushItem(output, outputs);
        }
    }

    @Override
    public int[] getTransportSlots(@Nonnull ItemTransportFlow flow) {
        return flow == ItemTransportFlow.WITHDRAW ? outputs : new int[0];
    }

    @Nonnull
    @Override
    public UpgradeType getMaxLevel() {
        return UpgradeType.ULTIMATE;
    }

    @Override
    public int getUpgradeSlot() {
        return 3;
    }

    static SlimefunItemStack make(String type, int power, Material material) {
        return new SlimefunItemStack(
                type.toUpperCase(Locale.ROOT) + "GEN_GRID",
                material,
                "&e" + type + "gen",
                "&7Generates " + type + " from GP",
                "",
                GridLorePreset.consumesGridPower(power)
        );
    }

}
