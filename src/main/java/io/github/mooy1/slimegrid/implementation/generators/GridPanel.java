package io.github.mooy1.slimegrid.implementation.generators;

import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockUseHandler;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class GridPanel extends AbstractGridGenerator {

    public static final int I_DAY = 1;
    public static final int I_NIGHT = 0;
    public static final int II_DAY = 2;
    public static final int II_NIGHT = 1;
    public static final int III_DAY = 6;
    public static final int III_NIGHT = 2;
    public static final int IV_DAY = 16;
    public static final int IV_NIGHT = 4;
    public static final int V_DAY = 50;
    public static final int V_NIGHT = 20;
    public static final int VI_DAY = 120;
    public static final int VI_NIGHT = 60;
    public static final int VII_DAY = 240;
    public static final int VII_NIGHT = 240;
    
    private final int day;
    private final int night;

    public GridPanel(int day, int night, SlimefunItemStack item, ItemStack[] recipe) {
        super(item, recipe, 4);
        this.day = day;
        this.night = night;
        
        addItemHandler((BlockUseHandler) PlayerRightClickEvent::cancel);
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
