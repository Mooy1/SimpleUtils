package io.github.mooy1.slimegrid.implementation.consumers;

import io.github.mooy1.slimegrid.implementation.grid.PowerGrid;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public final class ItemGenerator extends AbstractGridConsumer {

    public static final int COBBLE_I = 1;
    public static final int COBBLE_II = 4;
    public static final int COBBLE_III = 16;
    public static final int COBBLE_IV = 64;
    
    private final int speed;
    private final Material material;

    public ItemGenerator(SlimefunItemStack item, int consumption, int speed, Material material, ItemStack[] recipe) {
        super(item, recipe, 4, consumption);
        this.speed = speed;
        this.material = material;
    }

    @Override
    public void setupInv(@Nonnull BlockMenuPreset blockMenuPreset) {
        for (int i = 0 ; i < 18 ; i++) {
            if (i == 13) i++;
            blockMenuPreset.addItem(i, ChestMenuUtils.getBackground(), ChestMenuUtils.getEmptyClickHandler());
        }
    }

    @Override
    public void onBreak(Player p, Block b, Location l, BlockMenu menu, PowerGrid grid) {
        menu.dropItems(b.getLocation(), 13);
    }

    @Override
    public boolean process(@Nonnull BlockMenu menu, @Nonnull Block b) {
        ItemStack output = new ItemStack(this.material, this.speed);
        if (menu.fits(output, 13)) {
            menu.pushItem(output, 13);
            return true;
        }
        return false;
    }

}
