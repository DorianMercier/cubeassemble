package com.dorianmercier.cubeassemble.common;

import static com.dorianmercier.cubeassemble.common.gameConfig.setGamePhase;
import com.dorianmercier.cubeassemble.inventories.setup;
import com.dorianmercier.cubeassemble.inventories.startInventory;
import com.dorianmercier.cubeassemble.structures.blockRooms;
import com.dorianmercier.cubeassemble.structures.spawn;
import com.dorianmercier.cubeassemble.threads.scoreboard;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
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

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        World world = Bukkit.getWorld("world");
        
        if(command.getName().equalsIgnoreCase("giveCompass")) {
            if(args.length != 1) return false;
            
            try {
                gameConfig.giveCompas(Bukkit.getPlayer(args[0]));
                return true;
            }
            catch(Exception e) {
                sender.sendMessage("Impossible de donner le compass à ce joueur");
                return false;
            }
        }
        
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
            
            world.setTime(6000);
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            world.setClearWeatherDuration(10);
            world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
            
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
            
            Player playerSender = (Player) sender;
            playerSender.chat("/recipe give @a *");
            scoreboard.start(plugin);      
            
            for(Player player : Bukkit.getOnlinePlayers()) {
                if(gameConfig.playerLinkedTeam.containsKey(player.getName())) {
                    player.setGameMode(GameMode.SURVIVAL);
                    player.teleport(new Location(world, 0, 250, 0));
                    startInventory.assign(player);
                    gameConfig.giveCompas(player);
                    main.lastY.getScore(player.getName()).setScore(-1);
                }
                else {
                    player.setGameMode(GameMode.SPECTATOR);
                }
            }
            world.setTime(0);
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
            world.setClearWeatherDuration(0);
            world.setGameRule(GameRule.DO_WEATHER_CYCLE, true);
            Bukkit.broadcastMessage("Début de la partie. Vous avez 30 secondes d'invulnérabilité");
            gameConfig.updateRoomsLocation();
            setGamePhase(3);
            Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(30000);
                        //If /finish has been called bebore, do not execute following lines
                        if(gameConfig.gamePhase < 5) {
                            Bukkit.broadcastMessage("Période d'invulnérabilité terminée.");
                            setGamePhase(4);
                        }
                    }
                    catch(InterruptedException e) {
                        log.error("Thread error in start command : " + e);
                    }
                }
            });
        }
        if(command.getName().equalsIgnoreCase("finish")) {
            if(gameConfig.gamePhase < 3) {
                sender.sendMessage(ChatColor.RED + "Action impossible. La partie n'a pas encore commencé.");
                return true;
            }
            if(gameConfig.gamePhase > 4) {
                sender.sendMessage(ChatColor.RED + "Action impossible. La partie est déjà terminée.");
                return true;
            }
            return finish();
        }
        if(command.getName().equalsIgnoreCase("reset")) {
            if(gameConfig.gamePhase < 3) {
                sender.sendMessage(ChatColor.RED + "Action impossible. La partie n'a pas encore commencé.");
                return true;
            }
            if(gameConfig.gamePhase == 3 || gameConfig.gamePhase == 4) {
                sender.sendMessage(ChatColor.RED + "Action impossible. La partie n'est pas terminée.");
                return true;
            }
            for(Player player : Bukkit.getOnlinePlayers()) {
                player.setGameMode(GameMode.ADVENTURE);
                player.teleport(new Location(world, 0, 252, 0));
                player.getInventory().clear();
                player.getInventory().setItem(0, new ItemStack(Material.WHITE_BANNER, 1));
            }
            teamManager.resetTeams();
            main.board.getObjective("scores").unregister();
            main.board.getObjective("scoreboard").unregister();
            
            world.setTime(6000);
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            world.setClearWeatherDuration(10);
            world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
            
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
    
    public static boolean finish() {
        Bukkit.broadcastMessage("La partie est terminée");
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.setGameMode(GameMode.SPECTATOR);
        }
        setGamePhase(5);
        return true;
    }
}
