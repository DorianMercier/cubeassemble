/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dorianmercier.cubeassemble.inventories;

import static com.dorianmercier.cubeassemble.inventories.tools.createDisplay;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;

/**
 *
 * @author doria
 */
public class blockMenue {
    public static final Inventory inv = Bukkit.createInventory(null, 9, "Configuration des blocs");

    public blockMenue() {
        initializeItems();
    }

    public static void initializeItems() {
        createDisplay(Material.STONE, 1, inv, 3, "Choix des blocs","");
        createDisplay(Material.GOLD_INGOT, 1, inv, 5, "Configurationd des points","");
        createDisplay(Material.ARROW, 1, inv, 0, "Retour","");
        
    }

    public static void openInventory(final HumanEntity ent) {
        ent.openInventory(inv);
    }
}
