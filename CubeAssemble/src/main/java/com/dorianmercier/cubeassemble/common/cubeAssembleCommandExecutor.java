package com.dorianmercier.cubeassemble.common;

import static com.dorianmercier.cubeassemble.common.gameConfig.setGamePhase;
import com.dorianmercier.cubeassemble.inventories.setup;
import com.dorianmercier.cubeassemble.inventories.startInventory;
import com.dorianmercier.cubeassemble.structures.blockRooms;
import com.dorianmercier.cubeassemble.structures.spawn;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

public class cubeAssembleCommandExecutor implements CommandExecutor {
    
    private final main plugin;

    public cubeAssembleCommandExecutor(main plugin) {
            this.plugin = plugin; // Store the plugin in situations where you need it.
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        World world = Bukkit.getWorld("world");
        
        if(command.getName().equalsIgnoreCase("save")) {
            if(!gameConfig.onInvConfig.contains((Player) sender)) {
                sender.sendMessage(ChatColor.RED + "Action impossible. Utilisez /inv pour configurer l'inventaire de départ.");
                return true;
            }
            Player player = (Player) sender;
            startInventory.update(player.getInventory());
            dataBase.saveStartInventory(player.getInventory());
            player.getInventory().clear();
            player.getInventory().setItem(0, new ItemStack(Material.WHITE_BANNER, 1));
            player.setGameMode(GameMode.ADVENTURE);
            
            gameConfig.onInvConfig.remove(player);
            gameConfig.canClick.remove(player);
            return true;
        }
        
        //Cannot execute a command other than /save when configuring start inventory
        if(gameConfig.onInvConfig.contains((Player) sender)) {
            sender.sendMessage(ChatColor.RED + "Action impossible. Veuiller utiliser /save pour terminer la configuration de l'inventaire.");
            return true;
        }
        
        if (command.getName().equalsIgnoreCase("init")) {
            log.info("Initialization of the game");

            if(args.length != 0) return false;
            
            if(gameConfig.gamePhase >2) {
                sender.sendMessage(ChatColor.RED + "Action impossible. La partie a déjà commencé");
                return true;
            }
            if(gameConfig.gamePhase != 0) {
                sender.sendMessage(ChatColor.RED + "La partie a déjà été initialisée");
                return true;
            }
            
            if(world.setSpawnLocation(0,251,0)) {
                log.info("World spawn changed successfully to 0 0");
            }
            else {
                log.warning("Failed to set world spawn");
                return false;
            }
            
            gameConfig.hostList.add(sender.getName());
            dataBase.addhost((Player) sender);
            
            setGamePhase(1);
            
            //Building the spawn
            return spawn.build(true);
        }
        
        if (command.getName().equalsIgnoreCase("clean")) {
            if(gameConfig.gamePhase == 0) {
                sender.sendMessage(ChatColor.RED + "La partie n'a pas été initialisée.");
                return true;
            }
            if(gameConfig.gamePhase >2) {
                sender.sendMessage(ChatColor.RED + "Action impossible. La partie a déjà commencé");
                return true;
            }
            log.info("Cleaning the map");
            if(args.length != 0) return false;
            //Destroying the spawn
            blockRooms.reloadRooms(false);
            setGamePhase(0);
            return spawn.build(false);
        }
        
        if(command.getName().equalsIgnoreCase("setup")) {
            
            if(args.length != 0) return false;
            if(gameConfig.gamePhase == 0) {
                sender.sendMessage(ChatColor.RED + "La partie n'a pas été initialisée. Veuillez executer /init avant.");
                return true;
            }
             if(gameConfig.gamePhase >2) {
                sender.sendMessage(ChatColor.RED + "Action impossible. La partie a déjà commencé");
                return true;
            }
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
            if(gameConfig.gamePhase < 1) {
                sender.sendMessage(ChatColor.RED + "Action impossible, la partie n'a pas été initialisée.");
                return true;
            }
             if(gameConfig.gamePhase >2) {
                sender.sendMessage(ChatColor.RED + "Action impossible. La partie a déjà commencé");
                return true;
            }
            log.info("Reloading the rooms...");
            blockRooms.reloadRooms(false);
            blockRooms.reloadRooms(true);
            log.info("Rooms reloaded");
            setGamePhase(2);
            return true;
        }
        
        if(command.getName().equalsIgnoreCase("start")) {
            if(gameConfig.gamePhase < 2) {
                sender.sendMessage(ChatColor.RED + "Action impossible. La partie n'est pas prête. Veuillez utiliser /ready");
                return true;
            }
            if(gameConfig.gamePhase >2) {
                sender.sendMessage(ChatColor.RED + "Action impossible. La partie a déjà commencé");
                return true;
            }
            //Initialization of the scoreboard
            log.info("The player " + sender.getName() + " started the game");
            Objective score = main.board.registerNewObjective("scores", "dummy", "Scores");
            score.setDisplaySlot(DisplaySlot.SIDEBAR);
            ArrayList<String> listTeams = new ArrayList<>();
            ArrayList<Map.Entry<String, String>> playersTeams = new ArrayList<>(gameConfig.playerLinkedTeam.entrySet());
            String teamName;
            for(Entry<String, String> entry : playersTeams) {
                teamName = entry.getValue();
                if(!listTeams.contains(teamName)) listTeams.add(teamName);
            }
            
            Score Sscore;
            for(String team : listTeams) {
                Sscore = score.getScore(main.board.getTeam(team).getColor() + team);
                Sscore.setScore(0);
            }
            setGamePhase(3);
        }
        if(command.getName().equalsIgnoreCase("finish")) {
            if(gameConfig.gamePhase < 3) {
                sender.sendMessage(ChatColor.RED + "Action impossible. La partie n'a pas encore commencé.");
                return true;
            }
            if(gameConfig.gamePhase > 3) {
                sender.sendMessage(ChatColor.RED + "Action impossible. La partie est déjà terminée.");
                return true;
            }
            Bukkit.broadcastMessage("La partie est terminée");
            for(Player player : Bukkit.getOnlinePlayers()) {
                player.setGameMode(GameMode.SPECTATOR);
            }
            setGamePhase(4);
            return true;
        }
        if(command.getName().equalsIgnoreCase("reset")) {
            if(gameConfig.gamePhase < 3) {
                sender.sendMessage(ChatColor.RED + "Action impossible. La partie n'a pas encore commencé.");
                return true;
            }
            if(gameConfig.gamePhase == 3) {
                sender.sendMessage(ChatColor.RED + "Action impossible. La partie n'est pas terminée.");
                return true;
            }
            for(Player player : Bukkit.getOnlinePlayers()) {
                player.setGameMode(GameMode.ADVENTURE);
                player.teleport(new Location(world, 0, 252, 0));
            }
            teamManager.resetTeams();
            main.board.getObjective("scores").unregister();
            
            setGamePhase(1);
            return true;
        }
        
        if(command.getName().equalsIgnoreCase("inv")) {
            if(gameConfig.gamePhase == 0) {
                sender.sendMessage(ChatColor.RED + "Action impossible. La partie n'est pas initialisée. Veuillez utiliser /init");
                return true;
            }
            if(gameConfig.gamePhase > 2) {
                sender.sendMessage(ChatColor.RED + "Action impossible. La partie a déjà commencé");
                return true;
            }
            Player player = (Player) sender;
            player.setGameMode(GameMode.CREATIVE);
            startInventory.assign(player);
            gameConfig.onInvConfig.add(player);
            gameConfig.canClick.add(player);
            
            sender.sendMessage(ChatColor.GREEN + "Utilisez /save pour sauvegarder l'inventaire");
            
            return true;
        }
        return false;
    }
}
