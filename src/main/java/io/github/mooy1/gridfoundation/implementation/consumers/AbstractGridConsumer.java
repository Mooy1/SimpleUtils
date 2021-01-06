package io.github.mooy1.gridfoundation.implementation.consumers;

import io.github.mooy1.gridfoundation.implementation.AbstractGridContainer;
import io.github.mooy1.gridfoundation.implementation.grid.Consumer;
import io.github.mooy1.gridfoundation.implementation.grid.Grid;
import io.github.mooy1.gridfoundation.implementation.upgrades.UpgradeType;
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
        addTier(item);
    }

    @Override
    public void onNewInstance(@Nonnull BlockMenu menu, @Nonnull Block b, @Nonnull Grid grid) {
        menu.replaceExistingItem(this.statusSlot, grid.addConsumer(b.getLocation(), getItem(), getUpgrade(b).getLevel()).getStatusItem());
        updateMenuUpgrade(menu, b.getLocation());
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void onBreak(@Nonnull BlockBreakEvent e, @Nonnull Location l, @Nonnull BlockMenu menu, @Nonnull Grid grid) {
        breakUpgrade(e, e.getBlock().getLocation(), getItem().clone());
        grid.removeConsumer(e.getBlock().getLocation());
    }

    @Override
    public void onPlace(@Nonnull BlockPlaceEvent e) {
        placeUpgrade(e.getBlockPlaced().getLocation(), e.getItemInHand());
    }

    @Override
    public final void tick(@Nonnull Block block, @Nonnull BlockMenu blockMenu, @Nonnull Grid grid) {
        Consumer consumer = grid.getConsumer(block.getLocation());
        if (consumer != null) {
            UpgradeType type = getUpgrade(block);
            consumer.setConsumption(this.consumption * Math.max(1, type.getLevel() / 2));
            if (consumer.canConsume()) {
                process(blockMenu, block, type);
            }
            consumer.updateStatus(blockMenu, this.statusSlot);
        }
    }
    
    public abstract void process(@Nonnull BlockMenu menu, @Nonnull Block b, @Nonnull UpgradeType type);

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
    public void getStats(@Nonnull List<String> stats, int level) {
        stats.add("&6Usage: &e" + Math.max(1, level / 2) + "x");
        stats.add("&6Speed: &e" + level + "x");
    }

}
