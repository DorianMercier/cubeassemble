/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dorianmercier.cubeassemble.common;

import static com.dorianmercier.cubeassemble.common.main.board;
import java.util.ArrayList;
import java.util.Collection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

/**
 *
 * @author doria
 */
public class teamManager {
    
    public teamManager() {
    }
    
    public static void initTeams() {
        gameConfig.listTeams.put("Bleu", new ArrayList<>());
        gameConfig.listTeams.put("Rouge", new ArrayList<>());
        gameConfig.listTeams.put("Vert", new ArrayList<>());
        gameConfig.listTeams.put("Jaune", new ArrayList<>());
        gameConfig.listTeams.put("Orange", new ArrayList<>());
        gameConfig.listTeams.put("Rose", new ArrayList<>());
        gameConfig.listTeams.put("Noir", new ArrayList<>());
        gameConfig.listTeams.put("Gris", new ArrayList<>());
        gameConfig.listTeams.put("Cyan", new ArrayList<>());

        main.blue = board.registerNewTeam("Bleu");
        main.red = board.registerNewTeam("Rouge");
        main.green = board.registerNewTeam("Vert");
        main.yellow = board.registerNewTeam("Jaune");
        main.orange = board.registerNewTeam("Orange");
        main.pink = board.registerNewTeam("Rose");
        main.black = board.registerNewTeam("Noir");
        main.gray = board.registerNewTeam("Gris");
        main.cyan = board.registerNewTeam("Cyan");

        main.blue.setColor(ChatColor.DARK_BLUE);
        main.red.setColor(ChatColor.RED);
        main.green.setColor(ChatColor.GREEN);
        main.yellow.setColor(ChatColor.YELLOW);
        main.orange.setColor(ChatColor.GOLD);
        main.pink.setColor(ChatColor.LIGHT_PURPLE);
        main.black.setColor(ChatColor.BLACK);
        main.gray.setColor(ChatColor.GRAY);
        main.cyan.setColor(ChatColor.BLUE);
    }
    
    public static void resetTeams() {

        try {
            main.red.unregister();
            main.blue.unregister();
            main.green.unregister();
            main.yellow.unregister();
            main.orange.unregister();
            main.pink.unregister();
            main.black.unregister();
            main.gray.unregister();
            main.cyan.unregister();
        }
        catch(NullPointerException e) {
            //Nothing to do. Teams are just not initialized. Not a problem.
        }

        dataBase.resetTeams();
        gameConfig.listTeams.clear();
        
        Collection<? extends Player> colPlayers = Bukkit.getServer().getOnlinePlayers();
        
        for(Player player : colPlayers) {
            setPlayerColor(player, ChatColor.RESET);
        }
        
        initTeams();
    }

        public static void addPlayer(Team team, Player player) {
            log.info("The player " + player.getName() + " joined the team " + team.getName());
            setPlayerColor(player, team.getColor());
            team.addEntry(player.getName());
            for(ArrayList<String> list : gameConfig.listTeams.values()) {
                //Removing player for all team lists
                list.remove(player.getName());
            }
            gameConfig.listTeams.get(team.getName()).add(player.getName());
            dataBase.setTeam(player, team.getName());
        }
        
        public static void updatePlayer(Player player) {
            String teamName = gameConfig.playerLinkedTeam.get(player.getName());
            if(teamName != null) {
                Team team = main.board.getTeam(teamName);
                setPlayerColor(player, team.getColor());
                team.addEntry(player.getName());
            }
            else setPlayerColor(player, ChatColor.RESET);
        }
        
        public static void setPlayerColor(Player player, ChatColor color) {
           String playerName = player.getName();
           player.setDisplayName(color + playerName + ChatColor.RESET);
           player.setPlayerListName(color + playerName);
    }
}
