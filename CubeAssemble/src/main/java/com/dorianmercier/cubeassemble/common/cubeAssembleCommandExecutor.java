package com.dorianmercier.cubeassemble.common;

import com.dorianmercier.cubeassemble.inventories.setup;
import com.dorianmercier.cubeassemble.structures.spawn;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

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
            
            gameConfig.hostList.add(sender.getName());
            dataBase.addhost((Player) sender);
            
            //Building the spawn
            return spawn.build(true);
        }
        
        if (command.getName().equalsIgnoreCase("clean")) {
            log.info("Cleaning the map");
            if(args.length != 0) return false;
            //Destroying the spawn
            return spawn.build(false);
        }
        
        if(command.getName().equalsIgnoreCase("setup")) {
            
            if(args.length != 0) return false;
            log.info(sender.getName() + " requested setup mod");
            Player player = (Player) sender;
            setup.openInventory(player);
            
            return true;
        }
        if(command.getName().equalsIgnoreCase("teammod")) {
            if(args.length < 2) return false;
            Team team;
                team = main.board.getTeam(args[1]);
            if(team == null) {
                sender.sendMessage("La team \"" + args[1] + "\" n'existe pas.");
                return false;
            }
            switch(args[0]) {
                case "list":
                    sender.sendMessage("Liste des joueurs de la team \"" + args[1] + "\" :");
                    sender.sendMessage(team.getEntries().toString());
                    break;
                case "join":
                    if(args.length < 3) return false;
                    try {
                        teamManager.addPlayer(team, Bukkit.getPlayer(args[2]));
                        sender.sendMessage("Vous avez placé le joueur " + args[2] + " dans la team " + args[1]);
                    }
                    catch(Exception e) {
                        sender.sendMessage("Le joueur " + args[2] + " n'existe pas");
                        return false;
                    }
                    
                    break;
            }
            return true;
        }
        if(command.getName().equalsIgnoreCase("test")) {
            
            sender.sendMessage("Nom de la team rouge : " + main.red.getName());
            main.red.addEntry(sender.getName());
            sender.sendMessage("Liste de la team rouge : " + main.red.getEntries().toString());
            
            return true;
        }
        return false;
    }
}
