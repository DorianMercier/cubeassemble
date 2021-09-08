package com.dorianmercier.cubeassemble.common;

import com.dorianmercier.cubeassemble.inventories.*;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public final class main extends JavaPlugin {

    @Override
    public void onEnable() {
        log.info("onEnable has been invoked!");
        this.getCommand("init").setExecutor(new cubeAssembleCommandExecutor(this));
        this.getCommand("clean").setExecutor(new cubeAssembleCommandExecutor(this));
        this.getCommand("setup").setExecutor(new cubeAssembleCommandExecutor(this));
        
        //Initializing inventories
        new setup();
        new setup_teams();
        new teams();
        
        //Initalizing eventsHandlers
        new events(this);
        
       
    }

    @Override
    public void onDisable() {
        log.info("onDisable has been invoked!");
        //Deleting scoreboards created
       
    }
}