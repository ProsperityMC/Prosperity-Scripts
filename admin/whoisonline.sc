// WhoIsOnline
// v1.0.0
// Created by CarbonGhost (Discord: https://discord.gg/rC38tvFSEU).
// A script which registers a Discord command returning the names of all online players.
// WARNING: This script might conflict with other scripts that register application commands with Discarpet.
// Requires Discarpet (https://github.com/replaceitem/carpet-discarpet).

// The application ID for your bot.
global_bot_app_id = '987655240528588860';
// The ID of your Discord server.
global_server_id = '844449121376534558';
// The name of the slash command as it will appear in Discord.
global_command_name = 'whoisonline';
// The description of the command as it will appear in Discord.
global_command_description = 'Returns a list of players who are on the server.';




// Script:
__config() -> {
  'scope' -> 'global',
  'bot'-> global_bot_app_id,
};

global_server = dc_server_from_id(global_server_id);

task(_() -> initialize_commands());

initialize_commands() -> (
  // Remove all existing application commands before initializing.
  for(global_server ~ 'slash_commands',
      dc_delete(_);
  );

 dc_create_application_command(
    'slash_command', 
    {
      'name' -> global_command_name,
      'description' -> global_command_description,
    },
    global_server
  );
);

__on_discord_slash_command(command) -> (
  if(command ~ 'command_name' == global_command_name,
    task(_(outer(command)) -> (
      if(length(player('all')) == 0,
        response = 'There are no players online...',
      // else
        response = str(
          '**There are %d players online:**\n%s', 
          length(player('all')),                      // Player count.
          reduce(player('all'), _a + _ + '\n', ''),   // List of players on the server with newlines between them.
        )
      );

      dc_respond_interaction(command, 'RESPOND_IMMEDIATELY', response);
    ));
  // else
    return();
  );
);
