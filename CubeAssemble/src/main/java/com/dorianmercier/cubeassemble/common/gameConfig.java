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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author doria
 */
public class gameConfig {
    public static boolean team_freezed = false;
    public static ArrayList<String> hostList = new ArrayList<String>();
    public static int numberTeams;
    public static final HashMap<String, ArrayList<String>> listTeams = new HashMap<>();
    public static LinkedHashMap<Material, Integer> blocksConfig = new LinkedHashMap<>();
    public static ArrayList<blocksInventory> invBlocks = new ArrayList<>();
    public static ArrayList<blockConfigInventory> invBlockConfig = new ArrayList<>();
    
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
}
