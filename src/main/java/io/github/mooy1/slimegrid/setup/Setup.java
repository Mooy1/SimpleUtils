package io.github.mooy1.slimegrid.setup;

import io.github.mooy1.slimegrid.SlimeGrid;
import io.github.mooy1.slimegrid.implementation.consumers.GridMachine;
import io.github.mooy1.slimegrid.implementation.consumers.ItemGenerator;
import io.github.mooy1.slimegrid.implementation.consumers.crafters.MagicAutoCrafter;
import io.github.mooy1.slimegrid.implementation.consumers.crafters.SlimefunAutoCrafter;
import io.github.mooy1.slimegrid.implementation.consumers.crafters.VanillaAutoCrafter;
import io.github.mooy1.slimegrid.implementation.generators.GridPanel;
import io.github.mooy1.slimegrid.implementation.wireless.WirelessConfigurator;
import io.github.mooy1.slimegrid.implementation.wireless.WirelessInputNode;
import io.github.mooy1.slimegrid.implementation.wireless.WirelessOutputNode;
import io.github.mooy1.slimegrid.lists.Categories;
import io.github.mooy1.slimegrid.lists.Items;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class Setup {

    public static void setup(SlimeGrid plugin) {

        new SlimefunItem(Categories.COMPONENTS, Items.ADDON_INFO, RecipeType.NULL, null).register(plugin);

        new VanillaAutoCrafter().register(plugin);
        new SlimefunAutoCrafter().register(plugin);
        new MagicAutoCrafter().register(plugin);

        new WirelessInputNode().register(plugin);
        new WirelessOutputNode().register(plugin);
        new WirelessConfigurator(plugin).register(plugin);

        new SlimefunItem(Categories.COMPONENTS, Items.SILVER_WIRE, RecipeType.COMPRESSOR, new ItemStack[] {
                SlimefunItems.SILVER_INGOT, null, null, null, null, null, null, null, null
        }, new SlimefunItemStack(Items.SILVER_WIRE, 3)).register(plugin);
        new SlimefunItem(Categories.COMPONENTS, Items.INFUSED_CRYSTAL, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                new ItemStack(Material.QUARTZ), new ItemStack(Material.REDSTONE), new ItemStack(Material.QUARTZ),
                SlimefunItems.SILVER_DUST, new ItemStack(Material.QUARTZ), SlimefunItems.SILVER_DUST,
                new ItemStack(Material.QUARTZ), new ItemStack(Material.REDSTONE), new ItemStack(Material.QUARTZ)
        }).register(plugin);
        new SlimefunItem(Categories.COMPONENTS, Items.INFUSED_GLASS, RecipeType.SMELTERY, new ItemStack[] {
                new ItemStack(Material.SAND), Items.INFUSED_CRYSTAL, SlimefunItems.SILVER_DUST,
                null, null, null, null, null, null
        }).register(plugin);
        new SlimefunItem(Categories.COMPONENTS, Items.INFUSED_PLATE, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                SlimefunItems.SILVER_INGOT, SlimefunItems.SILVER_INGOT, SlimefunItems.SILVER_INGOT,
                SlimefunItems.SILVER_INGOT, Items.INFUSED_CRYSTAL, SlimefunItems.SILVER_INGOT,
                SlimefunItems.SILVER_INGOT, SlimefunItems.SILVER_INGOT, SlimefunItems.SILVER_INGOT,
        }).register(plugin);

        new SlimefunItem(Categories.COMPONENTS, Items.GRID_CIRCUIT_I, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                SlimefunItems.COPPER_WIRE, SlimefunItems.GOLD_4K, SlimefunItems.COPPER_WIRE,
                Items.SILVER_WIRE, Items.INFUSED_CRYSTAL, Items.SILVER_WIRE,
                SlimefunItems.COPPER_WIRE, SlimefunItems.GOLD_4K, SlimefunItems.COPPER_WIRE
        }).register(plugin);
        new SlimefunItem(Categories.COMPONENTS, Items.GRID_CIRCUIT_II, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                Items.GRID_CIRCUIT_I, SlimefunItems.GOLD_8K, Items.GRID_CIRCUIT_I,
                Items.SILVER_WIRE, Items.INFUSED_CRYSTAL, Items.SILVER_WIRE,
                Items.GRID_CIRCUIT_I, SlimefunItems.GOLD_8K, Items.GRID_CIRCUIT_I
        }).register(plugin);
        new SlimefunItem(Categories.COMPONENTS, Items.GRID_CIRCUIT_III, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                Items.GRID_CIRCUIT_II, SlimefunItems.GOLD_12K, Items.GRID_CIRCUIT_II,
                Items.SILVER_WIRE, Items.INFUSED_CRYSTAL, Items.SILVER_WIRE,
                Items.GRID_CIRCUIT_II, SlimefunItems.GOLD_12K, Items.GRID_CIRCUIT_II
        }).register(plugin);
        new SlimefunItem(Categories.COMPONENTS, Items.GRID_CIRCUIT_IV, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                Items.GRID_CIRCUIT_III, SlimefunItems.GOLD_16K, Items.GRID_CIRCUIT_III,
                SlimefunItems.MAGNET, Items.INFUSED_CRYSTAL, SlimefunItems.MAGNET,
                Items.GRID_CIRCUIT_III, SlimefunItems.GOLD_16K, Items.GRID_CIRCUIT_III
        }).register(plugin);
        new SlimefunItem(Categories.COMPONENTS, Items.GRID_CIRCUIT_V, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                Items.GRID_CIRCUIT_IV, SlimefunItems.GOLD_20K, Items.GRID_CIRCUIT_IV,
                SlimefunItems.MAGNET, Items.INFUSED_CRYSTAL, SlimefunItems.MAGNET,
                Items.GRID_CIRCUIT_IV, SlimefunItems.GOLD_20K, Items.GRID_CIRCUIT_IV
        }).register(plugin);
        new SlimefunItem(Categories.COMPONENTS, Items.GRID_CIRCUIT_VI, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                Items.GRID_CIRCUIT_V, SlimefunItems.GOLD_24K, Items.GRID_CIRCUIT_V,
                SlimefunItems.ELECTRO_MAGNET, Items.INFUSED_CRYSTAL, SlimefunItems.ELECTRO_MAGNET,
                Items.GRID_CIRCUIT_V, SlimefunItems.GOLD_24K, Items.GRID_CIRCUIT_V
        }).register(plugin);
        new SlimefunItem(Categories.COMPONENTS, Items.GRID_CIRCUIT_VII, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                Items.GRID_CIRCUIT_VI, SlimefunItems.GOLD_24K_BLOCK, Items.GRID_CIRCUIT_VI,
                SlimefunItems.ELECTRO_MAGNET, Items.INFUSED_CRYSTAL, SlimefunItems.ELECTRO_MAGNET,
                Items.GRID_CIRCUIT_VI, SlimefunItems.GOLD_24K_BLOCK, Items.GRID_CIRCUIT_VI
        }).register(plugin);

        new GridPanel(GridPanel.I_DAY, GridPanel.I_NIGHT, Items.GRID_PANEL_I, new ItemStack[] {
                new ItemStack(Material.GLASS), new ItemStack(Material.GLASS), new ItemStack(Material.GLASS),
                Items.INFUSED_CRYSTAL, Items.SILVER_WIRE, Items.INFUSED_CRYSTAL,
                Items.GRID_CIRCUIT_I, Items.SILVER_WIRE, Items.GRID_CIRCUIT_I,
        }).register(plugin);
        new GridPanel(GridPanel.II_DAY, GridPanel.II_NIGHT, Items.GRID_PANEL_II, new ItemStack[] {
                new ItemStack(Material.GLASS), new ItemStack(Material.GLASS), new ItemStack(Material.GLASS),
                Items.GRID_PANEL_I, Items.SILVER_WIRE, Items.GRID_PANEL_I,
                Items.GRID_CIRCUIT_II, Items.SILVER_WIRE, Items.GRID_CIRCUIT_II,
        }).register(plugin);
        new GridPanel(GridPanel.III_DAY, GridPanel.III_NIGHT, Items.GRID_PANEL_III, new ItemStack[] {
                new ItemStack(Material.GLASS), new ItemStack(Material.GLASS), new ItemStack(Material.GLASS),
                Items.GRID_PANEL_II, Items.SILVER_WIRE, Items.GRID_PANEL_II,
                Items.GRID_CIRCUIT_III, Items.SILVER_WIRE, Items.GRID_CIRCUIT_III,
        }).register(plugin);
        new GridPanel(GridPanel.IV_DAY, GridPanel.IV_NIGHT, Items.GRID_PANEL_IV, new ItemStack[] {
                new ItemStack(Material.GLASS), Items.INFUSED_GLASS, new ItemStack(Material.GLASS),
                Items.GRID_PANEL_III, Items.SILVER_WIRE, Items.GRID_PANEL_III,
                Items.GRID_CIRCUIT_IV, Items.INFUSED_PLATE, Items.GRID_CIRCUIT_IV,
        }).register(plugin);
        new GridPanel(GridPanel.V_DAY, GridPanel.V_NIGHT, Items.GRID_PANEL_V, new ItemStack[] {
                Items.INFUSED_GLASS, Items.INFUSED_GLASS, Items.INFUSED_GLASS,
                Items.GRID_PANEL_IV, Items.INFUSED_PLATE, Items.GRID_PANEL_IV,
                Items.GRID_CIRCUIT_V, Items.INFUSED_PLATE, Items.GRID_CIRCUIT_V,
        }).register(plugin);
        new GridPanel(GridPanel.VI_DAY, GridPanel.VI_NIGHT, Items.GRID_PANEL_VI, new ItemStack[] {
                Items.INFUSED_GLASS, Items.INFUSED_GLASS, Items.INFUSED_GLASS,
                Items.GRID_PANEL_V, Items.INFUSED_PLATE, Items.GRID_PANEL_V,
                Items.GRID_CIRCUIT_VI, Items.INFUSED_PLATE, Items.GRID_CIRCUIT_VI,
        }).register(plugin);
        new GridPanel(GridPanel.VII_DAY, GridPanel.VII_NIGHT, Items.GRID_PANEL_VII, new ItemStack[] {
                Items.INFUSED_GLASS, Items.INFUSED_GLASS, Items.INFUSED_GLASS,
                Items.GRID_PANEL_VI, Items.INFUSED_PLATE, Items.GRID_PANEL_VI,
                Items.GRID_CIRCUIT_VII, Items.INFUSED_PLATE, Items.GRID_CIRCUIT_VII,
        }).register(plugin);

        new ItemGenerator(Items.COBBLE_GEN_I, ItemGenerator.COBBLE_I, ItemGenerator.COBBLE_I, Material.COBBLESTONE, new ItemStack[] {
                Items.INFUSED_PLATE, Items.GRID_CIRCUIT_I, Items.INFUSED_PLATE,
                new ItemStack(Material.WATER_BUCKET), Items.INFUSED_CRYSTAL, new ItemStack(Material.LAVA_BUCKET),
                Items.INFUSED_PLATE, Items.GRID_CIRCUIT_I, Items.INFUSED_PLATE
        }).register(plugin);
        new ItemGenerator(Items.COBBLE_GEN_II, ItemGenerator.COBBLE_II, ItemGenerator.COBBLE_II, Material.COBBLESTONE, new ItemStack[] {
                Items.INFUSED_PLATE, Items.GRID_CIRCUIT_II, Items.INFUSED_PLATE,
                Items.COBBLE_GEN_I, Items.INFUSED_CRYSTAL, Items.COBBLE_GEN_I,
                Items.INFUSED_PLATE, Items.GRID_CIRCUIT_II, Items.INFUSED_PLATE
        }).register(plugin);
        new ItemGenerator(Items.COBBLE_GEN_III, ItemGenerator.COBBLE_III, ItemGenerator.COBBLE_III, Material.COBBLESTONE, new ItemStack[] {
                Items.INFUSED_PLATE, Items.GRID_CIRCUIT_III, Items.INFUSED_PLATE,
                Items.COBBLE_GEN_II, Items.INFUSED_CRYSTAL, Items.COBBLE_GEN_II,
                Items.INFUSED_PLATE, Items.GRID_CIRCUIT_III, Items.INFUSED_PLATE
        }).register(plugin);
        new ItemGenerator(Items.COBBLE_GEN_IV, ItemGenerator.COBBLE_IV, ItemGenerator.COBBLE_IV, Material.COBBLESTONE, new ItemStack[] {
                Items.INFUSED_PLATE, Items.GRID_CIRCUIT_IV, Items.INFUSED_PLATE,
                Items.COBBLE_GEN_III, Items.INFUSED_CRYSTAL, Items.COBBLE_GEN_III,
                Items.INFUSED_PLATE, Items.GRID_CIRCUIT_IV, Items.INFUSED_PLATE
        }).register(plugin);

        new GridMachine(Items.PULVERIZER_I, GridMachine.pulverizerRecipes, GridMachine.P_I, 1, new ItemStack[] {
                
        }).register(plugin);
        new GridMachine(Items.PULVERIZER_II, GridMachine.pulverizerRecipes, GridMachine.P_II, 4, new ItemStack[] {

        }).register(plugin);
        new GridMachine(Items.PULVERIZER_III, GridMachine.pulverizerRecipes, GridMachine.P_III, 16, new ItemStack[] {

        }).register(plugin);
        
        new GridMachine(Items.FURNACE_I, GridMachine.furnaceRecipes, GridMachine.F_I, 1, new ItemStack[] {

        }).register(plugin);
        new GridMachine(Items.FURNACE_II, GridMachine.furnaceRecipes, GridMachine.F_II, 2, new ItemStack[] {

        }).register(plugin);
        new GridMachine(Items.FURNACE_III, GridMachine.furnaceRecipes, GridMachine.F_III, 4, new ItemStack[] {

        }).register(plugin);
        new GridMachine(Items.FURNACE_IV, GridMachine.furnaceRecipes, GridMachine.F_IV, 8, new ItemStack[] {

        }).register(plugin);
        new GridMachine(Items.FURNACE_V, GridMachine.furnaceRecipes, GridMachine.F_V, 16, new ItemStack[] {

        }).register(plugin);
        new GridMachine(Items.FURNACE_VI, GridMachine.furnaceRecipes, GridMachine.F_VI, 32, new ItemStack[] {

        }).register(plugin);
        new GridMachine(Items.FURNACE_VII, GridMachine.furnaceRecipes, GridMachine.F_VII, 64, new ItemStack[] {

        }).register(plugin);

        new GridMachine(Items.COMPRESSOR_I, GridMachine.compressorRecipes, GridMachine.C_I, 1, new ItemStack[] {

        }).register(plugin);
        new GridMachine(Items.COMPRESSOR_II, GridMachine.compressorRecipes, GridMachine.C_II, 4, new ItemStack[] {

        }).register(plugin);

        new GridMachine(Items.DECOMPRESSOR_I, GridMachine.decompressorRecipes, GridMachine.D_I, 1, new ItemStack[] {

        }).register(plugin);
        new GridMachine(Items.DECOMPRESSOR_II, GridMachine.decompressorRecipes, GridMachine.D_II, 4, new ItemStack[] {

        }).register(plugin);
        
    }

    }
