/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dorianmercier.cubeassemble.inventories;

import com.dorianmercier.cubeassemble.common.gameConfig;
import com.dorianmercier.cubeassemble.common.log;
import static com.dorianmercier.cubeassemble.inventories.tools.createDisplay;
import static java.lang.Integer.min;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

/**
 *
 * @author doria
 */
public class blockConfigInventory {
    public Inventory inv;
    public int index;
    public static int nbInventories = 0;
    
    
    public blockConfigInventory() {
        inv = Bukkit.createInventory(null, 54, "Configuration des points");
        index = ++nbInventories;
        setupBase();
        setupBlocks();
        
    }
    
    public void setupBlocks() {
        int k, nbPoints;
        ArrayList<Map.Entry<Material, Integer>> ListBlocks = new ArrayList<>(gameConfig.blocksConfig.entrySet());
        Material material;
        for(k=0; k<9; k++) {
            try {
                material = ListBlocks.get(k + (index-1)*9 ).getKey();
                nbPoints = ListBlocks.get(k + (index-1)*9 ).getValue();
                createDisplay(material, 1, inv, k + 18, material.getKey().toString().split(":")[1], "");
                createDisplay(Material.GOLD_NUGGET, min(nbPoints, 64), inv, k + 27, "" + nbPoints, "");
            }
            catch(Exception e) {
                //Wee reached the end of the blocks. Nothing else to add to the inventory
                break;
            }
        }
    }
    
    public void setupBase() {
        int k;
        
        ItemStack Banner = new ItemStack(Material.GREEN_BANNER, 1);
        BannerMeta metaGreen = (BannerMeta) Banner.getItemMeta();
        
        List<Pattern> patterns = new ArrayList<>();
        patterns.add(new Pattern(DyeColor.BLACK, PatternType.STRAIGHT_CROSS));
        patterns.add(new Pattern(DyeColor.LIME, PatternType.BORDER));
        patterns.add(new Pattern(DyeColor.LIME, PatternType.STRIPE_TOP));
        patterns.add(new Pattern(DyeColor.LIME, PatternType.STRIPE_BOTTOM));
        metaGreen.setPatterns(patterns);
        metaGreen.setDisplayName("+1");
        
        
        Banner = new ItemStack(Material.RED_BANNER, 1);
        BannerMeta metaRed = (BannerMeta) Banner.getItemMeta();
        
        patterns.clear();
        patterns.add(new Pattern(DyeColor.BLACK, PatternType.STRIPE_MIDDLE));
        patterns.add(new Pattern(DyeColor.RED, PatternType.BORDER));

        metaRed.setPatterns(patterns);
        metaRed.setDisplayName("-1");
        
        for(k=0; k<9; k++) {
            createDisplay(Material.LIME_BANNER, 1, inv, k, "+1","");
            createDisplay(Material.RED_BANNER, 1, inv, k + 9, "-1","");
            inv.getItem(k).setItemMeta(metaGreen);
            inv.getItem(k + 9).setItemMeta(metaRed);
        }
        
        //Setup return button
        if(index == 1) {
           createDisplay(Material.ARROW, 1, inv, 45, "Retour","");
        }
        else {
            createDisplay(Material.ARROW, 1, inv, 45, "Précédent","");
            //Setup next button for the previous inventory
            createDisplay(Material.MAGENTA_GLAZED_TERRACOTTA, 1, gameConfig.invBlockConfig.get(index - 2).inv, 53, "Suivant", "");
        }
        
        
        
    }
    
    // You can open the inventory with this
    public void openInventory(final HumanEntity ent) {
        ent.openInventory(inv);
    }
}
