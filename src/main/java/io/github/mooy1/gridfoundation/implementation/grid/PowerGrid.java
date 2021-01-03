package io.github.mooy1.gridfoundation.implementation.grid;

import lombok.Getter;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class PowerGrid {

    private static final Map<String, PowerGrid> grids = new HashMap<>();
    
    @Getter
    private final Set<GridGenerator> generators = new HashSet<>();
    
    private final String playerName;

    private int usage = 0;
    private int max = 0;
    private boolean maxed = false;

    public static void tick() {
        for (PowerGrid grid : grids.values()) {
            grid.generate();
        }
    }

    public static PowerGrid get(@Nonnull String playerName) {
        return grids.computeIfAbsent(playerName, k -> new PowerGrid(playerName));
    }
    
    private PowerGrid(String playerName) {
        this.playerName = playerName;
    }
    
    public void generate() {
        this.usage = 0;
        this.max = 0;
        this.maxed = false;

        for (GridGenerator generator : this.generators) {
            this.max += generator.getGeneration();
        }
    }
    
    public boolean consume(int amount, boolean consume) {
        if (this.max >= this.usage + amount) {
            if (consume) {
                this.usage += amount;
            }
            return true;
        }
        this.maxed = true;
        return false;
    }
    
    @Nonnull
    public ItemStack getStatusItem(boolean generator, int amount) {
        return new CustomItem(
                this.maxed ? Material.RED_STAINED_GLASS_PANE : this.usage == this.max ? Material.YELLOW_STAINED_GLASS_PANE : Material.LIME_STAINED_GLASS_PANE,
                "&a" + (generator ? "Generating" : "Consuming") + ": " + amount + " GP",
                "&7Grid: " + this.usage + " / " + this.max,
                "&7Owner: " + this.playerName
        );
    }
    
}
