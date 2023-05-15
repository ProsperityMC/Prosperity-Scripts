////////////////////////////////////////////////////////////////////////////////
// Discord - READ THIS BEFORE USING THIS SCRIPT ----------------------------- //
////////////////////////////////////////////////////////////////////////////////
//
// Hello, and thanks for checking out this script. Please be aware the first use
// configuration is somewhat complex, so it is recommended that you review this
// documentation comment carefully!
// 
// Suppoort ----------------------------------------------------------------- //
// 
// Due to the complexity of the script and changes to the Discord or Scarpet API
// you may find that the script becomes unstable without warning or explanation.
// Should that happen you may find me (CarbonGhost) on Discord and mention or DM
// me.
//   • Discord: https://discord.gg/hfTxZ4XxYj
//   • Github:  https://github.com/ProsperityMC/Prosperity-Scripts
// 
// Perquisites -------------------------------------------------------------- //
// 
// This app requires Discarpet to be installed and configured, you can find info
// on setting it up here (https://replaceitem.github.io/carpet-discarpet/setup/)
// and get it from (https://modrinth.com/mod/discarpet).
// 
// Make sure you set the `intents` in Discarpet to include:
//   • MESSAGE_CONTENT
//   • GUILD_MEMBERS
//   • GUILD_PRESENCES
//
// Note that the script is only intended to work for one Discord server and one
// Minecraft server. If you're running multiple servers you need to setup the
// script and bot anew for each one. Inviting the bot to more than one Discord
// server is not a supported use case. 
//
// You also must setup Carpet to allow scripts to run server management commands
// if you wish to use features like whitelisting via Discord. You can do that by
// running `/carpet setDefau1t commandScriptACE 4` in-game.
// 
// Configuring -------------------------------------------------------------- //
// 
// Below this comment you will find an object called `global_config` which you
// will have to edit in order to run the script.
// 
// Comments will explain what each configuration option does, you only need
// replace the value on the right side of the arrow (->). Make sure to surround
// strings in 'single quotes'!
//
// Usage -------------------------------------------------------------------- //
// 
// Once you have configured the script and setup your channels and roles as you 
// wish, place the file in your `/[world]/scripts` folder then load the script 
// with `/script load [script_name]`.
//
// More information is available in-game with the `/[script_name] help` command.


global_config = {
  // The user ID / application ID of your Discord bot:
  'discord_bot_id'                -> '0000000000000000000',
  // The ID of your Discord server:
  'discord_server_id'             -> '000000000000000000',
  // Whether or not you wish to enable a status message displaying the number of
  // online players:
  'enable_player_count_status'    -> true,
  // The webhook URL to be used as your chat bridge:
  'chat_bridge_webhook_url'       -> 'https://discord.com/api/webhooks/0000000000000000000/xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx',
  // The chat message types you wish to forward to Discord.
  // Find message types in your Minecraft language file:
  'chat_bridge_message_whitelist' -> [
    'chat.type.text',
    'chat.type.announcement',
    'chat.type.emote',
    'chat.type.advancement.task',
    'chat.type.advancement.challenge',
    'chat.type.advancement.goal',
    'multiplayer.player.joined',
    'multiplayer.player.joined.renamed',
    'multiplayer.player.left',
    // Every death message as of 1.19.4:
    'death.attack.anvil', 'death.attack.anvil.player', 'death.attack.arrow', 'death.attack.arrow.item', 'death.attack.badRespawnPoint.link', 'death.attack.badRespawnPoint.message', 'death.attack.cactus', 'death.attack.cactus.player', 'death.attack.cramming', 'death.attack.cramming.player', 'death.attack.dragonBreath', 'death.attack.dragonBreath.player', 'death.attack.drown', 'death.attack.drown.player', 'death.attack.dryout', 'death.attack.dryout.player', 'death.attack.even_more_magic', 'death.attack.explosion', 'death.attack.explosion.player', 'death.attack.explosion.player.item', 'death.attack.fall', 'death.attack.fall.player', 'death.attack.fallingBlock', 'death.attack.fallingBlock.player', 'death.attack.fallingStalactite', 'death.attack.fallingStalactite.player', 'death.attack.fireball', 'death.attack.fireball.item', 'death.attack.fireworks', 'death.attack.fireworks.item', 'death.attack.fireworks.player', 'death.attack.flyIntoWall', 'death.attack.flyIntoWall.player', 'death.attack.freeze', 'death.attack.freeze.player', 'death.attack.generic', 'death.attack.generic.player', 'death.attack.hotFloor', 'death.attack.hotFloor.player', 'death.attack.indirectMagic', 'death.attack.indirectMagic.item', 'death.attack.inFire', 'death.attack.inFire.player', 'death.attack.inWall', 'death.attack.inWall.player', 'death.attack.lava', 'death.attack.lava.player', 'death.attack.lightningBolt', 'death.attack.lightningBolt.player', 'death.attack.magic', 'death.attack.magic.player', 'death.attack.message_too_long', 'death.attack.mob', 'death.attack.mob.item', 'death.attack.onFire', 'death.attack.onFire.item', 'death.attack.onFire.player', 'death.attack.outOfWorld', 'death.attack.outOfWorld.player', 'death.attack.player', 'death.attack.player.item', 'death.attack.sonic_boom', 'death.attack.sonic_boom.item', 'death.attack.sonic_boom.player', 'death.attack.stalagmite', 'death.attack.stalagmite.player', 'death.attack.starve', 'death.attack.starve.player', 'death.attack.sting', 'death.attack.sting.item', 'death.attack.sting.player', 'death.attack.sweetBerryBush', 'death.attack.sweetBerryBush.player', 'death.attack.thorns', 'death.attack.thorns.item', 'death.attack.thrown', 'death.attack.thrown.item', 'death.attack.trident', 'death.attack.trident.item', 'death.attack.wither', 'death.attack.wither.player', 'death.attack.witherSkull', 'death.attack.witherSkull.item', 'death.fell.accident.generic', 'death.fell.accident.ladder', 'death.fell.accident.other_climbable', 'death.fell.accident.scaffolding', 'death.fell.accident.twisting_vines', 'death.fell.accident.vines', 'death.fell.accident.weeping_vines', 'death.fell.assist', 'death.fell.assist.item', 'death.fell.finish', 'death.fell.finish.item', 'death.fell.killer', 
  ],
  // The channel ID for account link and admin command logs:
  'logging_channel_id'            -> '0000000000000000000',
  // The URL service you wish to use for Discord avatars.
  // Use `%s` as a placeholder for your player username.
  //
  // If this is not working make sure to include the file extension at the end
  // e.g. `.png`, `.jpg`:
  'chat_head_url_service'         -> 'https://minotar.net/avatar/%s.png',
  // If players are prompted to link their accounts upon joining the server.
  // Settings this to `false` effectively disables the account linking featurs
  // because players that don't have access to the server files have no way of
  // linking their account:
  'account_link_prompt'           -> true,
  // Assign a user with a linked an account a role when their account is first 
  // linked. Make sure the bot has permissions and is above this role otherwise 
  // it won't work.
  //
  // If you have something like a staff role above the bot that will give a Java
  // error every time they join the server, so make sure the bot is above those
  // users as well!
  //
  // Leave empty to assign no roles:
  'account_link_roles'            -> [],
  // Assign a user with a linked an account a role when they are online on
  // the server.
  //
  // Be warned: The bot will automatically update roles on the user each time
  // they join and leave the server which can rapidly spam your Discord server's
  // audit log, potentially causing problems for usability. It is recommended to
  // have some alternate form of logging via a 3rd party Discord bot.
  //
  // Leave empty to assign no roles:
  'account_link_online_roles'     -> [],
  // The roles required to run admin commands from Discord. This list is not
  // exclusive, having ANY of these roles allows the command to be run!
  //
  // Admin commands include:
  //   • /whitelist
  'admin_command_roles'           -> [],
  // The Discord commands which are available. Must by of the following types:
  //   • 'whitelist'
  //   • 'uptime'
  //   • 'playing_now'
  //   • 'seed'
  'discord_commands'              -> [
    'whitelist',
    'uptime',
    'playing_now',
    'seed',
  ],
  // The display name of your Minecraft server:
  'minecraft_server_name'         -> 'A Minecraft Server',
  // Your server icon as an image URL.
  //
  // If this is not working make sure to include the file extension at the end
  // e.g. `.png`, `.jpg`:
  'minecraft_server_icon_url'     -> 'https://raw.githubusercontent.com/replaceitem/carpet-discarpet/master/src/main/resources/assets/discarpet/icon.png',
  // How verbose the script's console logging will be:
  // → 0:  No logging
  // → 1:  Log on startup only
  // → 2:  Log on startup and Discord messages:
  'script_log_level'              -> 2,
};


////////////////////////////////////////////////////////////////////////////////
// Script - Any modifications beyond this point are at your own risk ---------//
////////////////////////////////////////////////////////////////////////////////


// Chat bridge -------------------------------------------------------------- //

handle_minecraft_bridge_message(message, translate) -> (
  if(global_config : 'chat_bridge_message_whitelist' ~ translate == null, return());
  
  task(_(outer(message), outer(translate)) -> (
      message_data = decode_json(encode_json(message));
      content = { 'content' -> message, 'allowed_mentions' -> { 'mention_users' -> true }};
      profile = {
        'name' -> global_config : 'minecraft_server_name',
        'avatar' -> global_config : 'minecraft_server_icon_url',
      };

      // Handle a special case where it is desireable for the user's Minecraft
      // head to show as the webhook profile:
      user_profile_match_list = [
        'chat.type.text',
        'chat.type.emote',
        'multiplayer.player.joined',
        'multiplayer.player.joined.renamed',
        'multiplayer.player.left',
      ];

      if(user_profile_match_list ~ translate != null,
        profile = {
          'name' -> message_data : 'with' : 0 : 'insertion',
          'avatar' -> str(
            global_config : 'chat_head_url_service', 
            message_data : 'with' : 0 : 'insertion'
          ),
        };
      );

      // Handle a special case where you would want the raw message content :
      raw_text_match_list = [
        'chat.type.text',
      ];

      if(raw_text_match_list ~ translate != null,
        content : 'content' = message_data : 'with' : 1 : 'text';
      );

      global_message_rate += 1;

      dc_send_webhook(global_webhook, content, profile);
  ));
);


handle_discord_bridge_message(message) -> (
  channel = message ~ 'channel';
  user = message ~ 'user';
  server = message ~ 'server';
  attachments = message ~ 'attachments';

  if(channel ~ 'id' != global_link_channel ~ 'id' ||
     user ~ 'id' == null                          ||
     user ~ 'is_bot' == true || null              ||
     user ~ 'is_self' == true || null,
    return();
  );

  display_name = dc_get_display_name(user, server);

  for(player('all'),
    print(
      player(_),
      format(
        // Minecraft chat formatting. Format for the username section of the 
        // message. Provides a hover tooltip which, when clicked, allows the 
        // user to mention a Discord user from Minecraft.
        str('g <@%s>', display_name), 
        str('^w Mention %s...', display_name), 
        str('?%s ', user ~ 'mention_tag')
      ) 
      // Concat the message content.
      + format(str('w  %s', message ~ 'content')) 
      // If the message contains attachments, concat a link to the first one.
      + if(length(attachments) != 0,
        format(
          'gu [Attachment]',
          str('^w %s', attachments : 0 ~ 'url'),
          str('@%s', attachments : 0 ~ 'url'),
        );
      );
    );
  );

  llog(2, 'info', str('<%s (@%s) via Discord> %s', user ~ 'name', user ~ 'id', message ~ 'readable_content'));
);


// Account linking ---------------------------------------------------------- //

init_linked_accounts() -> (
  f = read_file('linked-players', 'json');

  if(f == null,
    f = write_file('linked-players', 'json', {});
  );

  llog(1, 'info', str('Loaded %s linked Discord and Minecraft accounts', length(keys(f))));

  return(f);
);


write_linked_accounts() -> (
  task(_() -> (
    write_file('linked-players', 'json', global_linked_players);
  ));
);


update_linked_account_name(p) -> (
  uuid = p ~ 'uuid';
  
  if(
    global_linked_players ~ uuid != null &&
    global_linked_players : uuid : 'minecraft_name' != p,
    put(global_linked_players, uuid : 'minecraft_name', p);
    write_linked_accounts();
  );
);


prompt_account_link(p) -> (
  uuid = p ~ 'uuid';
  link_code = str(join('', map(range(4), floor(rand(9)))) + '-' + join('', map(range(4), floor(rand(9)))));

  if(global_config : 'account_link_prompt' != true, return());
  if(global_linked_players ~ uuid != null, return());

  put(global_player_link_codes, uuid, link_code);

  task(_(outer(p), outer(link_code)) -> (
    bot_name = dc_user_from_id(global_config : 'discord_bot_id') ~ 'discriminated_name';
    
    print(
      p,
      'You have not linked your Discord account to Minecraft. Do so by direct messaging '
      + format(
          str('#5865f2 @%s', split('#', bot_name) : 0), 
          str('^w Click to copy %s', bot_name),
          str('&%s', bot_name),
        )
      + ' the following code: '
      + format(
          str('gi %s', link_code), 
          str('^w Click to copy %s', link_code),
          str('&%s', link_code),
        )
    ));
  );
);


handle_account_link_message(message) -> (
  user = message ~ 'user';

  mc_uuid = first(
    global_player_link_codes, 
    global_player_link_codes : _ == message ~ 'readable_content'
  );
  linked_player_username = first(player('all'), _ ~ 'uuid' ~ mc_uuid);

  delete(global_player_link_codes, mc_uuid);

  if(linked_player_username != null,
    put(
      global_linked_players, 
      mc_uuid, 
      { 
        'discord_id' -> user ~ 'id', 
        'minecraft_name' -> linked_player_username,
        'link_time' -> unix_time(),
      },
    );
    write_linked_accounts();

    print(
      player(linked_player_username),
      str('Your account, %s, was successfully linked.', user ~ 'discriminated_name');
    );
  
    task(_(outer(message), outer(user), outer(linked_player_username), outer(mc_uuid)) -> (
      dc_send_message(
        user,
        {
          'content' -> str('Your account, **%s**, was successfully linked.', linked_player_username),
          'reply_to' -> message,
        },
      );

      for(global_linked_roles,
        dc_add_role(user, _, 'User linked account to Minecraft');
      );

      dc_send_message(
        global_logging_channel, 
        {
          'content' -> '',
          'embeds' -> [
            {
              'description' -> str(
                '<@%s> has linked their account to **%s**',
                user ~ 'id',
                linked_player_username,
              ),
              'fields' -> [
                {
                  'name' -> 'Discord User ID',
                  'value' -> user ~ 'id',
                },
                {
                  'name' -> 'Minecraft UUID',
                  'value' -> mc_uuid,
                },
                {
                  'name' -> 'Assigned Roles',
                  'value' -> (
                    if(length(global_linked_roles) == 0, 'None');
                    if(length(global_linked_roles) > 0, join(' ', map(global_linked_roles, _ ~ 'mention_tag')));
                  ),
                },
              ],
              'color' -> [138, 252, 153],
              'timestamp' -> global_linked_players : mc_uuid : 'link_time',
            },
          ],
        },
      );
    )),
  // else
    task(_(outer(user)) -> (
      dc_send_message(
        user, 
        'Failed to link account. This could be for several reasons, perhaps you entered the wrong code, or are not logged into the server.',
      );
    ));
  );
);


add_user_online_role(p) -> (
  task(_(outer(p)) -> (
    for(global_online_roles,
      role = _;
      uuid = p ~ 'uuid';

      if(global_linked_players ~ uuid != null,
        dc_add_role(
          dc_user_from_id(global_linked_players : uuid : 'discord_id'),
          role,
          'User joined the server'
        );
      );
    );
  ));
);


remove_user_online_role(p) -> (
  task(_(outer(p)) -> (
    for(global_online_roles,
      role = _;
      uuid = p ~ 'uuid';

      if(global_linked_players ~ uuid != null,
        dc_remove_role(
          dc_user_from_id(global_linked_players : uuid : 'discord_id'),
          role,
          'User left the server'
        );
      );
    );
  ));
);


// Player count status ------------------------------------------------------ //

update_status() -> (
  if(!global_config : 'enable_player_count_status',
    return();
  );

  count = length(player('all'));

  if(count == global_player_count_cache, return());
  global_player_count_cache = count;

  if(count  > 1, status = str('%s players', count));
  if(count == 1, status = str('%s player', count));
  if(count == 0, status = 'no players');

  dc_set_activity('watching', status);
);


// Utils -------------------------------------------------------------------- //

llog(level, kind, message) -> (
  if(global_config : 'script_log_level' >= level,
    logger(kind, str('[%s.sc] %s', system_info('app_name'), message));
  );
);


format_mc_error(error) -> str('*%s*', replace(error, '(<--\\[HERE\\])', ' **← here**'));


trim_unix_time(t) -> join('', map(range(10), split('', convert_date(map(range(6), convert_date(t) : _))) : _));


// Discord interactions ----------------------------------------------------- //

init_interactions() -> (
  task(_() -> (
    for(global_server ~ 'slash_commands', dc_delete(_));

    slash_commands = {
      'whitelist' -> {
        'name' -> 'whitelist',
        'description' -> 'Runs the whitelist command from within Minecraft',
        'options' -> [
          {
            'type' -> 'SUB_COMMAND',
            'name' -> 'add',
            'description' -> 'Add a user to the whitelist',
            'options' -> [
              {
                'type' -> 'STRING',
                'name' -> 'target',
                'description' -> 'The player to whitelist',
                'required' -> true,
              },
            ],
          },
          {
            'type' -> 'SUB_COMMAND',
            'name' -> 'remove',
            'description' -> 'Remove a user to the whitelist',
            'options' -> [
              {
                'type' -> 'STRING',
                'name' -> 'target',
                'description' -> 'The player to remove from the whitelist',
                'required' -> true,
              },
            ],
          },
          {
            'type' -> 'SUB_COMMAND',
            'name' -> 'list',
            'description' -> 'Lists all whitelisted players',
            'options' -> [],
          },
        ],
      },
      'uptime' -> {
        'name' -> 'uptime',
        'description' -> 'Returns the server start time',
      },
      'playing_now' -> {
        'name' -> 'playing_now',
        'description' -> 'Returns a list of online players',
      },
      'seed' -> {
        'name' -> 'seed',
        'description' -> 'Returns the world seed',
      },
    };

    for(global_config : 'discord_commands', dc_create_application_command('slash_command', slash_commands : _, global_server));
  ));
);


slash_whitelist(command) -> (
  task(_(outer(command)) -> (
    messages = [];
    user = command ~ 'user';
    user_roles = dc_get_user_roles(user, global_server);
    args = command ~ 'arguments_by_name';

    if(map(global_config : 'admin_command_roles', map(user_roles, user_roles : _i ~ 'id') ~ _) == [null],
      dc_respond_interaction(
        command,
        'RESPOND_IMMEDIATELY',
        {
          'content' -> 'You don\'t have permission to do that',
          'ephemeral' -> true,
        },
      );

      return();
    );

    if(args : 'add', 
      target = args : 'target' ~ 'value';
      result = run(str('whitelist add %s', target));

      // Hacky way of checking that the user was actually added to the whitelist
      // without having to compare the whitelist values directly.
      if(length(split('Added', result : 1 : 0)) == 2,
        dc_send_message(
          global_logging_channel, 
          {
            'content' -> '',
            'embeds' -> [
              {
                'description' -> str(
                  '<@%s> added **%s** to the whitelist',
                  user ~ 'id',
                  target,
                ),
                'color' -> [252, 214, 138],
                'timestamp' -> unix_time(),
              },
            ],
          },
        );
      );
    );
    if(args : 'remove', 
      target = args : 'target' ~ 'value';
      result = run(str('whitelist remove %s', target));

      // Hacky way of checking that the user was actually removed from the
      // whitelist without having to compare the whitelist values directly.
      if(length(split('Removed', result : 1 : 0)) == 2,
        dc_send_message(
          global_logging_channel, 
          {
            'content' -> '',
            'embeds' -> [
              {
                'description' -> str(
                  '<@%s> removed **%s** from the whitelist',
                  user ~ 'id',
                  target,
                ),
                'color' -> [252, 214, 138],
                'timestamp' -> unix_time(),
              },
            ],
          },
        );
      );
    );
    if(args : 'list', result = run('whitelist list'));

    // `null` is equal to the error value, so `null` means the command succeeded.
    if(result : 2 == null, 
      messages = result : 1,
    // else
      messages = [format_mc_error(result : 2)];
      
      if(system_info('world_carpet_rules') : 'commandScriptACE' != 4,
        map(
          [
            '\nThere is a problem with your Carpet configuration, insufficient command permissions, run the following command in-game to fix it:',
            '```',
            '/carpet setDefault commandScriptACE 4',
            '```',
          ],
          messages += _;
        );
      );
    );

    dc_respond_interaction(
      command,
      'RESPOND_IMMEDIATELY',
      {
        'content' -> join('\n', messages),
        'ephemeral' -> true,
      },
    );
  ));
);


slash_uptime(command) -> (
  task(_(outer(command)) -> (
    timestamp = trim_unix_time(global_start_time);

    dc_respond_interaction(
      command,
      'RESPOND_IMMEDIATELY',
      {
        'content' -> str('%s has been online since <t:%s:f>', global_config : 'minecraft_server_name', timestamp),
        'ephemeral' -> true,
      },
    );
  ));
);


slash_playing_now(command) -> (
  task(_(outer(command)) -> (
    msg = 'Could not get online players...';
    players = player('all');
    count = length(players);

    if(count == 0, msg = 'There are no players online');
    if(count == 1, msg = str('**There is %s player online:** %s', count, join('', players)));
    if(count > 1, msg = str('**There are %s players online:**\n%s', count, join('\n', players)));
    
    dc_respond_interaction(
      command,
      'RESPOND_IMMEDIATELY',
      {
        'content' -> msg,
        'ephemeral' -> true,
      },
    );
  ));
);


slash_seed(command) -> (
  task(_(outer(command)) -> (
    dc_respond_interaction(
      command,
      'RESPOND_IMMEDIATELY',
      {
        'content' -> str('```\n%s```', system_info('world_seed')),
        'ephemeral' -> true,
      },
    );
  ));
);


// Command handlers --------------------------------------------------------- //

command_link_list() -> (
  task(_() -> (
    count = length(global_linked_players);

    if(count  > 1, msg = 'There are %s linked accounts:');
    if(count == 1, msg = 'There is %s linked account:');
    if(count == 0, msg = 'There are no linked accounts.');

    print(str(msg, length(global_linked_players)));

    map(global_linked_players,
      print(
        format(
          str('w %s ', global_linked_players : _ : 'minecraft_name'),
          str('^w UUID: %s', global_linked_players ~ _),
          str('&%s', global_linked_players ~ _),
        )
        + format(
          str('w (%s)', dc_user_from_id(global_linked_players : _ : 'discord_id') ~ 'discriminated_name'),
          str('^w Discord ID: %s', global_linked_players : _ : 'discord_id'),
          str('&%s', global_linked_players : _ : 'discord_id'),
        );
      );
    );
  ));
);


command_link_reload() -> (
  global_linked_players = init_linked_accounts();
      
  for(player('all'), 
    update_linked_account_name(_);
    prompt_account_link(_);
    add_user_online_role(_);
  );

  print('Linked accounts reloaded...');
);


command_link_remove(linked_player) -> (
  k = first(global_linked_players, global_linked_players : _ : 'minecraft_name' == linked_player);

  if(k == null,
    print(format(str('r Could not remove %s because an entry with that name does not exist', linked_player)));
    
    return(),
  // else
    task(_(outer(k)) -> (
      for(global_linked_roles,
        dc_remove_role(dc_user_from_id(global_linked_players : k : 'discord_id'), _, 'User unlinked account from Minecraft');
      );

      dc_send_message(
        global_logging_channel, 
        {
          'content' -> '',
          'embeds' -> [
            {
              'description' -> str(
                '<@%s> has account has been unlinked from **%s**',
                global_linked_players : k : 'discord_id',
                global_linked_players : k : 'minecraft_name',
              ),
              'fields' -> [
                {
                  'name' -> 'Discord User ID',
                  'value' -> global_linked_players : k : 'discord_id',
                },
                {
                  'name' -> 'Minecraft UUID',
                  'value' -> k,
                },
                {
                  'name' -> 'Unassigned Roles',
                  'value' -> (
                    if(length(global_linked_roles) == 0, 'None');
                    if(length(global_linked_roles) > 0, join(' ', map(global_linked_roles, _ ~ 'mention_tag')));
                  ),
                },
              ],
              'color' -> [252, 152, 138],
              'timestamp' -> unix_time(),
            },
          ],
        },
      );
      
      print(str('Removed %s\'s linked account', global_linked_players : k : 'minecraft_name'));

      delete(global_linked_players, k);
      write_linked_accounts();
    ));
  );
);


command_help() -> (
  messages = [
    '/%s link list - Lists the Minecraft and Discord usernames of all linked accounts',
    '/%s link reload - Reloads the account linking system, including data from files, user roles, and names',
    '/%s link remove <linked_player> - Removes a linked account by Minecraft username',
    '/%s help - Prints this very message',
  ];

  print(join('\n', map(messages, str(_, system_info('app_name')))));
);


// Scarpet config ----------------------------------------------------------- //

__config() -> {
  'scope' -> 'global',
  'bot'-> global_config : 'discord_bot_id',
  'requires' -> { 'discarpet' -> '*' },
  'commands' -> {
    'link list' -> _() -> command_link_list(),
    'link reload' -> _() -> command_link_reload(),
    'link remove <linked_player>' -> _(linked_player) -> command_link_remove(linked_player),
    'help' -> _() -> command_help(),
  },
  'arguments' -> {
    'linked_player' -> {
      'type' -> 'term',
      'suggester' -> _(arg) -> (
        return(map(global_linked_players, global_linked_players : _ : 'minecraft_name'));
      ),
    },
  },
};


// Globals ------------------------------------------------------------------ //

task(_() -> (
  global_webhook = dc_webhook_from_url(global_config : 'chat_bridge_webhook_url');
  global_link_channel = dc_channel_from_id(global_webhook ~ 'channel' ~ 'id');
));
global_logging_channel = dc_channel_from_id(global_config : 'logging_channel_id');
global_server = dc_server_from_id(global_config : 'discord_server_id');
global_linked_roles = map(global_config : 'account_link_roles', dc_role_from_id(_));
global_online_roles = map(global_config : 'account_link_online_roles', dc_role_from_id(_));
global_admin_roles = map(global_config : 'admin_command_roles', dc_role_from_id(_));
global_message_rate = 0;          // In ticks.
global_next_status_update = tick_time() + 40;
global_player_count_cache = -1;   // Initialize to -1 to force status update the first time.
global_linked_players = init_linked_accounts();
global_player_link_codes = {};
global_next_role_update = tick_time() + 40;
global_start_time = unix_time();


// Events ------------------------------------------------------------------- //

__on_start() -> (llog(1, 'info', 'Discord link loaded!'));


init_interactions(); // Not an event but I put it here anyways


__on_tick() -> (
  global_message_rate = 0;

  if(tick_time() == global_next_status_update,
    update_status();
    global_next_status_update += 100;
  );
);


__on_system_message(message, translate) -> (
  handle_minecraft_bridge_message(message, translate);
);


__on_discord_message(message) -> (
  channel = message ~ 'channel';
  user = message ~ 'user';
  
  if(user ~ 'is_self' == true || null ||
     user ~ 'is_bot'  == true || null, 
    return();
  );
  
  // If the message is in a DM it will handle it as a account linking request.
  if(channel ~ 'type' == 'PRIVATE_CHANNEL', handle_account_link_message(message));

  // If the message is in the chat bridge channel it will handle it as a chat
  // bridge message.
  if(channel ~ 'id' == global_link_channel ~ 'id', handle_discord_bridge_message(message));
);


__on_player_connects(p) -> (
  prompt_account_link(p);
  update_linked_account_name(p);
  add_user_online_role(p);
);


__on_player_disconnects(p, _) -> (
  if(global_player_link_codes != null || {},
    delete(global_player_link_codes, p ~ 'uuid');
  );
  remove_user_online_role(p);
);


__on_discord_slash_command(command) -> (
  if(command ~ 'command_name' == 'whitelist', slash_whitelist(command));
  if(command ~ 'command_name' == 'uptime', slash_uptime(command));
  if(command ~ 'command_name' == 'playing_now', slash_playing_now(command));
  if(command ~ 'command_name' == 'seed', slash_seed(command));
);
