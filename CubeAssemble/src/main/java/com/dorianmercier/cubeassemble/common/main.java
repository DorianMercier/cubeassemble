package com.dorianmercier.cubeassemble.common;

import com.dorianmercier.cubeassemble.inventories.setup;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class main extends JavaPlugin {

    @Override
    public void onEnable() {
        log.info("onEnable has been invoked!");
        this.getCommand("init").setExecutor(new cubeAssembleCommandExecutor(this));
        this.getCommand("clean").setExecutor(new cubeAssembleCommandExecutor(this));
        this.getCommand("setup").setExecutor(new cubeAssembleCommandExecutor(this));
        
        //Initializing inventories
        new setup(this);
    }

    @Override
    public void onDisable() {
            log.info("onDisable has been invoked!");
    }
}
