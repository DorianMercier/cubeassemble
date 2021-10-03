/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dorianmercier.cubeassemble.inventories;

import com.dorianmercier.cubeassemble.common.gameConfig;
import static com.dorianmercier.cubeassemble.inventories.tools.createDisplay;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
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
    
    public static void update() {
        inv.clear();
        initializeItems();
        Material[] orderedTeams = {Material.GREEN_BANNER, Material.YELLOW_BANNER, Material.ORANGE_BANNER, Material.PINK_BANNER, Material.BLACK_BANNER, Material.GRAY_BANNER, Material.CYAN_BANNER};
        for(int k = 3; k <= gameConfig.numberTeams; k++) {
            inv.setItem(k-1, new ItemStack(orderedTeams[k-3], 1));
            ItemStack item = inv.getItem(k-1);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(gameConfig.teamsColors[k-3]);
            item.setItemMeta(meta);
        }
    }
}
