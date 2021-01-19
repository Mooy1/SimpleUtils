package io.github.mooy1.gridfoundation.implementation.consumers;

import io.github.mooy1.gridfoundation.implementation.AbstractGridContainer;
import io.github.mooy1.gridfoundation.implementation.powergrid.GPConsumer;
import io.github.mooy1.gridfoundation.implementation.powergrid.PowerGrid;
import io.github.mooy1.gridfoundation.implementation.upgrades.UpgradeableBlock;
import io.github.mooy1.gridfoundation.setup.Categories;
import io.github.mooy1.gridfoundation.utils.GridLorePreset;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.List;
import java.util.Locale;

public abstract class AbstractGridConsumer extends AbstractGridContainer implements UpgradeableBlock {

    private final int consumption;
    private final int statusSlot;
    
    public AbstractGridConsumer(SlimefunItemStack item, ItemStack[] recipe, int statusSlot, int consumption) {
        super(Categories.MACHINES, item, RecipeType.ENHANCED_CRAFTING_TABLE, recipe);
        this.statusSlot = statusSlot;
        this.consumption = consumption;
        addMeta(item);
    }

    @Override
    public void onNewInstance(@Nonnull BlockMenu menu, @Nonnull Block b, @Nonnull PowerGrid grid) {
        grid.addConsumer(b.getLocation().hashCode(), getItem(), this.consumption << getTier(b), b.getLocation()).updateStatus(menu, this.statusSlot);
        updateMenuUpgrade(menu, b.getLocation());
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void onBreak(@Nonnull BlockBreakEvent e, @Nonnull Location l, @Nonnull BlockMenu menu, @Nonnull PowerGrid grid) {
        breakUpgrade(e, e.getBlock().getLocation(), getItem().clone());
        grid.removeConsumer(e.getBlock().getLocation().hashCode());
    }

    @Override
    public void onPlace(@Nonnull BlockPlaceEvent e) {
        placeUpgrade(e.getBlockPlaced().getLocation(), e.getItemInHand());
    }

    @Override
    public final void tick(@Nonnull Block block, @Nonnull BlockMenu blockMenu, @Nonnull PowerGrid grid) {
        GPConsumer consumer = grid.getConsumer(block.getLocation().hashCode());
        if (consumer != null) {
            int tier = getTier(block);
            consumer.setConsumption(this.consumption << Math.max(0, tier - 1));
            if (consumer.canConsume()) {
                process(blockMenu, block, tier);
            }
            if (blockMenu.hasViewer()) {
                consumer.updateStatus(blockMenu, this.statusSlot);
            }
        }
    }
    
    public abstract void process(@Nonnull BlockMenu menu, @Nonnull Block b, int tier);

    protected static SlimefunItemStack make(int power, String name, String desc, Material material) {
        return new SlimefunItemStack(
                "GRID_" + name.replace(" ", "_").toUpperCase(Locale.ROOT),
                material,
                "&e" + name,
                "&7" + desc,
                "",
                GridLorePreset.consumesGridPower(power)
        );
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void getStats(@Nonnull List<String> stats, int tier) {
        stats.add("&6Usage: &e" +  (1 << Math.max(0, tier - 1)) + "x");
    } // TODO add speed for consumers

}
