/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dorianmercier.cubeassemble.common;

import com.dorianmercier.cubeassemble.inventories.blockConfigInventory;
import com.dorianmercier.cubeassemble.inventories.blocksInventory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
    
    //Freeze teams and do not allow player to change their team (moderator can still change player's team)
    public static boolean team_freezed = false;
    //List of players authorized to perform config commands
    public static ArrayList<String> hostList = new ArrayList<String>();
    //Total number of teams
    public static int numberTeams;
    //List of teams and players
    public static final HashMap<String, ArrayList<String>> listTeams = new HashMap<>();
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
    
    public static boolean addBlock(Material material) {
        return blocksConfig.putIfAbsent(material, 0) != null;
    }
    
    public static boolean setPoints(Material material, int points) {
        if(!blocksConfig.containsKey(material)) return false;
        blocksConfig.put(material, points);
        return true;
    }
    
    public static Integer getPoints(Material material) {
        if(!blocksConfig.containsKey(material)) return null;
        return blocksConfig.get(material);
    }
    
    public static void updateBlocksConfig(Inventory bInv) {
        blockConfigInventory.nbInventories = 0;
        LinkedHashMap<Material, Integer> blocksConfigUpdated = new LinkedHashMap<>();
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
}
