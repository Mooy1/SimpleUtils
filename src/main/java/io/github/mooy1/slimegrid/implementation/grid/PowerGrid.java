package io.github.mooy1.slimegrid.implementation.grid;

import io.github.mooy1.infinitylib.PluginUtils;
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

    public static void start() {
        PluginUtils.scheduleRepeatingSync(() -> {
            for (PowerGrid entry : grids.values()) {
                entry.generate();
            }
        }, 100, PluginUtils.TICKER_DELAY);
    }

    public static PowerGrid get(@Nonnull String playerName) {
        return grids.computeIfAbsent(playerName, k -> new PowerGrid(playerName));
    }

    @Getter
    private final Set<GridGenerator> generators = new HashSet<>();
    
    private final String playerName;
    
    private int current = 0;
    private int remaining = 0;
    private int total = 0;
    private boolean maxed = false;
    
    private PowerGrid(String playerName) {
        this.playerName = playerName;
    }
    
    public void generate() {
        this.remaining = this.current;
        
        int max = 0;
        for (GridGenerator generator : this.generators) {
            max += generator.getGeneration();
        }
        
        this.total = max;
        this.maxed = false;
        this.current = max;
    }
    
    public boolean consume(int amount, boolean consume) {
        if (this.current >= amount) {
            if (consume) {
                this.current -= amount;
            }
            return true;
        }
        this.maxed = true;
        return false;
    }
    
    @Nonnull
    public ItemStack getStatusItem(boolean generator, int amount) {
        return new CustomItem(
                this.maxed ? Material.RED_STAINED_GLASS_PANE : this.remaining == 0 ? Material.YELLOW_STAINED_GLASS_PANE : Material.LIME_STAINED_GLASS_PANE,
                "&a" + (generator ? "Generating" : "Consuming") + ": " + amount,
                "&7Grid: " + this.remaining + " / " + this.total,
                "&7Owner: " + this.playerName
        );
    }
    
}
