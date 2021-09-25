/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dorianmercier.cubeassemble.inventories;

import com.dorianmercier.cubeassemble.common.dataBase;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 *
 * @author doria
 */
public class startInventory {
    public static Inventory inv = dataBase.loadInventory();
    
    public static void assign(Player player) {
        player.getInventory().setContents(inv.getContents());
    }
    
    public static void update(Inventory newInv) {
        inv.setContents(newInv.getContents());
    }
}
