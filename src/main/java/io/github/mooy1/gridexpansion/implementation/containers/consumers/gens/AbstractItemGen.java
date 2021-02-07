package io.github.mooy1.gridexpansion.implementation.containers.consumers.gens;

import io.github.mooy1.gridexpansion.implementation.containers.consumers.AbstractGridConsumer;
import io.github.mooy1.gridexpansion.implementation.upgrades.UpgradeType;
import io.github.mooy1.gridexpansion.utils.GridLorePreset;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.Locale;

public abstract class AbstractItemGen extends AbstractGridConsumer {
    
    private final Material material;
    private static final int[] outputs = {4};
    
    public AbstractItemGen(SlimefunItemStack item, Material material, ItemStack[] recipe) {
        super(item, recipe);
        this.material = material;
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
    protected int getStatusSlot() {
        return 5;
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
    
    @Override
    protected void onBreak(@Nonnull BlockBreakEvent e, @Nonnull BlockMenu menu) {
        super.onBreak(e, menu);
        menu.dropItems(e.getBlock().getLocation(), outputs);
    }

    @Override
    protected void process(@Nonnull Block b, @Nonnull BlockMenu menu, @Nonnull Config config, int tier) {
        ItemStack output = new ItemStack(this.material, 1 << tier);
        if (menu.fits(output, outputs)) {
            menu.pushItem(output, outputs);
        }
    }

    @Override
    protected void setupMenu(@Nonnull BlockMenuPreset blockMenuPreset) {
        for (int i = 0 ; i < 9 ; i++) {
            if (i == 4) i++;
            blockMenuPreset.addItem(i, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }
    }

    @Nonnull
    @Override
    protected int[] getTransportSlots(@Nonnull DirtyChestMenu dirtyChestMenu, @Nonnull ItemTransportFlow itemTransportFlow, ItemStack itemStack) {
        if (itemTransportFlow == ItemTransportFlow.WITHDRAW) {
            return outputs;
        } 
        return new int[0];
    }

}
