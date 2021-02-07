package io.github.mooy1.gridexpansion.implementation.containers.consumers;

import io.github.mooy1.gridexpansion.implementation.containers.AbstractGridContainer;
import io.github.mooy1.gridexpansion.implementation.powergrid.GridConsumer;
import io.github.mooy1.gridexpansion.implementation.powergrid.PowerGrid;
import io.github.mooy1.gridexpansion.setup.Categories;
import io.github.mooy1.gridexpansion.utils.GridLorePreset;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.List;
import java.util.Locale;

public abstract class AbstractGridConsumer extends AbstractGridContainer {
    
    public AbstractGridConsumer(SlimefunItemStack item, ItemStack[] recipe) {
        super(Categories.MACHINES, item, RecipeType.ENHANCED_CRAFTING_TABLE, recipe);
    }

    @Override
    protected final void tick(@Nonnull BlockMenu blockMenu, @Nonnull Block block, @Nonnull Config config) {
        GridConsumer consumer = GridConsumer.get(block.getLocation());
        if (consumer != null) {
            if (consumer.canConsume()) {
                process(block, blockMenu, config, getUpgrade(config));
            }
            if (blockMenu.hasViewer()) {
                blockMenu.replaceExistingItem(getStatusSlot(), consumer.getStatusItem(), false);
            }
        }
    }

    protected abstract void process(@Nonnull Block b, @Nonnull BlockMenu menu, @Nonnull Config config, int tier);
    
    @Override
    @OverridingMethodsMustInvokeSuper
    public void getStats(@Nonnull List<String> stats, int tier) {
        stats.add("&6Usage: &e" +  (1 << Math.max(0, tier - 1)) + "x");
    } // TODO add speed for consumers

    @Override
    protected void onBreak(@Nonnull BlockBreakEvent e, @Nonnull BlockMenu menu) {
        super.onBreak(e, menu);
        GridConsumer.get(e.getBlock().getLocation()).remove(e.getBlock().getLocation());
    }

    @Override
    protected void onNewInstance(@Nonnull BlockMenu menu, @Nonnull Block b) {
        super.onNewInstance(menu, b);
        menu.replaceExistingItem(getStatusSlot(),
                new GridConsumer(menu.getPreset().getSlimefunItem().getItem(), getConsumption(), b, PowerGrid.get(b.getLocation())).getStatusItem()
        );
    }
    
    protected abstract int getConsumption();

    protected static SlimefunItemStack make(int power, @Nonnull String name, @Nonnull String desc, @Nonnull Material material) {
        return new SlimefunItemStack(
                "GRID_" + name.replace(" ", "_").toUpperCase(Locale.ROOT),
                material,
                "&e" + name,
                "&7" + desc,
                "",
                GridLorePreset.consumesGridPower(power)
        );
    }
}
