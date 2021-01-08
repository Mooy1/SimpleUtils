package io.github.mooy1.gridfoundation.setup;

import io.github.mooy1.gridfoundation.GridFoundation;
import io.github.mooy1.gridfoundation.implementation.blocks.ManualSieve;
import io.github.mooy1.gridfoundation.implementation.components.Gear;
import io.github.mooy1.gridfoundation.implementation.components.MachineFrame;
import io.github.mooy1.gridfoundation.implementation.components.Plate;
import io.github.mooy1.gridfoundation.implementation.components.Wire;
import io.github.mooy1.gridfoundation.implementation.consumers.machines.AutoSieve;
import io.github.mooy1.gridfoundation.implementation.consumers.machines.multi.AlloySmelter;
import io.github.mooy1.gridfoundation.implementation.consumers.machines.single.Compactor;
import io.github.mooy1.gridfoundation.implementation.consumers.machines.single.Decompactor;
import io.github.mooy1.gridfoundation.implementation.consumers.machines.single.Furnace;
import io.github.mooy1.gridfoundation.implementation.consumers.machines.single.Pulverizer;
import io.github.mooy1.gridfoundation.implementation.consumers.crafters.AutoSmeltery;
import io.github.mooy1.gridfoundation.implementation.consumers.crafters.MagicAutoCrafter;
import io.github.mooy1.gridfoundation.implementation.consumers.crafters.SlimefunAutoCrafter;
import io.github.mooy1.gridfoundation.implementation.consumers.crafters.VanillaAutoCrafter;
import io.github.mooy1.gridfoundation.implementation.consumers.gens.CobbleGen;
import io.github.mooy1.gridfoundation.implementation.consumers.machines.single.presses.GearCaster;
import io.github.mooy1.gridfoundation.implementation.consumers.machines.single.presses.PlatePress;
import io.github.mooy1.gridfoundation.implementation.consumers.machines.single.presses.WireDrawer;
import io.github.mooy1.gridfoundation.implementation.generators.machines.SurvivalGenerator;
import io.github.mooy1.gridfoundation.implementation.generators.panels.LunarPanel;
import io.github.mooy1.gridfoundation.implementation.generators.panels.SolarPanel;
import io.github.mooy1.gridfoundation.implementation.grid.GridViewer;
import io.github.mooy1.gridfoundation.implementation.materials.Alloy;
import io.github.mooy1.gridfoundation.implementation.materials.CrushedBlock;
import io.github.mooy1.gridfoundation.implementation.materials.Dust;
import io.github.mooy1.gridfoundation.implementation.materials.InfusionShard;
import io.github.mooy1.gridfoundation.implementation.materials.Ingot;
import io.github.mooy1.gridfoundation.implementation.materials.CrushedOre;
import io.github.mooy1.gridfoundation.implementation.tools.Hammer;
import io.github.mooy1.gridfoundation.implementation.upgrades.UpgradeKit;
import io.github.mooy1.gridfoundation.implementation.upgrades.UpgradeType;
import io.github.mooy1.gridfoundation.implementation.wireless.WirelessConfigurator;
import io.github.mooy1.gridfoundation.implementation.wireless.WirelessInputNode;
import io.github.mooy1.gridfoundation.implementation.wireless.WirelessOutputNode;
import io.github.mooy1.infinitylib.PluginUtils;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.ChatColor;
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
        new Alloy(Alloy.INFUSED, new SlimefunItemStack(SlimefunItems.SILVER_INGOT, 4), InfusionShard.ITEM, new ItemStack(Material.QUARTZ, 3)).register(plugin);
        new Alloy(Alloy.ULTIMATUM, new SlimefunItemStack(SlimefunItems.REINFORCED_PLATE, 8), new SlimefunItemStack(Ingot.PLATINUM, 16), new SlimefunItemStack(Alloy.INFUSED, 16)).register(plugin);
        
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
        
        new Dust(Dust.PLATINUM, CrushedOre.PLATINUM, Ingot.PLATINUM).register(plugin);
        new Dust(Dust.COBALT, CrushedOre.COBALT, SlimefunItems.COBALT_INGOT).register(plugin);
        new Dust(Dust.NICKEL, CrushedOre.NICKEL, SlimefunItems.NICKEL_INGOT).register(plugin);

        new Ingot(Ingot.PLATINUM, Dust.PLATINUM).register(plugin);

        new CrushedOre(CrushedOre.COPPER, SlimefunItems.COPPER_DUST, SlimefunItems.COPPER_INGOT).register(plugin);
        new CrushedOre(CrushedOre.GOLD, SlimefunItems.GOLD_DUST, new ItemStack(Material.GOLD_INGOT)).register(plugin);
        new CrushedOre(CrushedOre.IRON, SlimefunItems.IRON_DUST, new ItemStack(Material.IRON_INGOT)).register(plugin);
        new CrushedOre(CrushedOre.ZINC, SlimefunItems.ZINC_DUST, SlimefunItems.ZINC_INGOT).register(plugin);
        new CrushedOre(CrushedOre.TIN, SlimefunItems.TIN_DUST, SlimefunItems.TIN_INGOT).register(plugin);
        new CrushedOre(CrushedOre.LEAD, SlimefunItems.LEAD_DUST, SlimefunItems.LEAD_INGOT).register(plugin);
        new CrushedOre(CrushedOre.SILVER, SlimefunItems.SILVER_DUST, SlimefunItems.SILVER_INGOT).register(plugin);
        new CrushedOre(CrushedOre.ALUMINUM, SlimefunItems.ALUMINUM_DUST, SlimefunItems.ALUMINUM_INGOT).register(plugin);
        new CrushedOre(CrushedOre.MAGNESIUM, SlimefunItems.MAGNESIUM_DUST, SlimefunItems.MAGNESIUM_INGOT).register(plugin);
        new CrushedOre(CrushedOre.PLATINUM, Ingot.PLATINUM).register(plugin);
        new CrushedOre(CrushedOre.COBALT, SlimefunItems.COBALT_INGOT).register(plugin);
        new CrushedOre(CrushedOre.NICKEL, SlimefunItems.NICKEL_INGOT).register(plugin);
        
        new InfusionShard().register(plugin);
        
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
        
        //TODO: crucible
        
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
        
        // don't do basic or the first level
        for (int i = 2 ; i < UpgradeType.values().length ; i++) {
            new UpgradeKit(UpgradeType.values()[i]).register(plugin);
        }
        
        // tools

        new WirelessConfigurator().register(plugin);
        new WirelessInputNode().register(plugin);
        new WirelessOutputNode().register(plugin);
        
        new Hammer(Material.DIAMOND_PICKAXE, new ItemStack(Material.DIAMOND), ChatColor.AQUA, 3).register(plugin);
        new Hammer(Material.IRON_PICKAXE, SlimefunItems.REINFORCED_ALLOY_INGOT, ChatColor.GRAY, 5).register(plugin);
        
        // TODO workbench, wand, hammers, gear for advanced ingots
        
    }

    }
