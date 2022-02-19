/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dorianmercier.cubeassemble.common;

import static com.dorianmercier.cubeassemble.common.gameConfig.nonCompatible;
import static com.dorianmercier.cubeassemble.common.gameConfig.setGamePhase;
import com.dorianmercier.cubeassemble.inventories.blockMenue;
import com.dorianmercier.cubeassemble.inventories.setup;
import com.dorianmercier.cubeassemble.inventories.setup_teams;
import com.dorianmercier.cubeassemble.inventories.teams;
import com.dorianmercier.cubeassemble.inventories.tools;
import static com.dorianmercier.cubeassemble.inventories.tools.createDisplay;
import static java.lang.Integer.max;
import static java.lang.Integer.min;
import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.Collection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Objective;

/**
 *
 * @author doria
 */
public class events implements Listener{
    public events(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public static void onInventoryClick(final InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        /*TODO Modify if condition later to allow inventory interations in the blocks room
          TODO Add an admin tag in order to make possible to open the menue in adventure mode*/
        
        // verify current item is not null
        final ItemStack clickedItem = e.getCurrentItem();
        Inventory inv = e.getInventory();
        if (clickedItem == null || clickedItem.getType().isAir() || gameConfig.gamePhase > 2) return;
        
        if(!player.getGameMode().equals(GameMode.CREATIVE) && !gameConfig.canClick.contains(player)) {
            e.setCancelled(true);
        }
        
        //Choice team menue
        if(inv.equals(teams.inv)) {
            // Using slots click is a best option for your inventory click's
            e.setCancelled(true);
            switch (clickedItem.getType()) {
                case BLUE_BANNER:
                    teamManager.addPlayer(main.blue, player);
                    break;
                case RED_BANNER:
                    teamManager.addPlayer(main.red, player);
                    break;
                case GREEN_BANNER:
                    teamManager.addPlayer(main.green, player);
                    break;
                case YELLOW_BANNER:
                    teamManager.addPlayer(main.yellow, player);
                    break;
                case ORANGE_BANNER:
                    teamManager.addPlayer(main.orange, player);
                    break;
                case PINK_BANNER:
                    teamManager.addPlayer(main.pink, player);
                    break;
                case BLACK_BANNER:
                    teamManager.addPlayer(main.black, player);
                    break;
                case GRAY_BANNER:
                    teamManager.addPlayer(main.gray, player);
                    break;
                case CYAN_BANNER:
                    teamManager.addPlayer(main.cyan, player);
                    break;
                default:
                    break;
            }
            player.closeInventory();
            return;
        }
        //Setup menue
        if(inv.equals(setup.inv)) {
            e.setCancelled(true);
            if (clickedItem.getType().equals(Material.GRASS_BLOCK)) {
                blockMenue.openInventory(player);
            }
            else if(clickedItem.getType().equals(Material.RED_BANNER)) {
                setup_teams.openInventory(player);
            }
            return;
        }
        
        //Team manager menue
        if(inv.equals(setup_teams.inv)) {
            e.setCancelled(true);
            boolean resetTeams = false;
            if (clickedItem.getType().equals(Material.LIME_BANNER) && gameConfig.numberTeams < 9) {
                inv.getItem(4).setAmount(++ gameConfig.numberTeams);
                resetTeams = true;
            }
            else if(clickedItem.getType().equals(Material.RED_BANNER) && gameConfig.numberTeams > 2) {
                inv.getItem(4).setAmount(-- gameConfig.numberTeams);
                resetTeams = true;
            }
            else if(clickedItem.getType().equals(Material.ARROW)) {
                setup.openInventory(player);
            }else if(clickedItem.getType().equals(Material.ICE)) {
                gameConfig.team_freezed = true;
                dataBase.setTeamsFreezed(true);
                log.info("The player " +  player.getName() + " freezed the teams");
                tools.createDisplay(Material.LIME_TERRACOTTA, 1, inv, 8, "Dévérouiller les teams", "");
            }else if(clickedItem.getType().equals(Material.LIME_TERRACOTTA)) {
                dataBase.setTeamsFreezed(false);
                gameConfig.team_freezed = false;
                tools.createDisplay(Material.ICE, 1, inv, 8, "Vérouiller les teams", "");
                log.info("The player " +  player.getName() + " unfreezed the teams");
            }
            
            
            if(resetTeams) {
                dataBase.setTeamsNumber(gameConfig.numberTeams);
                log.info("The player " + player.getName() + " updated teams number to " + gameConfig.numberTeams);
                teamManager.resetTeams();
                teams.update();
                setGamePhase(1);
            }
            return;
        }
        
        if(inv.equals(blockMenue.inv)) {
            e.setCancelled(true);
            switch (clickedItem.getType()) {
                case STONE:
                    gameConfig.canClick.add(player);
                    gameConfig.invBlocks.get(0).openInventory(player);
                    gameConfig.currentBlocksPage.put(player, 1);
                    break;
                case GOLD_INGOT:
                    gameConfig.invBlockConfig.get(0).openInventory(player);
                    gameConfig.currentConfigPage.put(player, 0);
                    break;
                case ARROW:
                    setup.openInventory(player);
                    break;
                default:
                    break;
            }
            return;
        }
        if(gameConfig.arrayContainsInventory(inv)) {
            e.setCancelled(true);
            int k = e.getSlot();
            int nbPoints;
            if(0 <= k && k <= 8) {
                ItemStack item = inv.getItem(k + 18);
                if(item != null) {
                    Material material = item.getType();
                    nbPoints = gameConfig.getPoints(material);
                    gameConfig.setPoints(material, ++nbPoints);
                    createDisplay(Material.GOLD_NUGGET, min(nbPoints, 64), inv, k + 27, "" + nbPoints, "");
                    log.info("The player " + player.getName() + " updated the points for " + material + " to " + nbPoints);
                    setGamePhase(1);
                }
                return;
            }
            if(9 <= k && k <= 17) {
                ItemStack item = inv.getItem(k + 9);
                if(item != null) {
                    Material material = item.getType();
                    nbPoints = max(gameConfig.getPoints(material) - 1, 0);
                    gameConfig.setPoints(material, nbPoints);
                    createDisplay(Material.GOLD_NUGGET, min(nbPoints, 64), inv, k + 18, "" + nbPoints, "");
                    log.info("The player " + player.getName() + " updated the points for " + material + " to " + nbPoints);
                    setGamePhase(1);
                }
                return;
            }
            if(k==45) {
                Integer currentPage = gameConfig.currentConfigPage.get(player);
                if(currentPage == null || currentPage == 0) {
                    blockMenue.openInventory(player);
                }
                else {
                    gameConfig.invBlockConfig.get(currentPage - 1).openInventory(player);
                    gameConfig.currentConfigPage.put(player, currentPage - 1);
                }
                return;
            }
            if(k==53) {
                Integer currentPage = gameConfig.currentConfigPage.get(player);
                gameConfig.invBlockConfig.get(currentPage + 1).openInventory(player);
                gameConfig.currentConfigPage.put(player, currentPage + 1);
                return;
            }
        }
        if(gameConfig.arrayContainsBlocksInventory(inv)) {
            int k = e.getSlot();
            if(k >= 45 && k < 54) {
                e.setCancelled(true);
                if(clickedItem.getType().equals(Material.ARROW)) {
                    int currentPage = gameConfig.currentBlocksPage.get(player);
                    if(currentPage == 1) {
                        blockMenue.openInventory(player);
                    }
                    else {
                        gameConfig.invBlocks.get(currentPage - 2).openInventory(player);
                        gameConfig.currentBlocksPage.put(player, currentPage - 1);
                    }
                }
                else if(clickedItem.getType().equals(Material.MAGENTA_GLAZED_TERRACOTTA)) {
                    int currentPage = gameConfig.currentBlocksPage.get(player);
                    gameConfig.invBlocks.get(currentPage).openInventory(player);
                    gameConfig.currentBlocksPage.put(player, currentPage + 1);
                }
            }
        }
    }
    
    @EventHandler
    public static void onInventoryClick(final InventoryDragEvent e) {
        Player player = (Player) e.getWhoClicked();
        if(!(player.getGameMode().equals(GameMode.CREATIVE)) && gameConfig.gamePhase < 3 && !gameConfig.canClick.contains(player)) {
          e.setCancelled(true);
        }
    }
    
    @EventHandler
    public static void onDropPlayer(final PlayerDropItemEvent e) {
        Player player = (Player) e.getPlayer();
        if (gameConfig.gamePhase < 3) {
        e.setCancelled(true);
        }
    }
    
    @EventHandler
    public static void onSwipeHandPlayer(final PlayerSwapHandItemsEvent e) {
        Player player = (Player) e.getPlayer();
        if (gameConfig.gamePhase < 3 && !gameConfig.canClick.contains(player)) {
        e.setCancelled(true);
        }
    }
    
    @EventHandler
    public static void onJoinPlayer(final PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if(gameConfig.gamePhase < 3) {
            player.getInventory().clear();
            player.getInventory().setItem(0, new ItemStack(Material.WHITE_BANNER, 1));
            player.teleport(new Location(Bukkit.getWorld("world"), 0, 312, 0));
            player.setGameMode(GameMode.ADVENTURE);
        }
        else {
            String teamName = gameConfig.playerLinkedTeam.get(player.getName());
            if(teamName == null) {
                e.getPlayer().setGameMode(GameMode.SPECTATOR);
                e.getPlayer().teleport(new Location(Bukkit.getWorld("world"), 0, 312, 0));
            }
        }
        if(!dataBase.isPlayer(e.getPlayer())) dataBase.addPlayer(e.getPlayer());
        teamManager.updatePlayer(e.getPlayer());
    }
    
    @EventHandler
    public static void onInteractPlayer(final PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if(gameConfig.gamePhase < 3 && player.getInventory().getItemInMainHand().equals(new ItemStack(Material.WHITE_BANNER))) {
            e.setCancelled(true);
            if(!gameConfig.team_freezed) teams.openInventory(player);
            return;
        }
        if(gameConfig.gamePhase > 2 && gameConfig.gamePhase < 5 && player.getInventory().getItemInMainHand().equals(gameConfig.compas)) {
            if(e.getAction().equals(Action.PHYSICAL) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) return;
            e.setCancelled(true);
            if(e.getHand() == EquipmentSlot.HAND) return;
            String name = player.getName();
            if(main.lastY.getScore(name).getScore() < 0) {
                Location location = player.getLocation();
                main.lastX.getScore(name).setScore(location.getBlockX());
                main.lastY.getScore(name).setScore(location.getBlockY());
                main.lastZ.getScore(name).setScore(location.getBlockZ());
                String worldName = location.getWorld().getName();
                int intWorld;
                if(worldName.equals("world")) {
                    intWorld = 0;
                }
                else if(worldName.equals("world_nether")) {
                    intWorld = -1;
                }
                else {
                    intWorld = 1;
                }
                log.info("Contenu de playerLinkedTeam : " + main.board.getEntryTeam(name).getName());
                main.lastWorld.getScore(name).setScore(intWorld);
                player.teleport(gameConfig.roomsLocations.get(main.board.getEntryTeam(name).getName()));
            }
            else {
                World world;
                int intWorld = main.lastWorld.getScore(name).getScore();
                if(intWorld == -1) world = Bukkit.getWorld("world_nether");
                else if(intWorld == 0) world = Bukkit.getWorld("world");
                else world = Bukkit.getWorld("world_the_end");
                
                Location destination = new Location(world, main.lastX.getScore(name).getScore()+0.5, main.lastY.getScore(name).getScore(), main.lastZ.getScore(name).getScore()+0.5);
                main.lastY.getScore(name).setScore(-10);
                player.teleport(destination);
            }
        }
    }
    
    @EventHandler
    public static void onCloseInventory(InventoryCloseEvent e) {
        if(gameConfig.arrayContainsBlocksInventory(e.getInventory())) {
            updateBlocks((Player) e.getPlayer());
        }
    }
    
    @EventHandler
    public static void onPlaceBlock(BlockPlaceEvent e) {
        //Managing block placemement in RoomBlocks
        if(gameConfig.gamePhase == 4) {
            Block block = e.getBlockPlaced();
            Location location = block.getLocation();
            int x = (int) location.getX(), z = (int) location.getZ();
            if(abs(x) < 160 && abs(z) < 160 && abs(location.getY()) > 309) {
                e.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public static void onBreakBlock(BlockBreakEvent e) {
        if(!e.getPlayer().getGameMode().equals(GameMode.CREATIVE) && (gameConfig.gamePhase ==3 || gameConfig.gamePhase ==4)) {
            Location location = e.getPlayer().getLocation();
            int x = (int) location.getX(), z = (int) location.getZ();
            if(abs(x) < 160 && abs(z) < 160 && abs(location.getY()) > 309) {
                e.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public static void onEmptyBucket(PlayerBucketEmptyEvent e) {
        if(!e.getPlayer().getGameMode().equals(GameMode.CREATIVE) && (gameConfig.gamePhase ==3 || gameConfig.gamePhase ==4)) {
            Location location = e.getPlayer().getLocation();
            int x = (int) location.getX(), z = (int) location.getZ();
            if(abs(x) < 100 && abs(z) < 100 && abs(location.getY()) > 309) {
                e.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public static void onFillBucket(PlayerBucketFillEvent e) {
        if(!e.getPlayer().getGameMode().equals(GameMode.CREATIVE) && (gameConfig.gamePhase ==3 || gameConfig.gamePhase ==4)) {
            Location location = e.getPlayer().getLocation();
            int x = (int) location.getX(), z = (int) location.getZ();
            if(abs(x) < 100 && abs(z) < 100 && abs(location.getY()) > 309) {
                e.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public static void onClickEntity(PlayerInteractEntityEvent e) {
        if(gameConfig.gamePhase == 4) {
            Player player = e.getPlayer();
            Entity entity = e.getRightClicked();
            Location location = entity.getLocation();
            int x = (int) location.getX(), z = (int) location.getZ();
            if(entity instanceof ItemFrame && abs(x) < 160 && abs(z) < 160 && abs(location.getY()) > 310) {
                ItemFrame clickedFrame = (ItemFrame) entity;
                if(clickedFrame.getItem().getType() != Material.AIR) return;
                Material currentMaterial = player.getInventory().getItemInMainHand().getType();
                World world = Bukkit.getWorld("world");
                Material checkMaterial;
                Collection<Entity> listEntities = new ArrayList(world.getEntitiesByClass((Class) ItemFrame.class));
                Collection<Entity> listEntitiesiteration = world.getEntitiesByClass((Class) ItemFrame.class);
                for(Entity curEntity : listEntitiesiteration) {
                    if(curEntity instanceof ItemFrame && distance(location, curEntity.getLocation()) <= 1) {
                        ItemFrame checkFrame = (ItemFrame) curEntity;
                        checkMaterial = checkFrame.getItem().getType();
                        if(!checkMaterial.equals(currentMaterial)) {
                            listEntities.remove(curEntity);
                        }
                    }
                    else listEntities.remove(curEntity);
                }
                if(listEntities.isEmpty()) {
                    e.setCancelled(true);
                }
                else {
                    obtainItem(player, currentMaterial);
                }
            }
        }
    }
    
    @EventHandler
    public static void onChangePlayerHunger(FoodLevelChangeEvent e) {
        if(gameConfig.gamePhase < 4 && e.getEntity() instanceof Player) {
            e.setCancelled(true);
            e.getEntity().setFoodLevel(20);
        }
    }
    
    @EventHandler
    public static void onChangePlayerHealth(EntityDamageEvent e) {
        if(gameConfig.gamePhase < 4 && e.getEntity() instanceof Player) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public static void onRespawnPlayer(PlayerRespawnEvent e) {
        if(gameConfig.gamePhase < 5 && gameConfig.gamePhase > 2) {
            gameConfig.giveCompas(e.getPlayer());
        }
    }
    
    private static void obtainItem(Player player, Material material) {
        String teamName = gameConfig.playerLinkedTeam.get(player.getName());
        Objective score = main.board.getObjective("scores");
        String teamNameString = main.board.getTeam(teamName).getColor() + teamName;
        int newScore = gameConfig.blocksConfig.get(material) + score.getScore(teamNameString).getScore();
        score.getScore(main.board.getTeam(teamName).getColor() + teamName).setScore(newScore);
        log.info("The player " + player.getName() + " has brought the block " + material.toString());
        Bukkit.broadcastMessage("L'équipe " + teamNameString + ChatColor.RESET + " a déposé " + material.toString().toLowerCase());
    }
    
    private static int distance(Location loc1, Location loc2) {
        return max(abs((int) (loc1.getX() - loc2.getX())), abs((int) (loc1.getZ() - loc2.getZ())));
    }
    
    private static void updateBlocks(Player player) {
        log.info("The player " + player.getName() + " updated the required blocks");
        gameConfig.updateBlocksConfig();
        gameConfig.updateInventoriesBlocksConfig();
        gameConfig.canClick.remove(player);
    }
}


