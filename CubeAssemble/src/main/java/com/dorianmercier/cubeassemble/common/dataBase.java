/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dorianmercier.cubeassemble.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


/**
 *
 * @author doria
 */
public class dataBase {
    
    private static Connection con() {
        try {
            Class.forName("org.postgresql.Driver");
            log.info("Driver postgresql jdbc chargé avec succès");
            return DriverManager.getConnection("jdbc:postgresql://localhost:5432/cubeassemble", "cubeassemble", "cubeassemble");
        }
        catch(ClassNotFoundException | SQLException e) {
            log.error("Cannot connect to the database : " + e.getLocalizedMessage());
            return null;
        }
    }
    
    public static void disconnect() {
        try {
            con.close();
        } catch (SQLException ex) {
            log.error("Database : Cannot close the connexion to the database");
        }
    }
    
    private static Connection con = con();
    
    public static void addPlayer(Player player) {
        try {
            PreparedStatement st = con.prepareStatement("insert into players values(?, ?, null, false)");
            st.setString(1, player.getUniqueId().toString());
            st.setString(2, player.getName());
            st.execute();
        }
        catch(SQLException e) {
            log.error("Cannot add a player to the database");
        }
    }
    
    public static boolean isPlayer(Player player) {
        try {
            PreparedStatement st = con.prepareStatement("select uuid from players where uuid like ?");
            st.setString(1, player.getUniqueId().toString());
            ResultSet rs = st.executeQuery();
            rs.next();
            if(rs.getString(1) == null) {
                log.info("Database : Player " + player.getName() + " already in database");
                return true;
            }
            else return false;
        }
        catch(SQLException e) {
            return false;
        }
    }
    
    public static void setTeam(Player player, String team) {
        try {
            //Retrieving team id
            PreparedStatement st = con.prepareStatement("select id from teams where name like ?");
            st.setString(1, team);
            ResultSet rs = st.executeQuery();
            rs.next();
            int idTeam = rs.getInt(1);
            //Registering team for the player
            st = con.prepareStatement("update players set team = ? where uuid like ?");
            st.setInt(1, idTeam);
            st.setString(2, player.getUniqueId().toString());
            st.execute();
        }
        catch(SQLException e) {
            log.error("Database : Cannot set the team " + team + " for the player " + player.getName());
        }
    }
    
    public static void resetTeams() {
        try {
            PreparedStatement st = con.prepareStatement("update players set team = null");
            st.execute();
            st = con.prepareStatement("update teams set score=0");
            st.execute();
        }
        catch(SQLException e) {
            log.error("Database : Cannot reset teams");
        }
    }
    
    public static void addTeam(String name) {
        try {
            PreparedStatement st = con.prepareStatement("insert into teams values(default, ?, 0)");
            st.setString(1, name);
            st.execute();
        }
        catch(SQLException e) {
            log.error("Database : Cannot create the team " + name);
        }
    }
    
    public static void addhost(Player player) {
        try {
            PreparedStatement st = con.prepareStatement("update players set ishost=true where uuid like ?");
            st.setString(1, player.getUniqueId().toString());
            st.execute();
        }
        catch(SQLException e) {
            log.error("Database : Cannot set player " + player.getName() + " as host");
        }
    }
    
    public static void addBlock(Material material, int points) {
        try {
            PreparedStatement st = con.prepareStatement("insert into blocks values(default, ?, ?)");
            st.setString(1, material.toString());
            st.setInt(2, points);
            st.execute();
        }
        catch(SQLException e) {
            log.error("Database : Cannot add the block " + material.toString() + " to the database");
        }
    }
    
    public static void setPoints(Material material, int points) {
        try {
            PreparedStatement st = con.prepareStatement("update blocks set points = ? where material like ?");
            st.setString(2, material.toString());
            st.setInt(1, points);
            st.execute();
        }
        catch(SQLException e) {
            log.error("Database : Cannot modify the points for " + material.toString());
        }
    }
    
    public static void voidBlocks() {
        try {
            PreparedStatement st = con.prepareStatement("delete from blocks");
            st.execute();
        }
        catch(SQLException e) {
            log.error("Database : Cannot void blocks");
        }
    }
    
    public static ArrayList<String> getHosts() {
        ArrayList<String> result = new ArrayList<>();
        
        try {
            PreparedStatement st = con.prepareStatement("select name from players where ishost=true");
            ResultSet rs = st.executeQuery();
            while(rs.next()) {
                result.add(rs.getString(1));
            }
        }
        catch(SQLException e) {
            log.error("Database : Cannot load hosts");
        }
        return result;
    }
    
    public static HashMap<String, String> getListTeams() {
        HashMap<String, String> result = new HashMap<>();
        try {
            PreparedStatement st = con.prepareStatement("select players.name, teams.name from players join teams on teams.id = players.team");
            ResultSet rs = st.executeQuery();
            while(rs.next()) {
                log.info("On a chargé le joueur " + rs.getString(1) + " pour la team " + rs.getString(2));
                result.put(rs.getString(1), rs.getString(2));
            }
        }
        catch(SQLException e) {
            log.error("Database : Cannot load players and teams");
        }
        return result;
    }
    
    public static LinkedHashMap<Material, Integer> getblocksConfig() {
        LinkedHashMap<Material, Integer> result = new LinkedHashMap<>();
        
        try {
            PreparedStatement st = con.prepareStatement("select material, points from blocks order by id asc");
            ResultSet rs = st.executeQuery();
            Material material;
            int points;
            while(rs.next()) {
                material = Material.getMaterial(rs.getString(1));
                points = rs.getInt(2);
                result.put(material, points);
            }
        }
        catch(SQLException e) {
            log.error("Database : Cannot load blocks");
        }
        return result;
    }
    
    public static void setTeamsFreezed(boolean value) {
        try {
            PreparedStatement st = con.prepareStatement("update general_config set value=? where key like 'teamsFreezed'");
            int intValue;
            if(value) intValue = 1;
            else intValue = 0;
            st.setInt(1, intValue);
            st.execute();
        }
        catch(SQLException e) {
            log.error("Database : Cannot set teamsFreezed");
        }
    }
    
    public static void setTeamsNumber(int value) {
        try {
            PreparedStatement st = con.prepareStatement("update general_config set value=? where key like 'numberTeams'");
            st.setInt(1, value);
            st.execute();
        }
        catch(SQLException e) {
            log.error("Database : Cannot set teamsNumber");
        }
    }
     
    public static boolean getTeamsFreezed() {
        try {
            PreparedStatement st = con.prepareStatement("select value from general_config where key like 'teamsFreezed'");
            ResultSet rs = st.executeQuery();
            rs.next();
            return rs.getInt(1)==1;
        }
        catch(SQLException e) {
            log.error("Database : Cannot load teamsFreezed");
            return false;
        }
    }
    
    public static int getTeamsNumber() {
        try {
            PreparedStatement st = con.prepareStatement("select value from general_config where key like 'numberTeams'");
            ResultSet rs = st.executeQuery();
            rs.next();
            return rs.getInt(1);
        }
        catch(SQLException e) {
            log.error("Database : Cannot load numberTeams");
            return 2;
        }
    }
    
    public static int getPreviousTeamsNumber() {
        try {
            PreparedStatement st = con.prepareStatement("select value from general_config where key like 'previousNumberTeams'");
            ResultSet rs = st.executeQuery();
            rs.next();
            return rs.getInt(1);
        }
        catch(SQLException e) {
            log.error("Database : Cannot load previousNumberTeams");
            return 2;
        }
    }
    public static void setPreviousTeamsNumber(int value) {
        try {
            PreparedStatement st = con.prepareStatement("update general_config set value=? where key like 'previousNumberTeams'");
            st.setInt(1, value);
            st.execute();
        }
        catch(SQLException e) {
            log.error("Database : Cannot set previousNumberTeams");
        }
    }
    
    public static int getPreviousNumberItems() {
        try {
            PreparedStatement st = con.prepareStatement("select value from general_config where key like 'previousNumberItems'");
            ResultSet rs = st.executeQuery();
            rs.next();
            return rs.getInt(1);
        }
        catch(SQLException e) {
            log.error("Database : Cannot load previousNumberTeams");
            return 2;
        }
    }
    public static void setPreviousNumberItems(int value) {
        try {
            PreparedStatement st = con.prepareStatement("update general_config set value=? where key like 'previousNumberItems'");
            st.setInt(1, value);
            st.execute();
        }
        catch(SQLException e) {
            log.error("Database : Cannot set previousNumberItems");
        }
    }
    
    public static void saveStartInventory(Inventory inv) {
        ItemStack[] content = inv.getContents();
        int k=0;
        try {
            PreparedStatement st = con.prepareStatement("delete from start_inventory");
            st.execute();
        }
        catch(SQLException e) {
            log.error("Database : Cannot void table start_inventory");
            return;
        }
        
        try {
            for(ItemStack itemStack : content) {
                PreparedStatement st = con.prepareStatement("insert into start_inventory values(?, ?, ?)");
                st.setInt(1, k++);
                if(itemStack == null) {
                    st.setString(2, "null");
                    st.setInt(3, 0);
                }
                else {
                    st.setString(2, itemStack.getType().toString());
                    st.setInt(3, itemStack.getAmount());
                }
                st.execute();
            }
        }
        catch(SQLException e) {
            log.error("Database : Cannot save start inventory : " + e);
        }
    }
    
    public static Inventory loadInventory() {
        Inventory inv = Bukkit.createInventory(null, InventoryType.PLAYER);
        
        ItemStack[] contents = new ItemStack[41];
        ItemStack currItem;
        int k=0;
        
        try {
            PreparedStatement st = con.prepareStatement("select * from start_inventory order by key asc");
            ResultSet rs = st.executeQuery();
            while(rs.next()) {
                if(rs.getString(2).equals("null")) contents[k++] = null;
                else {
                    currItem = new ItemStack(Material.getMaterial(rs.getString(2)), rs.getInt(3));
                    contents[k++] = currItem;
                }
            }
        }
        catch(SQLException e) {
            log.error("Database : Cannot load start inventory : " + e);
        }
        
        if(k!=0) {
            inv.setContents(contents);
        }
        
        return inv;
    }
}
