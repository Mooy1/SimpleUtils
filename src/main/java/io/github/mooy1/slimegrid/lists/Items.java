package io.github.mooy1.slimegrid.lists;

import io.github.mooy1.infinitylib.presets.LorePreset;
import io.github.mooy1.slimegrid.implementation.consumers.ItemGenerator;
import io.github.mooy1.slimegrid.implementation.consumers.ProcessingMachine;
import io.github.mooy1.slimegrid.implementation.consumers.crafters.MagicAutoCrafter;
import io.github.mooy1.slimegrid.implementation.consumers.crafters.SlimefunAutoCrafter;
import io.github.mooy1.slimegrid.implementation.consumers.crafters.VanillaAutoCrafter;
import io.github.mooy1.slimegrid.implementation.generators.PowerGenerator;
import io.github.mooy1.slimegrid.implementation.generators.SolarPanel;
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

    public static final SlimefunItemStack ALUMINUM_PLATE = makePlate("Aluminum", Material.WHITE_STAINED_GLASS_PANE);
    public static final SlimefunItemStack TIN_PLATE = makePlate("Tin", Material.CYAN_STAINED_GLASS_PANE);
    public static final SlimefunItemStack LEAD_PLATE = makePlate("Lead", Material.GRAY_STAINED_GLASS_PANE);
    public static final SlimefunItemStack IRON_PLATE = makePlate("Iron", Material.LIGHT_GRAY_STAINED_GLASS_PANE);
    public static final SlimefunItemStack BRONZE_PLATE = makePlate("Bronze", Material.ORANGE_STAINED_GLASS_PANE);

    public static final SlimefunItemStack GOLDEN_GEAR = makePlate("Gold", Material.HORN_CORAL);
    public static final SlimefunItemStack COPPER_GEAR = makePlate("Copper", Material.FIRE_CORAL);
    public static final SlimefunItemStack MAGNESIUM_GEAR = makePlate("Magnesium", Material.BRAIN_CORAL);
    public static final SlimefunItemStack COBALT_GEAR = makePlate("Cobalt", Material.TUBE_CORAL);

    public static final SlimefunItemStack GRID_PANEL_I = makePanel(1, SolarPanel.I_DAY, SolarPanel.I_NIGHT); 
    public static final SlimefunItemStack GRID_PANEL_II = makePanel(2, SolarPanel.II_DAY, SolarPanel.II_NIGHT); 
    public static final SlimefunItemStack GRID_PANEL_III = makePanel(3, SolarPanel.III_DAY, SolarPanel.III_NIGHT); 
    public static final SlimefunItemStack GRID_PANEL_IV = makePanel(4, SolarPanel.IV_DAY, SolarPanel.IV_NIGHT); 
    public static final SlimefunItemStack GRID_PANEL_V = makePanel(5, SolarPanel.V_DAY, SolarPanel.V_NIGHT); 
    public static final SlimefunItemStack GRID_PANEL_VI = makePanel(6, SolarPanel.VI_DAY, SolarPanel.VI_NIGHT); 
    public static final SlimefunItemStack GRID_PANEL_VII = makePanel(7, SolarPanel.VII_DAY, SolarPanel.VII_NIGHT); 

    public static final SlimefunItemStack COBBLE_GEN_I = makeItemGenerator("Cobble", 1, ItemGenerator.COBBLE_I, ItemGenerator.COBBLE_I, Material.SMOOTH_STONE);
    public static final SlimefunItemStack COBBLE_GEN_II = makeItemGenerator("Cobble", 2, ItemGenerator.COBBLE_II, ItemGenerator.COBBLE_II, Material.SMOOTH_STONE);
    public static final SlimefunItemStack COBBLE_GEN_III = makeItemGenerator("Cobble", 3, ItemGenerator.COBBLE_III, ItemGenerator.COBBLE_III, Material.SMOOTH_STONE);
    public static final SlimefunItemStack COBBLE_GEN_IV = makeItemGenerator("Cobble", 4, ItemGenerator.COBBLE_IV, ItemGenerator.COBBLE_IV, Material.SMOOTH_STONE);

    public static final SlimefunItemStack GRID_CIRCUIT_I = makeCircuit(1);
    public static final SlimefunItemStack GRID_CIRCUIT_II = makeCircuit(2);
    public static final SlimefunItemStack GRID_CIRCUIT_III = makeCircuit(3);
    public static final SlimefunItemStack GRID_CIRCUIT_IV = makeCircuit(4);
    public static final SlimefunItemStack GRID_CIRCUIT_V = makeCircuit(5);
    public static final SlimefunItemStack GRID_CIRCUIT_VI = makeCircuit(6);
    public static final SlimefunItemStack GRID_CIRCUIT_VII = makeCircuit(7);

    public static final SlimefunItemStack PULVERIZER_I = makeMachine(ProcessingMachine.P_I, 1, 1, "Pulverizer", "Pulverizes ores and ingots, into dusts", Material.BLAST_FURNACE);
    public static final SlimefunItemStack PULVERIZER_II = makeMachine(ProcessingMachine.P_II, 2, 4, "Pulverizer", "Pulverizes ores and ingots, into dusts", Material.BLAST_FURNACE);
    public static final SlimefunItemStack PULVERIZER_III = makeMachine(ProcessingMachine.P_III, 3, 16, "Pulverizer", "Pulverizes ores and ingots, into dusts", Material.BLAST_FURNACE);

    public static final SlimefunItemStack FURNACE_I = makeMachine(ProcessingMachine.F_I, 1, 1, "Furnace", "Smelts materials using GP", Material.FURNACE);
    public static final SlimefunItemStack FURNACE_II = makeMachine(ProcessingMachine.F_II, 2, 2, "Furnace", "Smelts materials using GP", Material.FURNACE);
    public static final SlimefunItemStack FURNACE_III = makeMachine(ProcessingMachine.F_III, 3, 4, "Furnace", "Smelts materials using GP", Material.FURNACE);
    public static final SlimefunItemStack FURNACE_IV = makeMachine(ProcessingMachine.F_IV, 4, 8, "Furnace", "Smelts materials using GP", Material.FURNACE);
    public static final SlimefunItemStack FURNACE_V = makeMachine(ProcessingMachine.F_V, 5, 16, "Furnace", "Smelts materials using GP", Material.FURNACE);
    public static final SlimefunItemStack FURNACE_VI = makeMachine(ProcessingMachine.F_VI, 6, 32, "Furnace", "Smelts materials using GP", Material.FURNACE);
    public static final SlimefunItemStack FURNACE_VII = makeMachine(ProcessingMachine.F_VII, 7, 64, "Furnace", "Smelts materials using GP", Material.FURNACE);

    public static final SlimefunItemStack COMPRESSOR_I = makeMachine(ProcessingMachine.C_I, 1, 1, "Compressor", "Compresses ingots and other materials into blocks", Material.SMOOTH_SANDSTONE);
    public static final SlimefunItemStack COMPRESSOR_II = makeMachine(ProcessingMachine.C_II, 2, 4, "Compressor", "Compresses ingots and other materials into blocks", Material.SMOOTH_SANDSTONE);

    public static final SlimefunItemStack DECOMPRESSOR_I = makeMachine(ProcessingMachine.D_I, 1, 1, "Decompressor", "Decompresses blocks into ingots and materials", Material.SMOOTH_RED_SANDSTONE);
    public static final SlimefunItemStack DECOMPRESSOR_II = makeMachine(ProcessingMachine.D_II, 2, 4, "Decompressor", "Decompresses blocks into ingots and materials", Material.SMOOTH_RED_SANDSTONE);
    
    public static final SlimefunItemStack INFUSED_GLASS = new SlimefunItemStack(
            "INFUSED_GLASS",
            Material.WHITE_STAINED_GLASS,
            "&fInfused Glass",
            "&7Grid machine component"
    );
    public static final SlimefunItemStack INFUSED_CORE = new SlimefunItemStack(
            "INFUSED_CORE",
            Material.QUARTZ,
            "&fInfused Core",
            "&7Grid machine component"
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
            Material.BOOKSHELF,
            "&eAuto Magic Workbench",
            "&7Automatically crafts magic items and runes using GP",
            "",
            GridLorePreset.consumesGridPower(MagicAutoCrafter.GP)
    );

    public static final SlimefunItemStack SURVIVAL_GENERATOR = makeGenerator(PowerGenerator.SURVIVAL, "Survival", "flammable fuels", Material.GRAY_CONCRETE);
    public static final SlimefunItemStack OVERCLOCKED_GENERATOR = makeGenerator(PowerGenerator.OVERCLOCKED, "Overclocked", "flammable fuels", Material.MAGENTA_CONCRETE);

    private static SlimefunItemStack makePanel(int tier, int day, int night) {
        return new SlimefunItemStack(
                "PANEL_" + LorePreset.romanNumeral(tier) + "_GRID",
                Material.DAYLIGHT_DETECTOR,
                "&eSolar Panel &6" + LorePreset.romanNumeral(tier),
                "&7Generates GP from sunlight",
                "",
                GridLorePreset.generatesGridPower(day) + "Daytime",
                GridLorePreset.generatesGridPower(night) + "Nighttime"
        );
    }

    private static SlimefunItemStack makeItemGenerator(String type, int tier, int power, int speed, Material material) {
        return new SlimefunItemStack(
                type.toUpperCase(Locale.ROOT) + "GEN_" + LorePreset.romanNumeral(tier) + "_GRID",
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
                "CIRCUIT_" + LorePreset.romanNumeral(tier) + "_GRID",
                Material.HONEYCOMB,
                "&eGrid Circuit &6" + LorePreset.romanNumeral(tier),
                "&7Grid Machine component"
        );
    }

    private static SlimefunItemStack makeGenerator(int power, String name, String input, Material material) {
        return new SlimefunItemStack(
                name.toUpperCase(Locale.ROOT) + "_GENERATOR_GRID",
                material,
                "&e" + name + " Generator &6",
                "&7Generates power from " + input,
                GridLorePreset.generatesGridPower(power)
        );
    }

    private static SlimefunItemStack makeMachine(int power, int tier, int speed, String name, String desc, Material material) {
        return new SlimefunItemStack(
                name.toUpperCase(Locale.ROOT) + "_" + LorePreset.romanNumeral(tier) + "_GRID",
                material,
                "&e" + name + " &6" + LorePreset.romanNumeral(tier),
                "&7" + desc,
                GridLorePreset.consumesGridPower(power),
                LorePreset.speed(speed)
        );
    }
    
    private static SlimefunItemStack makePlate(String name, Material material) {
        return new SlimefunItemStack(
                name.toUpperCase(Locale.ROOT) + "_PLATE_GRID",
                material,
                "&6" + name + " Plate",
                "&7Grid Machine Component"
        );
    }

    private static SlimefunItemStack makeGear(String name, Material material) {
        return new SlimefunItemStack(
                name.toUpperCase(Locale.ROOT) + "_GEAR_GRID",
                material,
                "&6" + name + " Plate",
                "&7Grid Machine Component"
        );
    }
    
}
