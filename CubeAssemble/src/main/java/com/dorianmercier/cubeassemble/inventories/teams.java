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
public class teams {
    public static final Inventory inv = Bukkit.createInventory(null, 9, "Choix des équipes");

    public teams() {
        // Put the items into the inventory
        initializeItems();
    }

    // You can call this whenever you want to put the items in
    public static void initializeItems() {
        //Placing items in the inventory
        createDisplay(Material.BLUE_BANNER, 1, inv, 0, "Bleu","");
        createDisplay(Material.RED_BANNER, 1, inv, 1, "Rouge","");
    }

    // You can open the inventory with this
    public static void openInventory(final HumanEntity ent) {
        ent.openInventory(inv);
    }
}
