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
import static java.lang.Math.abs;
import static java.lang.Math.ceil;
import static java.lang.Math.cos;
import static java.lang.Math.floor;
import static java.lang.Math.sin;
import java.util.ArrayList;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.material.MaterialData;

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
            Material floor, glass, pillar;
            if(build) {
                pillar = Material.QUARTZ_PILLAR;
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
                pillar = Material.AIR;
            }
            //Creating floor
            int size = (int) (3 + 2*ceil((float) numberItems/4.));
            createFloor(location, size, glass);
            k++;
            
            //Placing blocks
            
            placePillars(location, size, pillar);
            if(build) {
                ArrayList<Map.Entry<Material, Integer>> listBlocks = new ArrayList<>(gameConfig.blocksConfig.entrySet());
                
                placeBlocks(location, size, listBlocks, listBlocks.size());
            }
        }
        if(!build) {
            World world = Bukkit.getWorld("world");
            for(Entity e : world.getEntities()) {
                Location elocation = e.getLocation();
                double x = elocation.getX();
                double Y = elocation.getY();
                double z = elocation.getZ();
                if(e instanceof Item && abs(x) <= 100 && abs(z) <= 100 && z < 240) e.remove();
            }
        }
    }
    
    private static void placeBlocks(Location center, int size, ArrayList<Map.Entry<Material, Integer>> listBlocks, int nbBlocks) {
        World world = Bukkit.getWorld("world");
        int x, z, z_init, x_init, k = 0;
        x = (int) (center.getX() + floor(size)/2 - 1);
        z_init = (int) (center.getZ() + floor(size)/2 -2);
        if(z_init<0) z_init--;
        if(x<0) x--;
        Material material;
        
        for(z = z_init; z> z_init - size + 4; z-=2) {
            if(k >= nbBlocks) return;
            material = listBlocks.get(k).getKey();
            world.getBlockAt(x, 250, z).setType(material, false);
            
           generateSign(x, z, BlockFace.WEST, material.name(), listBlocks.get(k++).getValue());
        }
        z = (int) (center.getZ() - floor(size)/2 +1);
        x_init = (int) (center.getX() + floor(size)/2 -2);
        if(z>0) z++;
        if(x_init<0) x_init--;
        for(x = x_init; x> x_init - size + 4; x-=2) {
            if(k >= nbBlocks) return;
            material = listBlocks.get(k).getKey();
            world.getBlockAt(x, 250, z).setType(material, false);
            generateSign(x, z, BlockFace.SOUTH, material.name(), listBlocks.get(k++).getValue());
        }
        
        x = (int) (center.getX() - floor(size)/2 + 2);
        z_init = (int) (center.getZ() - floor(size)/2 +3);
        if(z_init<0) z_init--;
        if(x<0) x--;
        for(z = z_init; z< z_init + size - 4; z+=2) {
            if(k >= nbBlocks) return;
            material = listBlocks.get(k).getKey();
            world.getBlockAt(x, 250, z).setType(material, false);
            generateSign(x, z, BlockFace.EAST, material.name(), listBlocks.get(k++).getValue());
        }
        z = (int) (center.getZ() + floor(size)/2 -2);
        x_init = (int) (center.getX() - floor(size)/2 +3);
        if(z>0) z++;
        if(x_init<0) x_init--;
        for(x = x_init; x< x_init + size - 4; x+=2) {
            if(k >= nbBlocks) return;
            material = listBlocks.get(k).getKey();
            world.getBlockAt(x, 250, z).setType(material, false);
            generateSign(x, z, BlockFace.NORTH, material.name(), listBlocks.get(k++).getValue());
        }
    }
    
    private static void generateSign(int x, int z, BlockFace face, String blockType, int points) {
        Sign sign;
        Block block;
        block = Bukkit.getWorld("world").getBlockAt(x, 251, z);
        block.setType(Material.OAK_WALL_SIGN,false);

        sign = (Sign) block.getState();

        sign.setLine(0, "---------------");
        sign.setLine(1, blockType.toLowerCase());
        sign.setLine(2, points + " points");
        sign.setLine(3, "---------------");

        org.bukkit.block.data.type.WallSign signData = (org.bukkit.block.data.type.WallSign) sign.getBlockData();
        signData.setFacing(face);
        sign.setBlockData(signData);
        block.setBlockData(sign.getBlockData());
        sign.update();
    }
    
    private static void placePillars(Location center, int size, Material material) {
        World world = Bukkit.getWorld("world");
        int x, z, z_init, x_init;
        x = (int) (center.getX() + floor(size)/2);
        z_init = (int) (center.getZ() + floor(size)/2 -2);
        if(z_init<0) z_init--;
        if(x<0) x--;
        for(z = z_init; z> z_init - size + 4; z-=2) {
            world.getBlockAt(x, 251, z).setType(material);
            world.getBlockAt(x - size + 1, 251, z).setType(material);
        }
        z = (int) (center.getZ() - floor(size)/2);
        x_init = (int) (center.getX() + floor(size)/2 -2);
        if(z>0) z++;
        if(x_init<0) x_init--;
        for(x = x_init; x> x_init - size + 4; x-=2) {
            world.getBlockAt(x, 251, z).setType(material);
            world.getBlockAt(x, 251, z + size - 1).setType(material);
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
