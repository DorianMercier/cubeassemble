/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dorianmercier.cubeassemble.threads;

import com.dorianmercier.cubeassemble.common.cubeAssembleCommandExecutor;
import com.dorianmercier.cubeassemble.common.gameConfig;
import com.dorianmercier.cubeassemble.common.log;
import com.dorianmercier.cubeassemble.common.main;
import java.util.Collection;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

/**
 *
 * @author doria
 */
public class scoreboard {
    //Thread status : 0 = not running, 1 = running
    public static int status = 0;
    
    public static void start(main plugin, int time) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
                @Override
                public void run() {
                    try {
                        Collection<String> teamList = gameConfig.playerLinkedTeam.values();
                        int temps = time; //Temps restant en secondes
                        try {
                            main.board.getObjective("scoreboard").unregister();
                        }
                        catch(IllegalArgumentException | IllegalStateException | NullPointerException e) {
                            //The objectif does not exist. Nothing to do.
                        }
                        Objective score = main.board.getObjective("scores");
                        Objective scoreboard;
                        
                        String completeName;
                        while(true) {
                            main.config.getScore("time").setScore(temps);
                            scoreboard = main.board.registerNewObjective("scoreboard", "dummy", "Scores");
                            for(String teamName : teamList) {
                                completeName = main.board.getTeam(teamName).getColor() + teamName;
                                scoreboard.getScore(completeName).setScore(score.getScore(completeName).getScore());
                            }
                            scoreboard.getScore("----------").setScore(-1);
                            scoreboard.getScore(ChatColor.DARK_RED +  "Temps :  ").setScore(-2);
                            scoreboard.getScore(ChatColor.DARK_GREEN +  getTimeString(temps)).setScore(-3);
                            scoreboard.setDisplaySlot(DisplaySlot.SIDEBAR);
                            
                            
                            Thread.sleep(1000);
                            if(gameConfig.gamePhase >= 5) break;
                            if(temps <= 0) {
                                Bukkit.getScheduler().runTask(plugin, new Runnable() {
                                    @Override
                                    public void run() {
                                        cubeAssembleCommandExecutor.finish();
                                    }
                                });
                                break;
                            }
                            temps--;
                            scoreboard.unregister();
                        }
                    }
                    catch(InterruptedException e) {
                        log.error("Thread error in start command : " + e);
                    }
                }
            }
        );
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
