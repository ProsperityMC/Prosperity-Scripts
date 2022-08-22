// WhoIsOnline
// v1.0.1
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
// Whether or not the command output is "ephemeral" (the output is shown only to the person who runs it).
global_command_is_ephemeral = true;




// Script:
__config() -> {
  'scope' -> 'global',
  'bot'-> global_bot_app_id,
};

global_server = dc_server_from_id(global_server_id);

delete_all_commands() -> (
  for(global_server ~ 'slash_commands',
    dc_delete(_);
    logger('info', str('Deleted %s.', _));
  );
  logger('info', 'Deleted all Discord application commands.');
);

initialize_commands() -> (
 // Delete the command if it has already been registered.
 existing_command = global_server ~ 'slash_commands' ~ 'name' ~ global_command_name;

 if(existing_command != null,
    dc_delete(existing_command);
 );

 // Register the new command.
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
      player_count = length(player('all'));

      if(player_count == 0,
        response = 'There are no players online...',
      // else
        response = str(
          '**There %s %d player%s online:**\n%s',
          if(player_count == 1, 'is', 'are'),         // Can't be having incorrect grammar, can we?
          player_count,                               // Player count.
          if(player_count == 1, '', 's'),
          reduce(player('all'), _a + _ + '\n', ''),   // List of players on the server with newlines between them.
        )
      );

      dc_respond_interaction(command, 'RESPOND_IMMEDIATELY', { 'content' -> response, 'ephemeral' -> global_command_is_ephemeral });
    ));
  // else
    return();
  );
);

task(_() -> initialize_commands());
