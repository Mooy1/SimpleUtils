package io.github.mooy1.slimegrid.implementation.generators;

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

    public static final int I_DAY = 1;
    public static final int I_NIGHT = 0; //.5
    public static final int II_DAY = 2;
    public static final int II_NIGHT = 0; //1
    public static final int III_DAY = 5;
    public static final int III_NIGHT = 1; //3
    public static final int IV_DAY = 15;
    public static final int IV_NIGHT = 3; //9
    public static final int V_DAY = 54;
    public static final int V_NIGHT = 18; //36
    public static final int VI_DAY = 192;
    public static final int VI_NIGHT = 96; //144
    public static final int VII_DAY = 720;
    public static final int VII_NIGHT = 720; //720
    
    private final int day;
    private final int night;

    public SolarPanel(int day, int night, SlimefunItemStack item, ItemStack[] recipe) {
        super(item, recipe, 4);
        this.day = day;
        this.night = night;
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
        return b.getWorld().getEnvironment() == World.Environment.NORMAL && b.getWorld().getTime() < 13000 && b.getLocation().add(0, 1, 0).getBlock().getLightFromSky() == 15 ? this.day : this.night;
    }

}
