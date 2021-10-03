/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dorianmercier.cubeassemble.inventories;

import com.dorianmercier.cubeassemble.common.log;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;

/**
 *
 * @author doria
 */
public class blocksInventory {
    
    public Inventory inv;
    public int index;
    public static int nbInventories = 0;
    
    
    public blocksInventory() {
        inv = Bukkit.createInventory(null, 54, "Choix des blocs");
        index = ++nbInventories;
        setupBase();
    }

    // You can open the inventory with this
    public void openInventory(final HumanEntity ent) {
        ent.openInventory(inv);
    }
    
    public final void setupBase() {
        int k;
        for(k=46; k<53; k++) {
            tools.createDisplay(Material.BARRIER, 1, inv, k, "interdit", "");
        }
        if(index < 5) tools.createDisplay(Material.MAGENTA_GLAZED_TERRACOTTA, 1, inv, 53, "Page suivante", "");
        else tools.createDisplay(Material.BARRIER, 1, inv, 53, "interdit", "");
        if(index != 1) tools.createDisplay(Material.ARROW, 1, inv, 45, "Page précédente", "");
        else {
            log.info("On est dans le else");
            tools.createDisplay(Material.ARROW, 1, inv, 45, "Retour", "");
        }
    }
}
