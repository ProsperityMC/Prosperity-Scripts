# Prosperity Scripts

[![Discord](https://img.shields.io/discord/844449121376534558?color=%235865F2&label=discord&logo=discord&logoColor=white)](https://discord.gg/rC38tvFSEU/)

A collection of [Scarpet](https://github.com/gnembon/fabric-carpet/blob/master/docs/scarpet/Documentation.md) scripts performing various functions on the Prosperity server.

## Current scripts

### Customize `v2.0.0`

A command which allows the player to modify the `CustomModelData` property of their items. Requires a resource pack to function. There are plans to expand this with features like colors and lore, as well as an option to require a certain experience level to run the command.

[customize.sc](https://github.com/ProsperityMC/Prosperity-Scripts/blob/main/utilities/customize.sc)

### Here `v1.0.0`

A command which prints the player's current location to the public chat. If they are in the overworld or nether it will print their equivalent location in the other dimension.

[here.sc](https://github.com/ProsperityMC/Prosperity-Scripts/blob/main/utilities/here.sc)

### Log4D `v0.0.0`

A script using [Discarpet](https://github.com/replaceitem/carpet-discarpet) which sends log events of your choice to your Discord. There are plans to add more features to this.

#### TODO

- [ ] Needs a rewrite.

[log4d.sc](https://github.com/ProsperityMC/Prosperity-Scripts/blob/main/admin/log4d.sc)

### Link4D `v1.0.0`

A script using [Discarpet](https://github.com/replaceitem/carpet-discarpet) which creates a two-way link between your Minecraft and Discord server chat.

#### TODO

- [ ] Show Discord formatting in Minecraft.
- [ ] Add clickable links in Minecraft.
- [ ] Handle messages which contain attachments in Minecraft.
- [ ] Fix some formatting in Discord.
- [ ] Add optional embeds.

[link4d.sc](https://github.com/ProsperityMC/Prosperity-Scripts/blob/main/admin/link4d.sc)

### Brand `v1.0.0`

A command which privately prints a player's client brand.

[brand.sc](https://github.com/ProsperityMC/Prosperity-Scripts/blob/main/admin/brand.sc)

### WhoIsOnline `v1.0.1`

A script using [Discarpet](https://github.com/replaceitem/carpet-discarpet) which create a Discord bot command to print the players who are currently on the server.

[whoisonline.sc](https://github.com/ProsperityMC/Prosperity-Scripts/blob/main/admin/whoisonline.sc)

### Waypoint `v0.0.0`

A script allowing players to create and manage waypoints that are stored persistently. Currently incomplete. There are plans to turn the waypoint management system into a library so that other scripts can add additional functionality to interact with waypoint databases.

#### TODO

- [ ] Finish the basic implementation.

[waypoint.sc](https://github.com/ProsperityMC/Prosperity-Scripts/blob/main/utilities/waypoint.sc)
