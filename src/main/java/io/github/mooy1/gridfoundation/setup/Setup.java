package io.github.mooy1.gridfoundation.setup;

import io.github.mooy1.gridfoundation.GridFoundation;
import io.github.mooy1.gridfoundation.implementation.blocks.ManualSieve;
import io.github.mooy1.gridfoundation.implementation.consumers.AutoSieve;
import io.github.mooy1.gridfoundation.implementation.grid.GridViewer;
import io.github.mooy1.gridfoundation.implementation.materials.CrushedBlock;
import io.github.mooy1.gridfoundation.implementation.components.Gear;
import io.github.mooy1.gridfoundation.implementation.components.MachineFrame;
import io.github.mooy1.gridfoundation.implementation.components.Plate;
import io.github.mooy1.gridfoundation.implementation.materials.Alloy;
import io.github.mooy1.gridfoundation.implementation.components.Wire;
import io.github.mooy1.gridfoundation.implementation.consumers.combiners.AlloySmelter;
import io.github.mooy1.gridfoundation.implementation.consumers.crafters.AutoSmeltery;
import io.github.mooy1.gridfoundation.implementation.consumers.crafters.MagicAutoCrafter;
import io.github.mooy1.gridfoundation.implementation.consumers.crafters.SlimefunAutoCrafter;
import io.github.mooy1.gridfoundation.implementation.consumers.crafters.VanillaAutoCrafter;
import io.github.mooy1.gridfoundation.implementation.consumers.gens.CobbleGen;
import io.github.mooy1.gridfoundation.implementation.consumers.converters.Compactor;
import io.github.mooy1.gridfoundation.implementation.consumers.converters.Decompactor;
import io.github.mooy1.gridfoundation.implementation.consumers.converters.Furnace;
import io.github.mooy1.gridfoundation.implementation.consumers.converters.Pulverizer;
import io.github.mooy1.gridfoundation.implementation.consumers.processors.GearCaster;
import io.github.mooy1.gridfoundation.implementation.consumers.processors.PlatePress;
import io.github.mooy1.gridfoundation.implementation.consumers.processors.WireDrawer;
import io.github.mooy1.gridfoundation.implementation.generators.machines.SurvivalGenerator;
import io.github.mooy1.gridfoundation.implementation.generators.panels.LunarPanel;
import io.github.mooy1.gridfoundation.implementation.generators.panels.SolarPanel;
import io.github.mooy1.gridfoundation.implementation.upgrades.UpgradeKit;
import io.github.mooy1.gridfoundation.implementation.upgrades.UpgradeType;
import io.github.mooy1.gridfoundation.implementation.wireless.WirelessConfigurator;
import io.github.mooy1.gridfoundation.implementation.wireless.WirelessInputNode;
import io.github.mooy1.gridfoundation.implementation.wireless.WirelessOutputNode;
import io.github.mooy1.infinitylib.PluginUtils;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public final class Setup {

    public static void setup(@Nonnull GridFoundation plugin) {

        PluginUtils.registerAddonInfoItem(Categories.MAIN, GridFoundation.getInstance());

        // blocks
        
        new GridViewer().register(plugin);
        new ManualSieve().register(plugin);
        
        // components

        new MachineFrame().register(plugin);
        new Alloy(Alloy.GLASS, new ItemStack(Material.SAND, 4), new SlimefunItemStack(SlimefunItems.SILVER_DUST, 6), new ItemStack(Material.QUARTZ, 4)).register(plugin);
        //new Alloy(Alloy.INFUSED, , , ).register(plugin);
        //new Alloy(Alloy.ULTIMATUM, , , ).register(plugin);
        
        new CrushedBlock(CrushedBlock.DUST).register(plugin);
        new CrushedBlock(CrushedBlock.END).register(plugin);
        new CrushedBlock(CrushedBlock.NETHER).register(plugin);
        new CrushedBlock(CrushedBlock.ANDESITE).register(plugin);
        new CrushedBlock(CrushedBlock.GRANITE).register(plugin);
        new CrushedBlock(CrushedBlock.DIORITE).register(plugin);
        
        new Wire(Wire.SILVER, SlimefunItems.SILVER_INGOT).register(plugin);

        new Gear(Gear.COPPER, new SlimefunItemStack(SlimefunItems.COPPER_INGOT, 8)).register(plugin);
        new Gear(Gear.COBALT, new SlimefunItemStack(SlimefunItems.COBALT_INGOT, 8)).register(plugin);
        new Gear(Gear.GOLDEN, new ItemStack(Material.GOLD_INGOT, 8)).register(plugin);
        new Gear(Gear.TIN, new SlimefunItemStack(SlimefunItems.TIN_INGOT, 8)).register(plugin);

        new Plate(Plate.ALUMINUM, new SlimefunItemStack(SlimefunItems.ALUMINUM_INGOT, 16)).register(plugin);
        new Plate(Plate.BRONZE, new SlimefunItemStack(SlimefunItems.BRONZE_INGOT, 16)).register(plugin);
        new Plate(Plate.IRON, new ItemStack(Material.IRON_INGOT, 16)).register(plugin);
        new Plate(Plate.LEAD, new SlimefunItemStack(SlimefunItems.LEAD_INGOT, 16)).register(plugin);
        
        // TODO: circuits, custom dusts from sifter
        
        // consumers
        
        new CobbleGen().register(plugin);
        
        new VanillaAutoCrafter().register(plugin);
        new SlimefunAutoCrafter().register(plugin);
        new MagicAutoCrafter().register(plugin);
        new AutoSmeltery().register(plugin);
        
        new GearCaster().register(plugin);
        new PlatePress().register(plugin);
        new WireDrawer().register(plugin);
        
        new Compactor().register(plugin);
        new Decompactor().register(plugin);
        new Furnace().register(plugin);
        new Pulverizer().register(plugin);
        
        new AlloySmelter().register(plugin);
        
        new AutoSieve().register(plugin);
        
        //TODO: crucible, sifter for crushed stuff and dirt, sand , gravel, soulsand
        
        // generators
        
        new SurvivalGenerator().register(plugin);
        
        new SolarPanel().register(plugin);
        new LunarPanel().register(plugin);
        
        //TODO dynamos
        
        // upgrades
        
        new UpgradeKit(UpgradeType.ADVANCED, new ItemStack[] {

        }).register(plugin);
        new UpgradeKit(UpgradeType.HARDENED, new ItemStack[] {

        }).register(plugin);
        new UpgradeKit(UpgradeType.ELITE, new ItemStack[] {

        }).register(plugin);
        new UpgradeKit(UpgradeType.REINFORCED, new ItemStack[] {

        }).register(plugin);
        new UpgradeKit(UpgradeType.INFUSED, new ItemStack[] {

        }).register(plugin);
        new UpgradeKit(UpgradeType.ULTIMATE, new ItemStack[] {

        }).register(plugin);
        
        // tools

        new WirelessConfigurator().register(plugin);
        new WirelessInputNode().register(plugin);
        new WirelessOutputNode().register(plugin);
        
        // TODO workbench, wand, hammers, gear for advanced ingots
        
    }

    }
