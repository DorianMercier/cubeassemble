package com.dorianmercier.cubeassemble.common;

import org.bukkit.Bukkit;
import static org.bukkit.Bukkit.getLogger;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class cubeAssembleCommandExecutor implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("init")) {
                    getLogger().info("Initialization of the game");
                    getLogger().info("Length of args : " + args.length);
			if(args.length != 1) return false;
                        
                        World world = Bukkit.getWorld("world");
                        
                        //Building the spawn
                        getLogger().info("Building the spawn");
                        //Building the floor
                        for(int x = -11; x <= 11; x++) {
                            for(int z = -11; z <= 11; z++) {
                                world.getBlockAt(x, 250, z).setType(Material.WHITE_STAINED_GLASS);
                            }
                        }
			getLogger().info("Game initialized");
			return true;
		}
		return false;
	}
	
	

}
