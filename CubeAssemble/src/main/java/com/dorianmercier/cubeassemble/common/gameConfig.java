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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author doria
 */
public class gameConfig {
    
    public static final String version = "v2.0";
    
    public static String[] teamsColors = {"Vert", "Jaune", "Orange", "Rose", "Noir", "Gris", "Cyan"};
    
    public static int previousNumberTeams;
    public static int previousNumberItems;
    
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
    //List of all inventories containing blocks needed (only one inventory is supported for now)
    public static ArrayList<blocksInventory> invBlocks = new ArrayList<>();
    //List of inventories in order to configure points of blocks
    public static ArrayList<blockConfigInventory> invBlockConfig = new ArrayList<>();
    //Current page of invBlockConfig where players are currently. Usefull to know the next and previous page
    public static HashMap<Player, Integer> currentConfigPage = new HashMap<>();
    
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
            tools.createDisplay(material, 1, blocksInventory.inv, k, material.toString(), "");
            k++;
            log.info("Loading the config for the block " + material.toString());
        }
        updateInventoriesBlocksConfig();
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
    
    public static void updateBlocksConfig(Inventory bInv) {
        blockConfigInventory.nbInventories = 0;
        LinkedHashMap<Material, Integer> blocksConfigUpdated = new LinkedHashMap<>();
        dataBase.voidBlocks();
        Material currMaterial;
        ItemStack itemstack;
        int currPoint;
        for(int k=0; k<54; k++) {
            itemstack = bInv.getItem(k);
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
    
    public static void updateInventoriesBlocksConfig() {
        int nbBlocks = blocksConfig.size();
        int indexFirst;
        invBlockConfig.clear();
        blockConfigInventory.nbInventories = 0;
        for(indexFirst=0; indexFirst<nbBlocks; indexFirst+=9) {
            invBlockConfig.add(new blockConfigInventory());
        }
    }
}
