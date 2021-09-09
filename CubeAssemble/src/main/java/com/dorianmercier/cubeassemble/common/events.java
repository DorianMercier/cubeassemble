/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dorianmercier.cubeassemble.common;

import com.dorianmercier.cubeassemble.inventories.setup;
import com.dorianmercier.cubeassemble.inventories.setup_teams;
import com.dorianmercier.cubeassemble.inventories.teams;
import com.dorianmercier.cubeassemble.inventories.tools;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author doria
 */
public class events implements Listener{
    public events(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public static void onInventoryClick(final InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        /*TODO Modify if condition later to allow inventory interations in the blocks room
          TODO Add an admin tag in order to make possible to open the menue in adventure mode*/
        if(player.getGameMode().equals(GameMode.ADVENTURE)) {
            //Freezing inventory for players in hub
            e.setCancelled(true);
            return;
        }
        // verify current item is not null
        final ItemStack clickedItem = e.getCurrentItem();
        Inventory inv = e.getInventory();
        if (clickedItem == null || clickedItem.getType().isAir()) return;
        
        //Setup menue
        if(inv.equals(setup.inv)) {
            e.setCancelled(true);

            if (clickedItem.getType().equals(Material.GRASS_BLOCK)) {
                player.sendMessage("Grass block");
            }
            else if(clickedItem.getType().equals(Material.RED_BANNER)) {
                setup_teams.openInventory(player);
            }
            return;
        }
        
        //Team manager menue
        if(inv.equals(setup_teams.inv)) {
            e.setCancelled(true);
            boolean resetTeams = false;
            int numberTeams = inv.getItem(4).getAmount();
            if (clickedItem.getType().equals(Material.LIME_BANNER) && numberTeams < 9) {
                inv.getItem(4).setAmount(++ numberTeams);
                resetTeams = true;
            }
            else if(clickedItem.getType().equals(Material.RED_BANNER) && numberTeams > 2) {
                inv.getItem(4).setAmount(-- numberTeams);
                resetTeams = true;
            }
            else if(clickedItem.getType().equals(Material.ARROW)) {
                setup.openInventory(player);
            }else if(clickedItem.getType().equals(Material.ICE)) {
                gameConfig.team_freezed = true;
                tools.createDisplay(Material.LIME_TERRACOTTA, 1, inv, 8, "Teams vérouillées", "");
            }else if(clickedItem.getType().equals(Material.LIME_TERRACOTTA)) {
                gameConfig.team_freezed = false;
                tools.createDisplay(Material.ICE, 1, inv, 8, "Vérouiller les teams", "");
            }
            
            
            if(resetTeams) {
                teamManager.resetTeams();
                teams.inv.clear();
                new teams();
                Material[] orderedTeams = {Material.GREEN_BANNER, Material.YELLOW_BANNER, Material.ORANGE_BANNER, Material.PINK_BANNER, Material.BLACK_BANNER, Material.GRAY_BANNER, Material.CYAN_BANNER};
                String[] teamsColors = {"Vert", "Jaune", "Orange", "Rose", "Noir", "Gris", "Cyan"};
                for(int k = 3; k <= numberTeams; k++) {
                    teams.inv.setItem(k-1, new ItemStack(orderedTeams[k-3], 1));
                    ItemStack item = teams.inv.getItem(k-1);
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(teamsColors[k-3]);
                    item.setItemMeta(meta);
                }
            }
            return;
        }
        
        //Choice team menue
        if(inv.equals(teams.inv)) {
            e.setCancelled(true);

            // Using slots click is a best option for your inventory click's
            switch (clickedItem.getType()) {
                case BLUE_BANNER:
                    teamManager.addPlayer(main.blue, player);
                    break;
                case RED_BANNER:
                    teamManager.addPlayer(main.red, player);
                    break;
                case GREEN_BANNER:
                    teamManager.addPlayer(main.green, player);
                    break;
                case YELLOW_BANNER:
                    teamManager.addPlayer(main.yellow, player);
                    break;
                case ORANGE_BANNER:
                    teamManager.addPlayer(main.orange, player);
                    break;
                case PINK_BANNER:
                    teamManager.addPlayer(main.pink, player);
                    break;
                case BLACK_BANNER:
                    teamManager.addPlayer(main.black, player);
                    break;
                case GRAY_BANNER:
                    teamManager.addPlayer(main.gray, player);
                    break;
                case CYAN_BANNER:
                    teamManager.addPlayer(main.cyan, player);
                    break;
                default:
                    break;
            }
            player.closeInventory();
            return;
        }
    }
    
    @EventHandler
    public static void onInventoryClick(final InventoryDragEvent e) {
        Inventory inv = e.getInventory();
        Player player = (Player) e.getWhoClicked();
        if (player.getGameMode().equals(GameMode.ADVENTURE) || inv.equals(setup.inv) || inv.equals(setup_teams.inv)) {
          e.setCancelled(true);
        }
    }
    
    @EventHandler
    public static void onDropPlayer(final PlayerDropItemEvent e) {
        Player player = (Player) e.getPlayer();
        if (player.getGameMode().equals(GameMode.ADVENTURE)) {
        e.setCancelled(true);
        }
    }
    
    @EventHandler
    public static void onSwipeHandPlayer(final PlayerSwapHandItemsEvent e) {
        Player player = (Player) e.getPlayer();
        if (player.getGameMode().equals(GameMode.ADVENTURE)) {
        e.setCancelled(true);
        }
    }
    
    @EventHandler
    public static void onJoinPlayer(final PlayerJoinEvent e) {
        e.getPlayer().getInventory().clear();
        e.getPlayer().getInventory().setItem(0, new ItemStack(Material.WHITE_BANNER, 1));
    }
    
    @EventHandler
    public static void onInteractPlayer(final PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if(player.getInventory().getItemInMainHand().equals(new ItemStack(Material.WHITE_BANNER))) {
            e.setCancelled(true);
            if(!gameConfig.team_freezed) teams.openInventory(player);
        }
    }
    
   
}
