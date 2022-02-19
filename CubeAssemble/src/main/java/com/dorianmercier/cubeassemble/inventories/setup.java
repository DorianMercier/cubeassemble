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
public class setup{
    public static final Inventory inv = Bukkit.createInventory(null, 9, "Configuration de la partie");

    public setup() {
        // Put the items into the inventory
        initializeItems();
    }

    // You can call this whenever you want to put the items in
    public static void initializeItems() {
        createDisplay(Material.RED_BANNER, 1, inv, 3, "Équipes", "Configuration des équipes");
        createDisplay(Material.GRASS_BLOCK, 1, inv, 5, "Blocs", "Configuration des blocs");
    }

    // You can open the inventory with this
    public static void openInventory(final HumanEntity ent) {
        ent.openInventory(inv);
        ent.sendMessage("Configuration de la partie");
    }
}
