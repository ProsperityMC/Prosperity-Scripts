# Prosperity Scripts

[![Discord](https://img.shields.io/discord/844449121376534558?color=%235865F2&label=discord&logo=discord&logoColor=white)](https://discord.gg/rC38tvFSEU/)

A collection of [Scarpet](https://github.com/gnembon/fabric-carpet/blob/master/docs/scarpet/Documentation.md) scripts performing various functions on the Prosperity server.

## Current Scripts

### Discord `v1.0.0`

A feature-rich Discord integration script utilizing [Discarpet](https://modrinth.com/mod/discarpet).

Make sure to review the included documentation before using it!

#### Features:

- Real time Discord ⇄ Minecraft chat bridge using webhooks.
- Whitelisting via Discord slash commands.
- Account linking system including role assignment.
- Assign users Discord roles when they're on the server.
- Player count status.
- Highly configurable.
- ...more

[discord.sc](https://github.com/ProsperityMC/Prosperity-Scripts/blob/main/admin/discord.sc)

### PoI `v1.0.0`

A script to facilitate better navigation around servers and to provide a better player experience for adding markers to BlueMap.

More information at the top of the file.

#### Features:
- Add, remove, and edit waypoints and view their locations
- Players can only remove or edit waypoints created by them
- Configurable permissions for adding and removing waypoints
- Fields for data migration in future updates
- Integrates seamlessly with [BlueMap](https://modrinth.com/plugin/bluemap) and [BlueMap Marker](https://modrinth.com/plugin/bmarker), see more at the top of the script

[poi.sc](https://github.com/ProsperityMC/Prosperity-Scripts/blob/main/utilities/poi.sc)

### Customize `v2.0.2`

A script giving players the power to customize their items' names, lore, and models.

Intended for use with a resource pack utilizing the vanilla `CustomModelData` NBT property. This script provides a suggestion and alias system which allows players to change custom custom models without having to remember a long string of numbers.

[customize.sc](https://github.com/ProsperityMC/Prosperity-Scripts/blob/main/utilities/customize.sc)

### Brand `v1.0.0`

A command which privately prints a player's client brand.

[brand.sc](https://github.com/ProsperityMC/Prosperity-Scripts/blob/main/admin/brand.sc)
