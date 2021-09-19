/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dorianmercier.cubeassemble.structures;

import com.dorianmercier.cubeassemble.common.dataBase;
import com.dorianmercier.cubeassemble.common.gameConfig;
import com.dorianmercier.cubeassemble.common.log;
import static java.lang.Math.PI;
import static java.lang.Math.ceil;
import static java.lang.Math.cos;
import static java.lang.Math.floor;
import static java.lang.Math.sin;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

/**
 *
 * @author doria
 */
public class blockRooms {
    
    public static void reloadRooms(boolean build) {
        ArrayList<ArrayList<Integer>> centers;
        int numberItems;
        if(build) {
            centers = getCenters(gameConfig.numberTeams);
            numberItems = gameConfig.blocksConfig.size();
            gameConfig.previousNumberTeams = gameConfig.numberTeams;
            dataBase.setPreviousTeamsNumber(gameConfig.previousNumberTeams);
            gameConfig.previousNumberItems = numberItems;
            dataBase.setPreviousNumberItems((gameConfig.previousNumberItems));
        }
        else {
            centers = getCenters(gameConfig.previousNumberTeams);
            numberItems = gameConfig.previousNumberItems;
        }
        Location location;
        int k=0;
        for(ArrayList<Integer> center : centers) {
            location = new Location(Bukkit.getWorld("world"), center.get(0), 250, center.get(1));
            Material floor, glass;
            if(build) {
                switch(k) {
                    case 0:
                        floor = Material.BLUE_CONCRETE;
                        glass = Material.BLUE_STAINED_GLASS;
                        break;
                    case 1:
                        floor = Material.RED_CONCRETE;
                        glass = Material.RED_STAINED_GLASS;
                        break;
                    case 2:
                        floor = Material.GREEN_CONCRETE;
                        glass = Material.GREEN_STAINED_GLASS;
                        break;
                    case 3:
                        floor = Material.YELLOW_CONCRETE;
                        glass = Material.YELLOW_STAINED_GLASS;
                        break;
                    case 4:
                        floor = Material.ORANGE_CONCRETE;
                        glass = Material.ORANGE_STAINED_GLASS;
                        break;
                    case 5:
                        floor = Material.PINK_CONCRETE;
                        glass = Material.PINK_STAINED_GLASS;
                        break;
                    case 6:
                        floor = Material.BLACK_CONCRETE;
                        glass = Material.BLACK_STAINED_GLASS;
                        break;
                    case 7:
                        floor = Material.GRAY_CONCRETE;
                        glass = Material.GRAY_STAINED_GLASS;
                        break;
                    case 8:
                        floor = Material.CYAN_CONCRETE;
                        glass = Material.CYAN_STAINED_GLASS;
                        break;
                    default:
                        log.error("Plateforms construction : Invalid team number");
                        return;
                }
            }
            else {
                floor = Material.AIR;
                glass = Material.AIR;
            }
            //Creating floor
            int size = (int) (3 + 2*ceil((float) numberItems/4.));
            createFloor(location, size, glass);
            k++;
        }
    }
    
    private static void createFloor(Location center, int size, Material material) {
        World world = Bukkit.getWorld("world");
        int x, z;
        for(x = (int) (center.getX() - floor(size/2)); x <= (int) (center.getX() + floor(size/2)); x++) {
            for(z = (int) (center.getZ() - floor(size/2)); z <= (int) (center.getZ() + floor(size/2)); z++) {
                world.getBlockAt(x, 250, z).setType(material);
            }
        }
    }
    
    private static ArrayList<ArrayList<Integer>> getCenters(int nbTeams) {
        ArrayList<ArrayList<Integer>> result;
        result = new ArrayList<>();
        
        ArrayList<Integer> center;
        double angle;
        for(int k=0; k<nbTeams; k++) {
            center = new ArrayList<>();
            angle = (2*PI/nbTeams)*k;
            center.add((int) ceil(cos(angle)*80));
            center.add((int) ceil(sin(angle)*80));
            result.add(center);
        }
        return result;
    }
}