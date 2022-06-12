package com.dorianmercier.cubeassemble.common;

import com.dorianmercier.cubeassemble.inventories.*;
import com.dorianmercier.cubeassemble.threads.scoreboard;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public final class main extends JavaPlugin {
    
    //Creating teams
    public static ScoreboardManager manager;
    public static Scoreboard board;
    public static Objective config;
    public static Objective lastX;
    public static Objective lastY;
    public static Objective lastZ;
    public static Objective lastWorld;

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
    
    private static cubeAssembleCommandExecutor cmdEx;
    private static events eventsHandler;
    
    //Inventories
    private static setup setupInv;
    private static setup_teams setup_teamsInv;
    private static teams teamsInv;
    private static blockMenue blockMenueInv;
    
    public void init() {
        log.info("Initiallising plugin...");
        manager = Bukkit.getScoreboardManager();
        board = manager.getMainScoreboard();
        try {
            host = board.getTeam("host");
        }
        catch(IllegalArgumentException e) {
            host = board.registerNewTeam("host");
        }
        
        cmdEx = new cubeAssembleCommandExecutor(this);
        
        try {
            this.getCommand("init").setExecutor(cmdEx);
            this.getCommand("clean").setExecutor(cmdEx);
            this.getCommand("setup").setExecutor(cmdEx);
            this.getCommand("test").setExecutor(cmdEx);
            this.getCommand("ready").setExecutor(cmdEx);
            this.getCommand("start").setExecutor(cmdEx);
            this.getCommand("finish").setExecutor(cmdEx);
            this.getCommand("reset").setExecutor(cmdEx);
            this.getCommand("inv").setExecutor(cmdEx);
            this.getCommand("save").setExecutor(cmdEx);
            this.getCommand("giveCompass").setExecutor(cmdEx);
            this.getCommand("hardreset").setExecutor(cmdEx);
        }
        catch(Exception e) {
            log.error("Error initialising commands");
        }
        
        //Initializing inventories
        setupInv = new setup();
        setup_teamsInv = new setup_teams();
        teamsInv = new teams();
        blockMenueInv = new blockMenue();
        
        //Initalizing eventsHandlers
        HandlerList.unregisterAll(this);
        eventsHandler = new events(this);
        
        //Initializing teams
        gameConfig.loadConfig();
        teamManager.initTeams();
        
        //Creating config scoreboard
        config = board.getObjective("config");
        if(config == null) {
            config = board.registerNewObjective("config", "dummy", "config");
            config.getScore("gamePhase").setScore(0);
        }
        lastX = board.getObjective("lastX");
        if(lastX == null) {
            lastX = board.registerNewObjective("lastX", "dummy", "lastX");
        }
        lastY = board.getObjective("lastY");
        if(lastY == null) {
            lastY = board.registerNewObjective("lastY", "dummy", "lastY");
        }
        lastZ = board.getObjective("lastZ");
        if(lastZ == null) {
            lastZ = board.registerNewObjective("lastZ", "dummy", "lastZ");
        }
        lastWorld = board.getObjective("lastWorld");
        if(lastWorld == null) {
            lastWorld = board.registerNewObjective("lastWorld", "dummy", "lastWorld");
        }
        gameConfig.gamePhase = config.getScore("gamePhase").getScore();
        
        //Starting scoreboard update
        if(gameConfig.gamePhase == 3 || gameConfig.gamePhase == 4) {
            scoreboard.start(this, board.getObjective("config").getScore("time").getScore());
        }
    
        log.info("...plugin initialised");
    }

    @Override
    public void onEnable() {
        init();
    }

    @Override
    public void onDisable() {
        log.info("onDisable has been invoked!");
       
        dataBase.disconnect();
    }
}