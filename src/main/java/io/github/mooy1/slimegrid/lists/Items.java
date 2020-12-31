package io.github.mooy1.slimegrid.lists;

import io.github.mooy1.infinitylib.presets.LorePreset;
import io.github.mooy1.slimegrid.SlimeGrid;
import io.github.mooy1.slimegrid.implementation.consumers.GridMachine;
import io.github.mooy1.slimegrid.implementation.consumers.ItemGenerator;
import io.github.mooy1.slimegrid.implementation.consumers.crafters.MagicAutoCrafter;
import io.github.mooy1.slimegrid.implementation.consumers.crafters.SlimefunAutoCrafter;
import io.github.mooy1.slimegrid.implementation.consumers.crafters.VanillaAutoCrafter;
import io.github.mooy1.slimegrid.implementation.generators.GridPanel;
import io.github.mooy1.slimegrid.utils.GridLorePreset;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Material;

import java.util.Locale;

public final class Items {

    public static final SlimefunItemStack WIRELESS_INPUT_NODE = new SlimefunItemStack(
            "WIRELESS_INPUT_NODE",
            Material.END_ROD,
            "&9Wireless input node",
            "&7Transfers items from the connected inventory to wireless output nodes",
            "&7Crouch + Right-Click to get info and mark a path to the connected output node"
    );
    public static final SlimefunItemStack WIRELESS_OUTPUT_NODE = new SlimefunItemStack(
            "WIRELESS_OUTPUT_NODE",
            Material.END_ROD,
            "&9Wireless output node",
            "&7Transfers items from wireless input nodes to the connected inventory"
    );
    public static final SlimefunItemStack WIRELESS_CONFIGURATOR = new SlimefunItemStack(
            "WIRELESS_CONFIGURATOR",
            Material.BLAZE_ROD,
            "&9Wireless configurator",
            "&eRight-Click &7an input node then an output node to connect them",
            "&eCrouch + Right-Click &7an input node to remove connected output node",
            "&eCrouch + Right-Click &7the air to clear the input node currently being configured"
    );

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

    public static final SlimefunItemStack GRID_PANEL_I = makeGridPanel(1, GridPanel.I_DAY, GridPanel.I_NIGHT); // .5
    public static final SlimefunItemStack GRID_PANEL_II = makeGridPanel(2, GridPanel.II_DAY, GridPanel.II_NIGHT); // 1.5
    public static final SlimefunItemStack GRID_PANEL_III = makeGridPanel(3, GridPanel.III_DAY, GridPanel.III_NIGHT); // 4
    public static final SlimefunItemStack GRID_PANEL_IV = makeGridPanel(4, GridPanel.IV_DAY, GridPanel.IV_NIGHT); // 10
    public static final SlimefunItemStack GRID_PANEL_V = makeGridPanel(5, GridPanel.V_DAY, GridPanel.V_NIGHT); // 35
    public static final SlimefunItemStack GRID_PANEL_VI = makeGridPanel(6, GridPanel.VI_DAY, GridPanel.VI_NIGHT); // 90
    public static final SlimefunItemStack GRID_PANEL_VII = makeGridPanel(7, GridPanel.VII_DAY, GridPanel.VII_NIGHT); // 240

    public static final SlimefunItemStack COBBLE_GEN_I = makeGridItemGenerator("Cobble", 1, ItemGenerator.COBBLE_I, ItemGenerator.COBBLE_I, Material.);
    public static final SlimefunItemStack COBBLE_GEN_II = makeGridItemGenerator("Cobble", 2, ItemGenerator.COBBLE_II, ItemGenerator.COBBLE_II, Material.);
    public static final SlimefunItemStack COBBLE_GEN_III = makeGridItemGenerator("Cobble", 3, ItemGenerator.COBBLE_III, ItemGenerator.COBBLE_III, Material.);
    public static final SlimefunItemStack COBBLE_GEN_IV = makeGridItemGenerator("Cobble", 4, ItemGenerator.COBBLE_IV, ItemGenerator.COBBLE_IV, Material.);

    public static final SlimefunItemStack GRID_CIRCUIT_I = makeCircuit(1);
    public static final SlimefunItemStack GRID_CIRCUIT_II = makeCircuit(2);
    public static final SlimefunItemStack GRID_CIRCUIT_III = makeCircuit(3);
    public static final SlimefunItemStack GRID_CIRCUIT_IV = makeCircuit(4);
    public static final SlimefunItemStack GRID_CIRCUIT_V = makeCircuit(5);
    public static final SlimefunItemStack GRID_CIRCUIT_VI = makeCircuit(6);
    public static final SlimefunItemStack GRID_CIRCUIT_VII = makeCircuit(7);

    public static final SlimefunItemStack PULVERIZER_I = makeMachine(GridMachine.P_I, 1, 1, "Pulverizer", "Pulverizes ores and ingots, into dusts", Material.);
    public static final SlimefunItemStack PULVERIZER_II = makeMachine(GridMachine.P_II, 2, 4, "Pulverizer", "Pulverizes ores and ingots, into dusts", Material.);
    public static final SlimefunItemStack PULVERIZER_III = makeMachine(GridMachine.P_III, 3, 16, "Pulverizer", "Pulverizes ores and ingots, into dusts", Material.);

    public static final SlimefunItemStack FURNACE_I = makeMachine(GridMachine.F_I, 1, 1, "Furnace", "Smelts materials using GP", Material.)
    public static final SlimefunItemStack FURNACE_II = makeMachine(GridMachine.F_II, 2, 2, "Furnace", "Smelts materials using GP", Material.)
    public static final SlimefunItemStack FURNACE_III = makeMachine(GridMachine.F_III, 3, 4, "Furnace", "Smelts materials using GP", Material.)
    public static final SlimefunItemStack FURNACE_IV = makeMachine(GridMachine.F_IV, 4, 8, "Furnace", "Smelts materials using GP", Material.)
    public static final SlimefunItemStack FURNACE_V = makeMachine(GridMachine.F_V, 5, 16, "Furnace", "Smelts materials using GP", Material.)
    public static final SlimefunItemStack FURNACE_VI = makeMachine(GridMachine.F_VI, 6, 32, "Furnace", "Smelts materials using GP", Material.)
    public static final SlimefunItemStack FURNACE_VII = makeMachine(GridMachine.F_VII, 7, 64, "Furnace", "Smelts materials using GP", Material.)

    public static final SlimefunItemStack COMPRESSOR_I = makeMachine(GridMachine.C_I, 1, 1, "Compressor", "Compresses ingots and other materials into blocks", Material.);
    public static final SlimefunItemStack COMPRESSOR_II = makeMachine(GridMachine.C_II, 2, 4, "Compressor", "Compresses ingots and other materials into blocks", Material.);

    public static final SlimefunItemStack DECOMPRESSOR_I = makeMachine(GridMachine.D_I, 1, 1, "Decompressor", "Decompresses blocks into ingots and materials", Material.);
    public static final SlimefunItemStack DECOMPRESSOR_II = makeMachine(GridMachine.D_II, 2, 4, "Decompressor", "Decompresses blocks into ingots and materials", Material.);
    
    public static final SlimefunItemStack INFUSED_GLASS = new SlimefunItemStack(
            "INFUSED_GLASS",
            Material.WHITE_STAINED_GLASS,
            "&fInfused Glass",
            "&7Grid machine Component"
    );
    public static final SlimefunItemStack INFUSED_CRYSTAL = new SlimefunItemStack(
            "INFUSED_CRYSTAL",
            Material.QUARTZ,
            "&fInfused Crystal",
            "&7Grid machine Component"
    );
    public static final SlimefunItemStack INFUSED_PLATE = new SlimefunItemStack(
            "INFUSED_PLATE",
            Material.PAPER,
            "&fInfused Plate",
            "&7Grid machine Component"
    );
    public static final SlimefunItemStack SILVER_WIRE = new SlimefunItemStack(
            "SILVER_WIRE",
            Material.STRING,
            "&fSilver Wire",
            "&7Grid machine Component"
    );
    public static final SlimefunItemStack VANILLA_AUTO_CRAFTER = new SlimefunItemStack(
            "VANILLA_AUTO_CRAFTER",
            Material.CRAFTING_TABLE,
            "&eAuto Vanilla Workbench",
            "&7Automatically crafts vanilla items using GP",
            "",
            GridLorePreset.consumesGridPower(VanillaAutoCrafter.GP)
    );
    public static final SlimefunItemStack SLIMEFUN_AUTO_CRAFTER = new SlimefunItemStack(
            "SLIMEFUN_AUTO_CRAFTER",
            Material.LOOM,
            "&eAuto Enhanced Workbench",
            "&7Automatically crafts slimefun items and armor using GP",
            "",
            GridLorePreset.consumesGridPower(SlimefunAutoCrafter.GP)
    );
    public static final SlimefunItemStack MAGIC_AUTO_CRAFTER = new SlimefunItemStack(
            "MAGIC_AUTO_CRAFTER",
            Material.LODESTONE,
            "&eAuto Magic Workbench",
            "&7Automatically crafts magic items and runes using GP",
            "",
            GridLorePreset.consumesGridPower(MagicAutoCrafter.GP)
    );

    private static SlimefunItemStack makeGridPanel(int tier, int day, int night) {
        return new SlimefunItemStack(
                "GRID_PANEL_" + LorePreset.romanNumeral(tier),
                Material.DAYLIGHT_DETECTOR,
                "&eSolar Panel &6" + LorePreset.romanNumeral(tier),
                "&7Generates GP from sunlight",
                "",
                GridLorePreset.generatesGridPower(day) + "Daytime",
                GridLorePreset.generatesGridPower(night) + "Nighttime"
        );
    }

    private static SlimefunItemStack makeGridItemGenerator(String type, int tier, int power, int speed, Material material) {
        return new SlimefunItemStack(
                type.toUpperCase(Locale.ROOT) + "_GENERATOR_" + LorePreset.romanNumeral(tier),
                material,
                "&e" + type + " Generator &6" + LorePreset.romanNumeral(tier),
                "&7Generates " + type + " from GP",
                "",
                GridLorePreset.consumesGridPower(power),
                LorePreset.speed(speed)
        );
    }

    private static SlimefunItemStack makeCircuit(int tier) {
        return new SlimefunItemStack(
                "GRID_CIRCUIT_" + LorePreset.romanNumeral(tier),
                Material.HONEYCOMB,
                "&eGrid Circuit &6" + LorePreset.romanNumeral(tier),
                "&7Grid Machine component"
        );
    }

    private static SlimefunItemStack makeGenerator(int power, int tier, String name, String desc, Material material) {
        return new SlimefunItemStack(
                name.toUpperCase(Locale.ROOT) + "_GENERATOR_" + LorePreset.romanNumeral(tier),
                material,
                "&e" + name + " Generator &6" + LorePreset.romanNumeral(tier),
                "&7Generates power from " + desc,
                GridLorePreset.generatesGridPower(power)
        );
    }

    private static SlimefunItemStack makeMachine(int power, int tier, int speed, String name, String desc, Material material) {
        return new SlimefunItemStack(
                name.toUpperCase(Locale.ROOT) + "_MACHINE_" + LorePreset.romanNumeral(tier),
                material,
                "&e" + name + " &6" + LorePreset.romanNumeral(tier),
                "&7" + desc,
                GridLorePreset.consumesGridPower(power),
                LorePreset.speed(speed)
        );
    }
    
}
