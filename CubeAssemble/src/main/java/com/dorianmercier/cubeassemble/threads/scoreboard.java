/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dorianmercier.cubeassemble.threads;

import com.dorianmercier.cubeassemble.common.cubeAssembleCommandExecutor;
import com.dorianmercier.cubeassemble.common.gameConfig;
import com.dorianmercier.cubeassemble.common.main;
import java.util.Collection;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

/**
 *
 * @author doria
 */
public class scoreboard {
    
    private static int temps;
    
    public static BukkitRunnable runnable;
    
    public static void start(main plugin, int time) {
        
        Collection<String> teamList = gameConfig.playerLinkedTeam.values();
        temps = time; //Temps restant en secondes
        try {
            main.board.getObjective("scoreboard").unregister();
        }
        catch(IllegalArgumentException | IllegalStateException | NullPointerException e) {
            //The objectif does not exist. Nothing to do.
        }
        Objective score = main.board.getObjective("scores");


        //Background task running every second
        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                Objective scoreboard = main.board.getObjective("scoreboard");
                if(scoreboard != null) scoreboard.unregister();
                main.config.getScore("time").setScore(temps);
                scoreboard = main.board.registerNewObjective("scoreboard", "dummy", "Scores");
                for(String teamName : teamList) {
                    String completeName = main.board.getTeam(teamName).getColor() + teamName;
                    scoreboard.getScore(completeName).setScore(score.getScore(completeName).getScore());
                }
                scoreboard.getScore("----------").setScore(-1);
                scoreboard.getScore(ChatColor.DARK_RED +  "Temps :  ").setScore(-2);
                scoreboard.getScore(ChatColor.DARK_GREEN +  getTimeString(temps)).setScore(-3);
                scoreboard.setDisplaySlot(DisplaySlot.SIDEBAR);


                if(gameConfig.gamePhase >= 5) this.cancel();
                if(temps <= 0) {
                    cubeAssembleCommandExecutor.finish();
                    this.cancel();
                }
                temps--;
            }
        };
        runnable.runTaskTimer(plugin, 0L /*<-- the initial delay */, 20L /*<-- the interval */);
    }

    public static void start(main plugin) {
        start(plugin, 120*60);
    }
    
    private static String getTimeString(int time) {
        int hours = time / 3600;
        time = time % 3600;
        int minutes = time / 60;
        time = time % 60;
        if(hours == 0) {
            if(minutes == 0) return time + " s";
            else return minutes + "m " + time + "s";
        }
        else return hours + "h " + minutes + "m " + time + "s";
    }
}
