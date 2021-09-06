/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dorianmercier.cubeassemble.inventories;

import com.dorianmercier.cubeassemble.common.log;
import static com.dorianmercier.cubeassemble.inventories.tools.createDisplay;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author doria
 */
public class setup implements Listener{
    private static final Inventory inv = Bukkit.createInventory(null, 9, "Configuration de la partie");

    public setup(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
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

    // Check for clicks on items
    @EventHandler
    public static void onInventoryClick(final InventoryClickEvent e) {
        if (e.getInventory() != inv) return;
        e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();

        // verify current item is not null
        if (clickedItem == null || clickedItem.getType().isAir()) return;
        final Player p = (Player) e.getWhoClicked();

        // Using slots click is a best option for your inventory click's
        p.sendMessage("You clicked at slot " + e.getRawSlot());
    }

    // Cancel dragging in our inventory
    @EventHandler
    public static void onInventoryClick(final InventoryDragEvent e) {
        if (e.getInventory().equals(inv)) {
          e.setCancelled(true);
        }
    }
}
