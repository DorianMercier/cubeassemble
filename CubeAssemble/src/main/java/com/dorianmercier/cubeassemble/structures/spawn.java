/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dorianmercier.cubeassemble.structures;

import com.dorianmercier.cubeassemble.common.log;
import org.bukkit.Bukkit;
import static org.bukkit.Bukkit.getLogger;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

/**
 *
 * @author doria
 */
public final class spawn {
    
    public spawn() {
    }
    
    //Method to build or destroy the spawn. If value is true we build the spawn, otherwise we destroy it
    public static boolean build(boolean value) {
        Material materialFloor, materialWall;
        World world = Bukkit.getWorld("world");
        if(value) {
            log.info("Building the spawn");
            materialFloor = Material.WHITE_STAINED_GLASS;
            materialWall = Material.WHITE_STAINED_GLASS_PANE;
        }
        else {
            log.info("Cleaning the spawn");
            materialFloor = Material.AIR;
            materialWall = Material.AIR;
            //Deleting buttons first
            world.getBlockAt(0,252,10).setType(Material.AIR);
            world.getBlockAt(0,252,-10).setType(Material.AIR);
        }
        int x, y, z;
        log.info("Processing the floor");
        //Building the floor
        for(x = -11; x <= 11; x++) {
            for(z = -11; z <= 11; z++) {
                world.getBlockAt(x, 250, z).setType(materialFloor);
            }
        }
        
        log.info("Processing walls");
        //Building walls
        for(y=251; y<= 253; y++) {
            for(z=-10; z<=10; z++) {
                world.getBlockAt(11,y,z).setType(materialWall,true);
            }
        }
        for(y=251; y<= 253; y++) {
            for(x=-10; x<=10; x++) {
                world.getBlockAt(x,y,11).setType(materialWall,true);
            }
        }
        for(y=251; y<= 253; y++) {
            for(z=-10; z<=10; z++) {
                world.getBlockAt(-11,y,z).setType(materialWall,true);
            }
        }
        for(y=251; y<= 253; y++) {
            for(x=-10; x<=10; x++) {
                world.getBlockAt(x,y,-11).setType(materialWall,true);
            }
        }
        for(y=251;y<=253;y++) {
            //Placing corners at the end to ensure block connexions
            world.getBlockAt(-11,y,-11).setType(materialWall,true);
            world.getBlockAt(11,y,-11).setType(materialWall,true);
            world.getBlockAt(-11,y,11).setType(materialWall,true);
            world.getBlockAt(11,y,11).setType(materialWall,true);
        }
        log.info("Processing colored blocks and buttons");
        if(value) {
            //Placing color blocks, buttons and commandBlocks
            //Red
            world.getBlockAt(0,252,11).setType(Material.RED_CONCRETE,true);
            world.getBlockAt(0,252,10).setType(Material.STONE_BUTTON,true);
            
            Block button = world.getBlockAt(0,252,10);
            button.setBlockData(Material.STONE_BUTTON.createBlockData("[facing=north]"));
            //Blue
            world.getBlockAt(0,252,-11).setType(Material.BLUE_CONCRETE,true);
            world.getBlockAt(0,252,-10).setType(Material.STONE_BUTTON,true);
            
            button = world.getBlockAt(0,252,-10);
            button.setBlockData(Material.STONE_BUTTON.createBlockData("[facing=south]"));
        }
       
        return true;
    }
}
