package com.dorianmercier.cubeassemble.common;

import com.dorianmercier.cubeassemble.inventories.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public final class main extends JavaPlugin {
    
    //Creating teams
    public static ScoreboardManager manager;
    public static Scoreboard board;

    public static Team blue;
    public static Team red;
    public static Team green;
    public static Team yellow;
    public static Team orange;
    public static Team pink;
    public static Team black;
    public static Team gray;
    public static Team cyan;
    public static Team host;

    @Override
    public void onEnable() {
        manager = Bukkit.getScoreboardManager();
        board = manager.getNewScoreboard();
        host = board.registerNewTeam("host");
        log.info("onEnable has been invoked!");
        this.getCommand("init").setExecutor(new cubeAssembleCommandExecutor(this));
        this.getCommand("clean").setExecutor(new cubeAssembleCommandExecutor(this));
        this.getCommand("setup").setExecutor(new cubeAssembleCommandExecutor(this));
        this.getCommand("test").setExecutor(new cubeAssembleCommandExecutor(this));
        this.getCommand("teammod").setExecutor(new cubeAssembleCommandExecutor(this));
        
        //Initializing inventories
        new setup();
        new setup_teams();
        new teams();
        new blocksInventory();
        new blockMenue();
        
        //Initalizing eventsHandlers
        new events(this);
        
        //Initializing teams
        teamManager.resetTeams();
    }

    @Override
    public void onDisable() {
        log.info("onDisable has been invoked!");
        //Deleting scoreboards created
       
    }
}