package io.github.mooy1.gridfoundation.implementation.consumers.machines;

import io.github.mooy1.gridfoundation.implementation.consumers.AbstractGridConsumer;
import io.github.mooy1.gridfoundation.implementation.powergrid.PowerGrid;
import io.github.mooy1.gridfoundation.implementation.upgrades.UpgradeType;
import io.github.mooy1.infinitylib.math.MathUtils;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.cscorelib2.collections.Pair;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import org.apache.commons.lang.mutable.MutableInt;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractProcessor extends AbstractGridConsumer implements RecipeDisplayItem {
    
    private static final Map<Location, Pair<MutableInt, ItemStack>> progressing = new HashMap<>();

    protected final int[] outputSlots;
    private final int progressSlot;
    protected final List<ItemStack> displayRecipes;
    private final int base;
    private final Material material;
    
    public AbstractProcessor(SlimefunItemStack item, List<ItemStack> displayRecipes, Material progressMaterial,
                             int base, int consumption, int statusSlot, int progressSlot, int[] outputSlots, ItemStack[] recipe
    ) {
        super(item, recipe, statusSlot, consumption);
        this.progressSlot = progressSlot;
        this.outputSlots = outputSlots;
        this.material = progressMaterial;
        this.displayRecipes = displayRecipes;
        this.base = MathUtils.floorLog2(base);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void onBreak(@Nonnull BlockBreakEvent e, @Nonnull Location l, @Nonnull BlockMenu menu, @Nonnull PowerGrid grid) {
        super.onBreak(e, l, menu, grid);
        menu.dropItems(l, this.outputSlots);
        progressing.remove(l);
    }

    @Override
    public final void process(@Nonnull BlockMenu menu, @Nonnull Block b, int tier) {
        // get progress from map
        Pair<MutableInt, ItemStack> pair = progressing.computeIfAbsent(b.getLocation(), k -> new Pair<>(new MutableInt(0), null));
        
        // temp store the values from pair
        int progress = pair.getFirstValue().intValue();
        ItemStack item = pair.getSecondValue();
        
        int ticks = 1 << Math.max(0, this.base - tier);
        
        // if not started try to get input and start
        if (progress == 0) {
            ItemStack output = getOutput(menu, 1 << Math.max(0, tier - this.base));
            if (output != null) {
                progress = 1;
                item = output;
            }
        // otherwise increase progress if not done
        } else if (progress < ticks) {
            progress++;
        }

        // update status before outputting so that progress isn't 0
        setStatus(menu, progress, ticks);

        // output if done and fits
        if (progress >= ticks && menu.fits(item, this.outputSlots)) {
            if (item.getType() != Material.AIR) {
                menu.pushItem(item, this.outputSlots);
            }
            item = null;
            progress = 0;
        }
        
        // update pair with new values
        pair.getFirstValue().setValue(progress);
        pair.setSecondValue(item);
        
    }
    
    protected abstract ItemStack getOutput(BlockMenu menu, int max);

    @Override
    @OverridingMethodsMustInvokeSuper
    public void setupInv(@Nonnull BlockMenuPreset blockMenuPreset) {
        blockMenuPreset.addItem(this.progressSlot, getStatus(0, this.base), ChestMenuUtils.getEmptyClickHandler());
    }
    
    private void setStatus(BlockMenu menu, int current, int ticks) {
         if (menu.hasViewer())  {
             menu.replaceExistingItem(this.progressSlot, getStatus(current, ticks), false);
         }
    }
    
    private ItemStack getStatus(int current, int ticks) {
        if (current == 0) {
            return new CustomItem(Material.BARRIER, "&cIdle...");
        } else {
            return new CustomItem(this.material, "&aProcessing... " + current + "/" + ticks);
        }
    }

    @Nonnull
    @Override
    public final UpgradeType getMaxLevel() {
        return UpgradeType.ULTIMATE;
    }

    @Nonnull
    @Override
    public final List<ItemStack> getDisplayRecipes() {
        return this.displayRecipes;
    }

}
