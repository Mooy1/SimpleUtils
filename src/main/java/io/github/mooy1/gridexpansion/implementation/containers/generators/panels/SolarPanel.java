package io.github.mooy1.gridexpansion.implementation.containers.generators.panels;


import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.inventory.ItemStack;

public final class SolarPanel extends AbstractPanel {

    public static final SlimefunItemStack ITEM = make(1, "Solar", "Sunlight");

    public SolarPanel() {
        super(true, 1, ITEM, new ItemStack[] {

        });
    }

}