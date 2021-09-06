package com.dorianmercier.cubeassemble.common;

import com.dorianmercier.cubeassemble.structures.spawn;
import org.bukkit.Bukkit;
import static org.bukkit.Bukkit.getLogger;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class cubeAssembleCommandExecutor implements CommandExecutor {
    
    private final main plugin;

    public cubeAssembleCommandExecutor(main plugin) {
            this.plugin = plugin; // Store the plugin in situations where you need it.
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        World world = Bukkit.getWorld("world");
        
        if (command.getName().equalsIgnoreCase("init")) {
            log.info("Initialization of the game");
            if(args.length != 0) return false;
            
            world = Bukkit.getWorld("world");
            if(world.setSpawnLocation(0,251,0)) {
                log.info("World spawn changed successfully to 0 0");
            }
            else {
                log.warning("Failed to set world spawn");
            }
            //Building the spawn
            return spawn.build(true);
        }
        
        if (command.getName().equalsIgnoreCase("clean")) {
            log.info("Cleaning the map");
            if(args.length != 0) return false;
            //Destroying the spawn
            return spawn.build(false);
        }
        return false;
    }
}
