# cubeassemble
Minecraft 1.19 plugin for CubeAssemble game

CubeAssemble version 2.3 is now on release !

CubeAssemble is a minecraft mini-game created by ElD0d0 (Dorian Mercier) and this is the spigot plugin associated.

**Game presentation:** The main objectif is for each team to bring the maximum of the items required in a room, in order to collect points. The winner is the team that will have collected the most points. All the players start in 0 0 with a starting inventory and have 2 hours to bring all the items they can to their team blockRoom. Before the game starts, a list of items is made by the host and each item has an associated number of points. During the game, all players can teleport themself into their team blockRoom to bring an item.

**Game description:** 
- Game duration: 2 hours
- Number of teams: between 2 and 9
- Number of players: between 2 and the maximum your server can handle

**Technical information**: To configure the game, you need to run the following commands in the order they are presented
- /init -> When loading the plugin for the first time, use this command to initiate the game
- /setup -> This is the command that allow you to configure all the game. Explore the different buttons in order to make your configuration
- /inv -> Run this command to configure starting inventory. Equip yourself with the desired items and then, use /save to save your starting inventory
- /save -> After running /inv, run this command to save your new starting inventory
- /ready -> This command will prepare the game and initiate all the blockRooms. When you change blocks configuration or the number of teams, you must run again this command.
- /start -> Run this command once all the players have joined their team and the configuration is finished. This will start the game and teleport all the player on the map.
- /finish -> Stop and end the game.
- /reset -> Reset the game, the scores and teleport all the players to the central zone. The game is then ready to restart (the map is not reset)

**Plugin installation:** In order to save your game configuration after stopping your server, this plugin uses a postgreSQL database. Before starting your server, you need to install postgreSQL and put the password "postgres" for the user postgres. Then, you need to run the file "reset_database.bat" from the folder scripts. Once you have done that without error, you can start the server with the plugin.

You can find a ready-to-run server whith that release (with bukkit.yml and server.properties well configured).
