package com.dorianmercier.cubeassemble.common;

import com.dorianmercier.cubeassemble.inventories.setup;
import com.dorianmercier.cubeassemble.structures.blockRooms;
import com.dorianmercier.cubeassemble.structures.spawn;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Score;
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
            blockRooms.reloadRooms(false);
            return spawn.build(false);
        }
        
        if(command.getName().equalsIgnoreCase("setup")) {
            
            if(args.length != 0) return false;
            log.info(sender.getName() + " requested setup mod");
            Player player = (Player) sender;
            setup.openInventory(player);
            
            return true;
        }
        if(command.getName().equalsIgnoreCase("test")) {
            
            sender.sendMessage("Nom de la team rouge : " + main.red.getName());
            main.red.addEntry(sender.getName());
            sender.sendMessage("Liste de la team rouge : " + main.red.getEntries().toString());
            
            return true;
        }
        
        if (command.getName().equalsIgnoreCase("ready")) {
            log.info("Reloading the rooms...");
            blockRooms.reloadRooms(false);
            blockRooms.reloadRooms(true);
            log.info("Rooms reloaded");
            return true;
        }
        
        if(command.getName().equalsIgnoreCase("start")) {
            //Initialization of the scoreboard
            log.info("The player " + sender.getName() + " started the game");
            main.score = main.board.registerNewObjective("scores", "dummy", "Scores");
            main.score.setDisplaySlot(DisplaySlot.SIDEBAR);
            ArrayList<String> listTeams = new ArrayList<>();
            ArrayList<Map.Entry<String, String>> playersTeams = new ArrayList<>(gameConfig.playerLinkedTeam.entrySet());
            String teamName;
            for(Entry<String, String> entry : playersTeams) {
                teamName = entry.getValue();
                if(!listTeams.contains(teamName)) listTeams.add(teamName);
            }
            
            Score score;
            for(String team : listTeams) {
                score = main.score.getScore(main.board.getTeam(team).getColor() + team);
                score.setScore(0);
            }
            
        }
        return false;
    }
}
