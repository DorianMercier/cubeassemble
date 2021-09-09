/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dorianmercier.cubeassemble.common;

import static com.dorianmercier.cubeassemble.common.main.board;
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

        public static void addPlayer(Team team, Player player) {
            setPlayerColor(player, team.getColor());
            team.addEntry(player.getName());
        }

        public static void setPlayerColor(Player player, ChatColor color) {
           String playerName = player.getName();
           player.setDisplayName(color + playerName + ChatColor.RESET);
           player.setPlayerListName(color + playerName);
           log.info("On affiche le joueur " + player.getName() + " de la couleur " + color);
    }
}
