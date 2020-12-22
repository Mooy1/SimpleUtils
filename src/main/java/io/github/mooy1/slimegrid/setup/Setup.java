package io.github.mooy1.slimegrid.setup;

import io.github.mooy1.slimegrid.SlimeGrid;
import io.github.mooy1.slimegrid.implementation.generators.GridItemGenerator;
import io.github.mooy1.slimegrid.implementation.generators.GridPanel;
import io.github.mooy1.slimegrid.lists.Categories;
import io.github.mooy1.slimegrid.lists.Items;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class Setup {
    
    public static void setup(SlimeGrid plugin) {
        
        new SlimefunItem(Categories.COMPONENTS, Items.ADDON_INFO, RecipeType.NULL, null).register(plugin);
        
        new GridPanel(GridPanel.I_DAY, GridPanel.I_NIGHT, Items.GRID_PANEL_I, new ItemStack[] {
                
        }).register(plugin);
        new GridPanel(GridPanel.II_DAY, GridPanel.II_NIGHT, Items.GRID_PANEL_II, new ItemStack[] {

        }).register(plugin);
        new GridPanel(GridPanel.III_DAY, GridPanel.III_NIGHT, Items.GRID_PANEL_III, new ItemStack[] {

        }).register(plugin);
        new GridPanel(GridPanel.IV_DAY, GridPanel.IV_NIGHT, Items.GRID_PANEL_IV, new ItemStack[] {

        }).register(plugin);
        new GridPanel(GridPanel.V_DAY, GridPanel.V_NIGHT, Items.GRID_PANEL_V, new ItemStack[] {

        }).register(plugin);
        new GridPanel(GridPanel.VI_DAY, GridPanel.VI_NIGHT, Items.GRID_PANEL_VI, new ItemStack[] {

        }).register(plugin);
        new GridPanel(GridPanel.VII_DAY, GridPanel.VII_NIGHT, Items.GRID_PANEL_VII, new ItemStack[] {

        }).register(plugin);
        
        new GridItemGenerator(Items.COBBLE_GEN_I, GridItemGenerator.COBBLE_I, GridItemGenerator.COBBLE_I, Material.COBBLESTONE, new ItemStack[] {
                
        }).register(plugin);
        new GridItemGenerator(Items.COBBLE_GEN_II, GridItemGenerator.COBBLE_II, GridItemGenerator.COBBLE_II, Material.COBBLESTONE, new ItemStack[] {

        }).register(plugin);
        new GridItemGenerator(Items.COBBLE_GEN_III, GridItemGenerator.COBBLE_III, GridItemGenerator.COBBLE_III, Material.COBBLESTONE, new ItemStack[] {

        }).register(plugin);
        new GridItemGenerator(Items.COBBLE_GEN_IV, GridItemGenerator.COBBLE_IV, GridItemGenerator.COBBLE_IV, Material.COBBLESTONE, new ItemStack[] {

        }).register(plugin);
        
        new SlimefunItem(Categories.COMPONENTS, , RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                
        }).register(plugin);
        
    }

}
