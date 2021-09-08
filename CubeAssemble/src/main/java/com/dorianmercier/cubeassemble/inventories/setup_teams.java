/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dorianmercier.cubeassemble.inventories;

import static com.dorianmercier.cubeassemble.inventories.tools.createDisplay;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author doria
 */
public class setup_teams{
    public static final Inventory inv = Bukkit.createInventory(null, 9, "Configuration des équipes");

    public setup_teams() {
        // Put the items into the inventory
        initializeItems();
    }

    // You can call this whenever you want to put the items in
    public static void initializeItems() {
        //Placing items in the inventory
        createDisplay(Material.LIME_BANNER, 1, inv, 3, "+1","Ajouter une équipe");
        createDisplay(Material.WHITE_BANNER, 2, inv, 4, "2", "Nombre d'équipes");
        createDisplay(Material.RED_BANNER, 1, inv, 5, "-1", "Supprimer une équipe");
        createDisplay(Material.ARROW, 1, inv, 0, "Retour","");
        
        //Creating banner style for plus
        ItemStack Banner = inv.getItem(3);
        BannerMeta m = (BannerMeta) Banner.getItemMeta();
        
        List<Pattern> patterns = new ArrayList<Pattern>();
        patterns.add(new Pattern(DyeColor.BLACK, PatternType.STRAIGHT_CROSS));
        patterns.add(new Pattern(DyeColor.LIME, PatternType.BORDER));
        patterns.add(new Pattern(DyeColor.LIME, PatternType.STRIPE_TOP));
        patterns.add(new Pattern(DyeColor.LIME, PatternType.STRIPE_BOTTOM));
        m.setPatterns(patterns);
        Banner.setItemMeta(m);
        
        //Creating banner style for minus
        Banner = inv.getItem(5);
        m = (BannerMeta) Banner.getItemMeta();
        
        patterns.clear();
        patterns.add(new Pattern(DyeColor.BLACK, PatternType.STRIPE_MIDDLE));
        patterns.add(new Pattern(DyeColor.RED, PatternType.BORDER));

        m.setPatterns(patterns);
        Banner.setItemMeta(m);
    }

    // You can open the inventory with this
    public static void openInventory(final HumanEntity ent) {
        ent.openInventory(inv);
        ent.sendMessage("Configuration des équipes");
    }
}
