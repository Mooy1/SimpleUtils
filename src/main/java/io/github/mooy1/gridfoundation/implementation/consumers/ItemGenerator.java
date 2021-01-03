package io.github.mooy1.gridfoundation.implementation.consumers;

import io.github.mooy1.gridfoundation.implementation.grid.PowerGrid;
import io.github.mooy1.gridfoundation.implementation.upgrades.UpgradeManager;
import io.github.mooy1.gridfoundation.implementation.upgrades.UpgradeType;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public final class ItemGenerator extends AbstractGridConsumer {

    private final Material material;

    public ItemGenerator(SlimefunItemStack item, int consumption, Material material, ItemStack[] recipe) {
        super(item, recipe, 4, consumption);
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
    public void onBreak(Player p, Block b,  BlockMenu menu, PowerGrid grid) {
        menu.dropItems(b.getLocation(), 13);
    }

    @Override
    public boolean process(@Nonnull BlockMenu menu, @Nonnull Block b) {
        ItemStack output = new ItemStack(this.material, UpgradeManager.getLevel(b));
        if (menu.fits(output, 13)) {
            menu.pushItem(output, 13);
            return true;
        }
        return false;
    }

    @Nonnull
    @Override
    public UpgradeType getMaxLevel() {
        return UpgradeType.ULTIMATE;
    }

    @Override
    public int getUpgradeSlot() {
        return 0;
    }

}
