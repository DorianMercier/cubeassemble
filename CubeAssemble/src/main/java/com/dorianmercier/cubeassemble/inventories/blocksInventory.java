/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dorianmercier.cubeassemble.inventories;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;

/**
 *
 * @author doria
 */
public class blocksInventory {
    
    public static Inventory inv;
    
    
    public blocksInventory() {
        inv = Bukkit.createInventory(null, 54, "Choix des blocs");
    }

    // You can open the inventory with this
    public static void openInventory(final HumanEntity ent) {
        ent.openInventory(inv);
    }
}
