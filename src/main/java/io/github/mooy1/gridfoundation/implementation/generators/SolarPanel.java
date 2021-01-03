package io.github.mooy1.gridfoundation.implementation.generators;

import io.github.mooy1.gridfoundation.implementation.upgrades.UpgradeType;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public final class SolarPanel extends AbstractGridGenerator {
    
    private final int day;
    private final int night;
    private final boolean always;
    private final UpgradeType max;

    public SolarPanel(int day, int night, SlimefunItemStack item, ItemStack[] recipe) {
        super(item, recipe, 4);
        this.day = day;
        this.night = night;
        this.always = day == night;
        if (this.always) {
            this.max = UpgradeType.ULTIMATE;
        } else {
            this.max = UpgradeType.REINFORCED;
        }
    }
    
    @Override
    public void setupInv(@Nonnull BlockMenuPreset blockMenuPreset) {
        for (int i = 0 ; i < 9 ; i ++) {
            blockMenuPreset.addItem(i, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }
    }

    @Override
    public int[] getTransportSlots(@Nonnull ItemTransportFlow itemTransportFlow) {
        return new int[0];
    }

    @Override
    public int getGeneration(@Nonnull BlockMenu menu, @Nonnull Block b) {
        if (this.always) {
            return this.day;
        }
        return b.getWorld().getEnvironment() == World.Environment.NORMAL && b.getWorld().getTime() < 13000 && b.getLocation().add(0, 1, 0).getBlock().getLightFromSky() == 15 ? this.day : this.night;
    }

    @Nonnull
    @Override
    public UpgradeType getMaxLevel() {
        return this.max;
    }

    @Override
    public int getUpgradeSlot() {
        return 0;
    }

}
