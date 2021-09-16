/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dorianmercier.cubeassemble.common;

import com.dorianmercier.cubeassemble.inventories.blockConfigInventory;
import com.dorianmercier.cubeassemble.inventories.blockMenue;
import com.dorianmercier.cubeassemble.inventories.blocksInventory;
import com.dorianmercier.cubeassemble.inventories.setup;
import com.dorianmercier.cubeassemble.inventories.setup_teams;
import com.dorianmercier.cubeassemble.inventories.teams;
import com.dorianmercier.cubeassemble.inventories.tools;
import static com.dorianmercier.cubeassemble.inventories.tools.createDisplay;
import static java.lang.Integer.max;
import static java.lang.Integer.min;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
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
        
        // verify current item is not null
        final ItemStack clickedItem = e.getCurrentItem();
        Inventory inv = e.getInventory();
        if (clickedItem == null || clickedItem.getType().isAir()) return;
        
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
        
        if(!gameConfig.hostList.contains(player.getName())) {
            //Freezing inventory for players in hub
            e.setCancelled(true);
            return;
        }
        
        
        //Setup menue
        if(inv.equals(setup.inv)) {
            e.setCancelled(true);

            if (clickedItem.getType().equals(Material.GRASS_BLOCK)) {
                blockMenue.openInventory(player);
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
                log.info("The player " +  player.getName() + " freezed the teams");
                tools.createDisplay(Material.LIME_TERRACOTTA, 1, inv, 8, "Dévérouiller les teams", "");
            }else if(clickedItem.getType().equals(Material.LIME_TERRACOTTA)) {
                gameConfig.team_freezed = false;
                tools.createDisplay(Material.ICE, 1, inv, 8, "Vérouiller les teams", "");
                log.info("The player " +  player.getName() + " unfreezed the teams");
            }
            
            
            if(resetTeams) {
                log.info("The player " + player.getName() + " updated teams number to " + numberTeams);
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
        
        
        
        if(inv.equals(blockMenue.inv)) {
            e.setCancelled(true);
            switch (clickedItem.getType()) {
                case STONE:
                    blocksInventory.openInventory(player);
                    break;
                case GOLD_INGOT:
                    gameConfig.invBlockConfig.get(0).openInventory(player);
                    gameConfig.currentConfigPage.put(player, 0);
                    break;
                case ARROW:
                    setup.openInventory(player);
                    break;
                default:
                    break;
            }
            return;
        }
        if(gameConfig.arrayContainsInventory(inv)) {
            e.setCancelled(true);
            int k = e.getSlot();
            int nbPoints;
            if(0 <= k && k <= 8) {
                ItemStack item = inv.getItem(k + 18);
                if(item != null) {
                    Material material = item.getType();
                    nbPoints = gameConfig.getPoints(material);
                    gameConfig.setPoints(material, ++nbPoints);
                    createDisplay(Material.GOLD_NUGGET, min(nbPoints, 64), inv, k + 27, "" + nbPoints, "");
                    log.info("The player " + player.getName() + " updated the points for " + material + " to " + nbPoints);
                }
                return;
            }
            if(9 <= k && k <= 17) {
                ItemStack item = inv.getItem(k + 9);
                if(item != null) {
                    Material material = item.getType();
                    nbPoints = max(gameConfig.getPoints(material) - 1, 0);
                    gameConfig.setPoints(material, nbPoints);
                    createDisplay(Material.GOLD_NUGGET, min(nbPoints, 64), inv, k + 18, "" + nbPoints, "");
                    log.info("The player " + player.getName() + " updated the points for " + material + " to " + nbPoints);
                }
                return;
            }
            if(k==45) {
                Integer currentPage = gameConfig.currentConfigPage.get(player);
                if(currentPage == null || currentPage == 0) {
                    blockMenue.openInventory(player);
                }
                else {
                    gameConfig.invBlockConfig.get(currentPage - 1).openInventory(player);
                    gameConfig.currentConfigPage.put(player, currentPage - 1);
                }
                return;
            }
            if(k==53) {
                Integer currentPage = gameConfig.currentConfigPage.get(player);
                gameConfig.invBlockConfig.get(currentPage + 1).openInventory(player);
                gameConfig.currentConfigPage.put(player, currentPage + 1);
                return;
            }
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
        if(!dataBase.isPlayer(e.getPlayer())) dataBase.addPlayer(e.getPlayer());
    }
    
    @EventHandler
    public static void onInteractPlayer(final PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if(player.getInventory().getItemInMainHand().equals(new ItemStack(Material.WHITE_BANNER))) {
            e.setCancelled(true);
            if(!gameConfig.team_freezed) teams.openInventory(player);
        }
    }
    
    @EventHandler
    public static void onCloseInventory(InventoryCloseEvent e) {
        if(e.getInventory().equals(blocksInventory.inv)) {
            log.info("The player " + e.getPlayer().getName() + " updated the required blocks");
            gameConfig.updateBlocksConfig(e.getInventory());
            int nbBlocks = gameConfig.blocksConfig.size();
            int indexFirst;
            gameConfig.invBlockConfig.clear();
            blockConfigInventory.nbInventories = 0;
            for(indexFirst=0; indexFirst<nbBlocks; indexFirst+=9) {
                gameConfig.invBlockConfig.add(new blockConfigInventory());
            }
        }
    }
}
