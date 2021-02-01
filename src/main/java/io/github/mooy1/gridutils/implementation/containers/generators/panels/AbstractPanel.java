package io.github.mooy1.gridutils.implementation.containers.generators.panels;

import io.github.mooy1.gridutils.implementation.containers.generators.AbstractGridGenerator;
import io.github.mooy1.gridutils.implementation.upgrades.UpgradeType;
import io.github.mooy1.gridutils.utils.GridLorePreset;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;
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
        super(item, recipe);
        this.gen = gen;
        this.day = day;
    }

    @Override
    protected final void setupMenu(@Nonnull BlockMenuPreset blockMenuPreset) {
        for (int i = 0 ; i < 9 ; i ++) {
            blockMenuPreset.addItem(i, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }
    }

    @Nonnull
    @Override
    protected final int[] getTransportSlots(@Nonnull DirtyChestMenu dirtyChestMenu, @Nonnull ItemTransportFlow itemTransportFlow, ItemStack itemStack) {
        return new int[0];
    }

    @Override
    protected final int generate(BlockMenu menu, Block b, Config config, int tier) {
        if (this.day == (b.getWorld().getEnvironment() == World.Environment.NORMAL
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
    public final UpgradeType getMaxLevel() {
        return UpgradeType.ULTIMATE;
    }

    @Override
    public final int getUpgradeSlot() {
        return 3;
    }

    @Override
    protected final void onNewInstance(@Nonnull BlockMenu menu, @Nonnull Block b) {
        super.onNewInstance(menu, b);
        BlockData data = b.getBlockData();
        if (data instanceof DaylightDetector) {
            DaylightDetector detector = (DaylightDetector) data;
            detector.setInverted(!this.day);
            b.setBlockData(detector);
        }
    }
    
    static SlimefunItemStack make(int gp, @Nonnull String name, @Nonnull String source) {
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
