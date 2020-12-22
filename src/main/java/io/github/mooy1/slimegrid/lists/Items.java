package io.github.mooy1.slimegrid.lists;

import io.github.mooy1.infinitylib.presets.LorePreset;
import io.github.mooy1.slimegrid.SlimeGrid;
import io.github.mooy1.slimegrid.implementation.generators.GridItemGenerator;
import io.github.mooy1.slimegrid.implementation.generators.GridPanel;
import io.github.mooy1.slimegrid.utils.GridLorePreset;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Material;

import java.util.Locale;

public final class Items {

    public static final SlimefunItemStack ADDON_INFO = new SlimefunItemStack(
            SlimeGrid.getInstance().getName().toUpperCase(Locale.ROOT) + "_ADDON_INFO",
            Material.NETHER_STAR,
            "&bAddon Info",
            "&fVersion: &7" + SlimeGrid.getInstance().getPluginVersion(),
            "",
            "&fDiscord: &b@&7Riley&8#5911",
            "&7discord.gg/slimefun",
            "",
            "&fGithub: &b@&8&7Mooy1",
            "&7" + SlimeGrid.getInstance().getBugTrackerURL()
    );

    public static final SlimefunItemStack GRID_PANEL_I = makeGridPanel("I", GridPanel.I_DAY, GridPanel.I_NIGHT); // .5
    public static final SlimefunItemStack GRID_PANEL_II = makeGridPanel("II", GridPanel.II_DAY, GridPanel.II_NIGHT); // 1.5
    public static final SlimefunItemStack GRID_PANEL_III = makeGridPanel("III", GridPanel.III_DAY, GridPanel.III_NIGHT); // 4
    public static final SlimefunItemStack GRID_PANEL_IV = makeGridPanel("IV", GridPanel.IV_DAY, GridPanel.IV_NIGHT); // 10
    public static final SlimefunItemStack GRID_PANEL_V = makeGridPanel("V", GridPanel.V_DAY, GridPanel.V_NIGHT); // 35
    public static final SlimefunItemStack GRID_PANEL_VI = makeGridPanel("VI", GridPanel.VI_DAY, GridPanel.VI_NIGHT); // 90
    public static final SlimefunItemStack GRID_PANEL_VII = makeGridPanel("VII", GridPanel.VII_DAY, GridPanel.VII_NIGHT); // 240

    public static final SlimefunItemStack COBBLE_GEN_I = makeGridItemGenerator("Cobble", "I", GridItemGenerator.COBBLE_I, GridItemGenerator.COBBLE_I);
    public static final SlimefunItemStack COBBLE_GEN_II = makeGridItemGenerator("Cobble", "II", GridItemGenerator.COBBLE_II, GridItemGenerator.COBBLE_II);
    public static final SlimefunItemStack COBBLE_GEN_III = makeGridItemGenerator("Cobble", "III", GridItemGenerator.COBBLE_III, GridItemGenerator.COBBLE_III);
    public static final SlimefunItemStack COBBLE_GEN_IV = makeGridItemGenerator("Cobble", "IV", GridItemGenerator.COBBLE_IV, GridItemGenerator.COBBLE_IV);
    
    private static SlimefunItemStack makeGridPanel(String tier, int day, int night) {
        return new SlimefunItemStack(
                "GRID_PANEL_" + tier.toUpperCase(Locale.ROOT),
                Material.DAYLIGHT_DETECTOR,
                "&eSolar Panel &6" + tier,
                "&7Generates GP from sunlight",
                "",
                GridLorePreset.generatesGridPower(day) + "Daytime",
                GridLorePreset.generatesGridPower(night) + "Nighttime"
        );
    }

    private static SlimefunItemStack makeGridItemGenerator(String type, String tier, int power, int speed) {
        return new SlimefunItemStack(
                 type.toUpperCase(Locale.ROOT) + "_GENERATOR_" + tier.toUpperCase(Locale.ROOT),
                Material.GRAY_CONCRETE,
                "&e" + type + " Generator &6" + tier,
                "&7Generates " + type + " from GP",
                "",
                GridLorePreset.consumesGridPower(power),
                LorePreset.speed(speed)
        );
    }
    
    private static SlimefunItemStack makeCircuit(String tier) {
        return new SlimefunItemStack(
                "GRID_CIRCUIT_" + tier, 
                Material.HONEYCOMB,
                "&eGrid Circuit &6" + tier,
                "&7Core Component of grid machines"
                
        );
    }
    
    
}
