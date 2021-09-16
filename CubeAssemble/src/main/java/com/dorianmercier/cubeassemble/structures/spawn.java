/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dorianmercier.cubeassemble.structures;

import com.dorianmercier.cubeassemble.common.log;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;

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
        return true;
    }
}
