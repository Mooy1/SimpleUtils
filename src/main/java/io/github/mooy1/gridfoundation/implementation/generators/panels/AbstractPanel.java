package io.github.mooy1.gridfoundation.implementation.generators.panels;

import io.github.mooy1.gridfoundation.implementation.generators.AbstractGridGenerator;
import io.github.mooy1.gridfoundation.implementation.powergrid.PowerGrid;
import io.github.mooy1.gridfoundation.implementation.upgrades.UpgradeType;
import io.github.mooy1.gridfoundation.utils.GridLorePreset;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.DaylightDetector;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.Locale;

public abstract class AbstractPanel extends AbstractGridGenerator {

    private final int gen;
    private final boolean day;
    
    public AbstractPanel(boolean day, int gen, SlimefunItemStack item, ItemStack[] recipe) {
        super(item, recipe, 5);
        this.gen = gen;
        this.day = day;
    }

    @Override
    public void setupInv(@Nonnull BlockMenuPreset blockMenuPreset) {
        for (int i = 0 ; i < 9 ; i ++) {
            blockMenuPreset.addItem(i, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }
    }

    @Nonnull
    @Override
    public int[] getTransportSlots(@Nonnull ItemTransportFlow itemTransportFlow) {
        return new int[0];
    }

    @Override
    public int getGeneration(@Nonnull BlockMenu menu, @Nonnull Block b, int tier) {
        if (this.day == (
                b.getWorld().getEnvironment() == World.Environment.NORMAL
                && b.getWorld().getTime() < 13000
                && b.getLocation().add(0, 1, 0).getBlock().getLightFromSky() == 15
        )) {
            return this.gen;
        } else {
            return 0;
        }
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


    @Override
    public void onNewInstance(@Nonnull BlockMenu menu, @Nonnull Block b, @Nonnull PowerGrid grid) {
        super.onNewInstance(menu, b, grid);
        BlockData data = b.getBlockData();
        if (data instanceof DaylightDetector) {
            DaylightDetector detector = (DaylightDetector) data;
            detector.setInverted(!this.day);
            b.setBlockData(detector);
        }
    }
    
    static SlimefunItemStack make(int gp, String name, String source) {
        return new SlimefunItemStack(
                "GRID_PANEL_" + name.toUpperCase(Locale.ROOT),
                Material.DAYLIGHT_DETECTOR,
                "&eGrid " + name + " Panel",
                "&7Generates GP from " + source,
                "",
                GridLorePreset.generatesGridPower(gp)
        );
    }

}
