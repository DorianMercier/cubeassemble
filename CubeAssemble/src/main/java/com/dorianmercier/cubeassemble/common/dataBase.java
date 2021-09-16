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
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;


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
            PreparedStatement st = con.prepareStatement("insert into blocks values(?, ?)");
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
    
    public static HashMap<String, ArrayList<String>> getListTeams() {
        HashMap<String, ArrayList<String>> result = new HashMap<>();
        try {
            PreparedStatement st = con.prepareStatement("select players.name, teams.name from players joint teams on teams.id = players.team");
            ResultSet rs = st.executeQuery();
            String player, team;
            while(rs.next()) {
                player = rs.getString(1);
                team = rs.getString(2);
                //!!!!!In progress!!!!!!!!//
                result.
                result.add(rs.getString(1));
            }
        }
        catch(SQLException e) {
            log.error("Database : Cannot load hosts");
        }
        return result;
    }
}
