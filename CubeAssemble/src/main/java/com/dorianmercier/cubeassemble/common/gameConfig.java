/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dorianmercier.cubeassemble.common;

import com.dorianmercier.cubeassemble.inventories.blockConfigInventory;
import com.dorianmercier.cubeassemble.inventories.blocksInventory;
import com.dorianmercier.cubeassemble.inventories.setup_teams;
import com.dorianmercier.cubeassemble.inventories.teams;
import com.dorianmercier.cubeassemble.inventories.tools;
import com.dorianmercier.cubeassemble.structures.blockRooms;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author doria
 */
public class gameConfig {
    
    public static final String version = "v2.0";
    
    public static int gamePhase = 0;
    
    public static String[] teamsColors = {"Vert", "Jaune", "Orange", "Rose", "Noir", "Gris", "Cyan"};
    
    public static int previousNumberTeams;
    public static int previousNumberItems;
    
    public static ArrayList<Player> canClick = new ArrayList<>();
    public static ArrayList<Player> onInvConfig = new ArrayList<>();
    
    public static int numberTeams;
    //Freeze teams and do not allow player to change their team (moderator can still change player's team)
    public static boolean team_freezed = false;
    //List of players authorized to perform config commands
    public static ArrayList<String> hostList = new ArrayList<String>();
    //List of teams and players
    public static HashMap<String, ArrayList<String>> listTeams = new HashMap<>();
    
    public static HashMap<String, String> playerLinkedTeam;
    //All blocks needed for the game and their points are referenced here
    public static LinkedHashMap<Material, Integer> blocksConfig = new LinkedHashMap<>();
    //List of all inventories containing blocks needed
    public static ArrayList<blocksInventory> invBlocks = new ArrayList<>();
    //List of inventories in order to configure points of blocks
    public static ArrayList<blockConfigInventory> invBlockConfig = new ArrayList<>();
    //Current page of invBlockConfig where players are currently. Usefull to know the next and previous page
    public static HashMap<Player, Integer> currentConfigPage = new HashMap<>();
    
    //Current page of invBlocks where players are currently. Usefull to know the next and previous page
    public static HashMap<Player, Integer> currentBlocksPage = new HashMap<>();
    
    public static ArrayList<Material> nonCompatible = new ArrayList<>();
    
    public static HashMap<String, Location> roomsLocations = new HashMap<>();
    
    public static ItemStack compas = new ItemStack(Material.COMPASS);
    
    static {
        listTeams.put("Bleu", new ArrayList<>());
        listTeams.put("Rouge", new ArrayList<>());
        listTeams.put("Vert", new ArrayList<>());
        listTeams.put("Jaune", new ArrayList<>());
        listTeams.put("Orange", new ArrayList<>());
        listTeams.put("Rose", new ArrayList<>());
        listTeams.put("Noir", new ArrayList<>());
        listTeams.put("Gris", new ArrayList<>());
        listTeams.put("Cyan", new ArrayList<>());
        
        invBlockConfig.add(new blockConfigInventory());
        
        nonCompatible.add(Material.BLUE_STAINED_GLASS);
        nonCompatible.add(Material.RED_STAINED_GLASS);
        nonCompatible.add(Material.BLUE_STAINED_GLASS);
        nonCompatible.add(Material.GREEN_STAINED_GLASS);
        nonCompatible.add(Material.YELLOW_STAINED_GLASS);
        nonCompatible.add(Material.ORANGE_STAINED_GLASS);
        nonCompatible.add(Material.PINK_STAINED_GLASS);
        nonCompatible.add(Material.BLACK_STAINED_GLASS);
        nonCompatible.add(Material.GRAY_STAINED_GLASS);
        nonCompatible.add(Material.CYAN_STAINED_GLASS);
        nonCompatible.add(Material.BARRIER);
        nonCompatible.add(Material.AIR);
        
        //Construction of teleporting item
        
        ItemMeta compasMeta = compas.getItemMeta();
        compasMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 0, true);
        compasMeta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
        ArrayList<String> lore = new ArrayList<>();
        lore.add("Clic droit pour vous téléporter");
        compasMeta.setLore(lore);
        compasMeta.setDisplayName("Outil de téléportation");
        compas.setItemMeta(compasMeta);
        
        //Initialize inventories containig required blocks
        for(int k=0; k<5; k++) invBlocks.add(new blocksInventory());
    }
    
    public static void loadConfig() {
        
        numberTeams = dataBase.getTeamsNumber();
        previousNumberTeams = dataBase.getPreviousTeamsNumber();
        previousNumberItems = dataBase.getPreviousNumberItems();
        
        team_freezed = dataBase.getTeamsFreezed();
        
        hostList = dataBase.getHosts();
        
        playerLinkedTeam = dataBase.getListTeams();
        for(String player : playerLinkedTeam.keySet()) {
            listTeams.get(playerLinkedTeam.get(player)).add(player);
            Player realPlayer = Bukkit.getServer().getPlayerExact(player);
            if(realPlayer != null) {
                teamManager.updatePlayer(realPlayer);
            }
        }
        
        setup_teams.inv.getItem(4).setAmount(numberTeams);
        
        teams.update();
        
        if(team_freezed) tools.createDisplay(Material.LIME_TERRACOTTA, 1, setup_teams.inv, 8, "Dévérouiller les teams", "");
        
        blocksConfig = dataBase.getblocksConfig();
        int k=0;
        for(Material material : blocksConfig.keySet()) {
            tools.createDisplay(material, 1, invBlocks.get(k/45).inv, k%45, material.toString(), "");
            k++;
            log.info("Loading the config for the block " + material.toString());
        }
        updateInventoriesBlocksConfig();
        
        //Registering roomsCenters
        updateRoomsLocation();
    }
    
    public static void setGamePhase(int phase) {
        gamePhase = phase;
        main.config.getScore("gamePhase").setScore(phase);
    }
    
    public static boolean addBlock(Material material) {
        if(blocksConfig.putIfAbsent(material, 0)==null) return false;
        else {
            dataBase.addBlock(material, 0);
            return true;
        }
    }
    
    public static boolean setPoints(Material material, int points) {
        if(!blocksConfig.containsKey(material)) return false;
        blocksConfig.put(material, points);
        dataBase.setPoints(material, points);
        return true;
    }
    
    public static Integer getPoints(Material material) {
        if(!blocksConfig.containsKey(material)) return null;
        return blocksConfig.get(material);
    }
    
    public static void updateBlocksConfig() {
        blockConfigInventory.nbInventories = 0;
        LinkedHashMap<Material, Integer> blocksConfigUpdated = new LinkedHashMap<>();
        dataBase.voidBlocks();
        Material currMaterial;
        ItemStack itemstack;
        int currPoint;
        for(int k=0; k<225; k++) {
            itemstack = gameConfig.invBlocks.get(k/45).inv.getItem(k%45);
            if(itemstack != null) {
                currMaterial = itemstack.getType();
                if(blocksConfig.containsKey(itemstack.getType())) {
                    currPoint = blocksConfig.get(currMaterial);
                }
                else currPoint = 0;
                blocksConfigUpdated.put(currMaterial, currPoint);
                dataBase.addBlock(currMaterial, currPoint);
            }
        }
        blocksConfig = blocksConfigUpdated;
    }
    
    public static boolean arrayContainsInventory(Inventory inventory) {
        for(blockConfigInventory invb : invBlockConfig) {
            if(invb.inv.equals(inventory)) return true;
        }
        return false;
    }
    
    public static boolean arrayContainsBlocksInventory(Inventory inventory) {
        for(blocksInventory invb : invBlocks) {
            if(invb.inv.equals(inventory)) return true;
        }
        return false;
    }
    
    public static void updateInventoriesBlocksConfig() {
        int nbBlocks = blocksConfig.size();
        int indexFirst;
        invBlockConfig.clear();
        blockConfigInventory.nbInventories = 0;
        for(indexFirst=0; indexFirst<nbBlocks; indexFirst+=9) {
            invBlockConfig.add(new blockConfigInventory());
        }
    }
    
    public static void giveCompas(Player player) {
        player.getInventory().remove(compas);
        player.getInventory().addItem(compas);
    }
    
    public static void updateRoomsLocation() {
        ArrayList<ArrayList<Integer>> listCenters = blockRooms.getCenters(numberTeams);
        Location location;
        int k = 0;
        String[] teamsNames = {"Bleu", "Rouge", "Vert", "Jaune", "Orange", "Rose", "Noir", "Gris", "Cyan"};
        for(ArrayList<Integer> list : listCenters) {
            location = new Location(Bukkit.getWorld("world"), list.get(0), 252, list.get(1));
            roomsLocations.put(teamsNames[k++], location);
        }
    }
}
